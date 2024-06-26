package com.ferreusveritas.resources.model.qsp;

import com.ferreusveritas.math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Quadtree Space Partitioner
 * <p>
 * Quadtrees are a great way to partition a 2D space into smaller and smaller
 * quadrants.  This is a Quadtree Space Partitioner that is used to determine
 * if a point is inside a 3D model.
 **/
public class QspModelPart {
	
	private static final int MIN_FACES = 3;
	private static final int DEFAULT_DEPTH = 4;
	private final AABBD aabb;
	private final QSPNode root;
	
	public QspModelPart(List<SimpleFace> faces, int depth) {
		this.aabb = calculateAABB(faces).orElseThrow(() -> new IllegalArgumentException("No faces to calculate AABB"));
		this.root = subdivide(faces, aabb.toRect(), depth);
	}
	
	public QspModelPart(List<SimpleFace> faces) {
		this(faces, DEFAULT_DEPTH);
	}
	
	private Optional<AABBD> calculateAABB(List<SimpleFace> faces) {
		if(faces.isEmpty()) {
			return Optional.empty();
		}
		AABBD box = faces.get(0).getAABB();
		int faceCount = faces.size();
		for(int i = 1; i < faceCount; i++) {
			AABBD faceAABB = faces.get(i).getAABB();
			box = box.union(faceAABB);
		}
		return Optional.of(box);
	}
	
	public boolean pointIsInside(Vec3D pos) {
		if(!aabb.contains(pos)) {
			return false;
		}
		int count = 0;
		List<SimpleFace> list = root.getFaces(pos);
		Vec3D[] tri = new Vec3D[3];
		Line3D line = new Line3D(pos, pos.withY(aabb.max().y() + 1.0));
		for(SimpleFace face : list) {
			face.getVertices(tri);
			if(Collision3D.lineInTriangle(line, tri)) {
				count++;
			}
		}
		return count % 2 == 1;
	}
	
	public AABBD getAABB() {
		return aabb;
	}
	
	private static QSPNode subdivide(List<SimpleFace> faces, RectD rect, int depth) {
		if(faces.size() < MIN_FACES || depth == 0) {
			return new QSPNode(rect, faces, null);
		}
		RectD[] rects = rect.subdivide();
		byte[] sortBits = sortFaces(faces, rects);
		int[] counts = countFaces(sortBits);
		QSPNode[] nodes = childNodes(faces, rects, depth, sortBits, counts);
		
		return new QSPNode(rect, List.of(), nodes);
	}
	
	private static byte[] sortFaces(List<SimpleFace> faces, RectD[] rects) {
		byte[] bits = new byte[faces.size()];
		
		for(int i = 0; i < faces.size(); i++) {
			SimpleFace face = faces.get(i);
			int bit = 0;
			for(int quadrant = 0; quadrant < 4; quadrant++) {
				if(face.rectIntersects(rects[quadrant])) {
					bit |= 1 << quadrant;
				}
			}
			bits[i] = (byte)bit;
		}
		
		return bits;
	}
	
	private static int[] countFaces(byte[] sortBits) {
		int[] counts = new int[4];
		for (byte bit : sortBits) {
			counts[0] += (bit & 1);
			counts[1] += (bit & 2) >> 1;
			counts[2] += (bit & 4) >> 2;
			counts[3] += (bit & 8) >> 3;
		}
		return counts;
	}
	
	private static QSPNode[] childNodes(List<SimpleFace> faces, RectD[] rects, int depth, byte[] sortBits, int[] counts) {
		QSPNode[] nodes = new QSPNode[4];
		int totalCounts = 0;
		for(int quadrant = 0; quadrant < 4; quadrant++) {
			totalCounts += counts[quadrant];
			nodes[quadrant] = childNode(faces, rects[quadrant], depth, sortBits, counts[quadrant], quadrant);
		}
		return totalCounts != 0 ? nodes : null;
	}
	
	private static QSPNode childNode(List<SimpleFace> faces, RectD rect, int depth, byte[] sortBits, int count, int quadrant) {
		if(count == 0) {
			return null;
		}
		List<SimpleFace> childList = new ArrayList<>(count);
		for(int j = 0; j < sortBits.length; j++) {
			int bit = sortBits[j];
			if((bit & (1 << quadrant)) != 0) {
				childList.add(faces.get(j));
			}
		}
		return subdivide(childList, rect, depth - 1);
	}
	
	private static class QSPNode {
		private final RectD rect;
		private final List<SimpleFace> faces; // Should never be null
		private final QSPNode[] children; // May be null or have null elements
		
		public QSPNode(RectD rect, List<SimpleFace> faces, QSPNode[] children) {
			this.rect = rect;
			this.faces = faces;
			this.children = children;
		}
		
		public RectD getRect() {
			return rect;
		}
		
		public QSPNode getChild(Vec3D pos) {
			double midX = (getRect().x1() + getRect().x2()) / 2;
			double midZ = (getRect().z1() + getRect().z2()) / 2;
			int index = (pos.x() >= midX ? 1 : 0) + (pos.z() >= midZ ? 2 : 0);
			return children[index];
		}
		
		public List<SimpleFace> getFaces(Vec3D pos) {
			if(!faces.isEmpty()) {
				return faces;
			}
			if(children == null) {
				return List.of();
			}
			QSPNode child = getChild(pos);
			if(child != null) {
				return child.getFaces(pos);
			}
			return List.of();
		}
		
	}
	
}
