package com.ferreusveritas.model;

import com.ferreusveritas.math.*;

import java.util.ArrayList;
import java.util.List;

public class QSP {
	
	private static final int MIN_FACES = 3;
	private static final int MAX_DEPTH = 4;
	private final QSPNode root;
	private final AABBD aabb;
	private final SimpleMeshModel model;
	
	private QSP(QSPNode root, AABBD aabb, SimpleMeshModel model) {
		this.root = root;
		this.aabb = aabb;
		this.model = model;
	}
	
	public static QSP load(SimpleMeshModel model) {
		AABBD aabb = model.getAABB();
		QSPNode node = subdivide(model.getFaces(), aabb.toRect(), 0);
		return new QSP(node, aabb, model);
	}
	
	public boolean pointIsInside(Vec3D pos) {
		return pointIsInsideSlow(pos);
	}
	
	public boolean pointIsInsideSlow(Vec3D pos) {
		if(!aabb.isInside(pos)) {
			return false;
		}
		int count = 0;
		for(SimpleFace face : model.getFaces()) {
			Line3D line = new Line3D(pos, pos.withY(aabb.max().y() + 1.0));
			Vec3D a = face.getVertex(0);
			Vec3D b = face.getVertex(1);
			Vec3D c = face.getVertex(2);
			Vec3D[] tri = new Vec3D[] {a, b, c};
			if(Collision.lineInTriangle(line, tri)) {
				count++;
			}
		}
		return count % 2 == 1;
	}
	
	public boolean pointIsInsideFast(Vec3D pos) {
		if(!aabb.isInside(pos)) {
			return false;
		}
		List<SimpleFace> faces = getFaces(pos);
		Line3D line = new Line3D(pos, pos.withY(aabb.max().y() + 1.0));
		int count = 0;
		for(SimpleFace face : faces) {
			Vec3D a = face.getVertex(0);
			Vec3D b = face.getVertex(1);
			Vec3D c = face.getVertex(2);
			Vec3D[] tri = new Vec3D[] {a, b, c};
			if(Collision.lineInTriangle(line, tri)) {
				count++;
			}
		}
		return count % 2 == 1;
	}
	
	public List<SimpleFace> getFaces(Vec3D pos) {
		List<SimpleFace> faces = new ArrayList<>();
		root.getFaces(pos, faces);
		return faces;
	}
	
	private static QSPNode subdivide(List<SimpleFace> faces, RectD rect, int depth) {
		if(faces.size() < MIN_FACES || depth == MAX_DEPTH) {
			return new QSPNode(rect, faces, null);
		}
		RectD[] rects = rect.subdivide();
		byte[] sortBits = sortFaces(faces, rects);
		int[] counts = countFaces(sortBits);
		List<SimpleFace> thisFaces = thisFaces(faces, sortBits, counts);
		QSPNode[] nodes = childNodes(faces, rects, depth, sortBits, counts);
		
		return new QSPNode(rect, thisFaces, nodes);
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
		int[] counts = new int[5];
		for (byte bit : sortBits) {
			if (bit == 0b1111) {
				counts[4]++;
			} else {
				counts[0] += (bit & 1);
				counts[1] += (bit & 2) >> 1;
				counts[2] += (bit & 4) >> 2;
				counts[3] += (bit & 8) >> 3;
			}
		}
		return counts;
	}
	
	private static List<SimpleFace> thisFaces(List<SimpleFace> faces, byte[] sortBits, int[] counts) {
		List<SimpleFace> thisFaces;
		int count = counts[4];
		if(count == 0) {
			return List.of();
		}
		thisFaces = new ArrayList<>(count);
		for(int i = 0; i < sortBits.length; i++) {
			if(sortBits[i] == 0b1111) {
				thisFaces.add(faces.get(i));
			}
		}
		return thisFaces;
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
			if((sortBits[j] | quadrant) != 0) {
				childList.add(faces.get(j));
			}
		}
		return subdivide(childList, rect, depth + 1);
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
		
		public void getFaces(Vec3D pos, List<SimpleFace> facesToAddTo) {
			facesToAddTo.addAll(faces);
			if(children == null) {
				return;
			}
			QSPNode child = getChild(pos);
			if(child != null) {
				child.getFaces(pos, facesToAddTo);
			}
		}
		
	}
	
}
