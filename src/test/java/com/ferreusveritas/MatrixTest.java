package com.ferreusveritas;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatrixTest {
	
	@Test
	void test() {
		
		Vec3D p = new Vec3D(1, 2, 3);
		
		Matrix4X4 ident = new Matrix4X4();
		Vec3D p1 = ident.transform(p);
		assertEquals(p, p1);
		
		Matrix4X4 trans = Matrix4X4.translateMatrix(new Vec3D(1, 2, 3));
		Vec3D p2 = trans.transform(p);
		assertEquals(new Vec3D(2, 4, 6), p2);
		
		Matrix4X4 rotX = Matrix4X4.rotateXMatrix(Math.PI / 2);
		Vec3D p3 = rotX.transform(p);
		assertEquals(new Vec3D(1, 3, -2), p3);
		
		Matrix4X4 rotY = Matrix4X4.rotateYMatrix(Math.PI / 2);
		Vec3D p4 = rotY.transform(p);
		assertEquals(new Vec3D(-3, 2, 1), p4);
		
		Matrix4X4 rotZ = Matrix4X4.rotateZMatrix(Math.PI / 2);
		Vec3D p5 = rotZ.transform(p);
		assertEquals(new Vec3D(2, -1, 3), p5);
		
		Matrix4X4 scale = Matrix4X4.scaleMatrix(new Vec3D(2, 3, 4));
		Vec3D p6 = scale.transform(p);
		assertEquals(new Vec3D(2, 6, 12), p6);
		
	}
	
	@Test
	void test2() {
		
		Vec3D p = new Vec3D(1, 2, 3);
		
		Matrix4X4 trans = new Matrix4X4().translate(new Vec3D(1, 2, 3));
		Vec3D p2 = trans.transform(p);
		assertEquals(new Vec3D(2, 4, 6), p2);
		
		Matrix4X4 rotX = new Matrix4X4().rotateX(Math.PI / 2);
		Vec3D p3 = rotX.transform(p);
		assertEquals(new Vec3D(1, 3, -2), p3);
		
		Matrix4X4 rotY = new Matrix4X4().rotateY(Math.PI / 2);
		Vec3D p4 = rotY.transform(p);
		assertEquals(new Vec3D(-3, 2, 1), p4);
		
		Matrix4X4 rotZ = new Matrix4X4().rotateZ(Math.PI / 2);
		Vec3D p5 = rotZ.transform(p);
		assertEquals(new Vec3D(2, -1, 3), p5);
		
		Matrix4X4 scale = new Matrix4X4().scale(new Vec3D(2, 3, 4));
		Vec3D p6 = scale.transform(p);
		assertEquals(new Vec3D(2, 6, 12), p6);
		
		Matrix4X4 rotXYZ = new Matrix4X4().rotateX(Math.PI / 2).rotateY(Math.PI / 2).rotateZ(Math.PI / 2);
		Vec3D p7 = rotXYZ.transform(p);
		assertEquals(new Vec3D(3, -2, 1), p7);
		
		Matrix4X4 rotX2 = new Matrix4X4().mul(rotX);
		Vec3D p8 = rotX2.transform(p);
		assertEquals(new Vec3D(1, 3, -2), p8);
		
		Matrix4X4 rotY2 = new Matrix4X4().mul(rotY);
		Vec3D p9 = rotY2.transform(p);
		assertEquals(new Vec3D(-3, 2, 1), p9);
		
		Matrix4X4 rotZ2 = new Matrix4X4().mul(rotZ);
		Vec3D p10 = rotZ2.transform(p);
		assertEquals(new Vec3D(2, -1, 3), p10);
		
	}
	
	@Test
	void testMatrixInversion() {
		
		Vec3D p = new Vec3D(1, 2, 3);
		
		Matrix4X4 trans = new Matrix4X4().translate(new Vec3D(1, 2, 3));
		Vec3D p2 = trans.transform(p);
		assertEquals(new Vec3D(2, 4, 6), p2);
		
		trans = trans.invert();
		Vec3D p3 = trans.transform(p2);
		assertEquals(p, p3);
		
		Matrix4X4 rotX = new Matrix4X4().rotateX(Math.PI / 2);
		Vec3D p4 = rotX.transform(p);
		assertEquals(new Vec3D(1, 3, -2), p4);
		
		rotX = rotX.invert();
		Vec3D p5 = rotX.transform(p4);
		assertEquals(p, p5);
		
	}
	
}
