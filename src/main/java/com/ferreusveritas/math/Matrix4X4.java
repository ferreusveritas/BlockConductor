package com.ferreusveritas.math;

/**
 * A 4x4 Matrix
 */
public record Matrix4X4(
	double m00, double m01, double m02, double m03,
	double m10, double m11, double m12, double m13,
	double m20, double m21, double m22, double m23,
	double m30, double m31, double m32, double m33
) {
	
	public static final Matrix4X4 IDENTITY = new Matrix4X4();
	
	// Identity Matrix
	public Matrix4X4() {
		this(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);
	}
	
	public static Matrix4X4 translateMatrix(Vec3D vec) {
		return new Matrix4X4(
			1, 0, 0, vec.x(),
			0, 1, 0, vec.y(),
			0, 0, 1, vec.z(),
			0, 0, 0, 1
		);
	}
	
	public static Matrix4X4 scaleMatrix(Vec3D scale) {
		return new Matrix4X4(
			scale.x(), 0, 0, 0,
			0, scale.y(), 0, 0,
			0, 0, scale.z(), 0,
			0, 0, 0, 1
		);
	}
	
	public static Matrix4X4 rotateXMatrix(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Matrix4X4(
			1, 0, 0, 0,
			0, cos, sin, 0,
			0, -sin, cos, 0,
			0, 0, 0, 1
		);
	}
	
	public static Matrix4X4 rotateYMatrix(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Matrix4X4(
			cos, 0, -sin, 0,
			0, 1, 0, 0,
			sin, 0, cos, 0,
			0, 0, 0, 1
		);
	}
	
	public static Matrix4X4 rotateZMatrix(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Matrix4X4(
			cos, sin, 0, 0,
			-sin, cos, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		);
	}
	
	public Matrix4X4 transform(Matrix4X4 m) {
		return new Matrix4X4(
			m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30,
			m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31,
			m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32,
			m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33,
			m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30,
			m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31,
			m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32,
			m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33,
			m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30,
			m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31,
			m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32,
			m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33,
			m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30,
			m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31,
			m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32,
			m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33);
	}
	
	public Vec3D transform(Vec3D vec) {
		return new Vec3D(
			m00 * vec.x() + m01 * vec.y() + m02 * vec.z() + m03,
			m10 * vec.x() + m11 * vec.y() + m12 * vec.z() + m13,
			m20 * vec.x() + m21 * vec.y() + m22 * vec.z() + m23
		);
	}
	
	public Matrix4X4 translate(Vec3D vec) {
		return new Matrix4X4(
			m00, m01, m02, m03 + vec.x(),
			m10, m11, m12, m13 + vec.y(),
			m20, m21, m22, m23 + vec.z(),
			m30, m31, m32, m33
		);
	}
	
	public Matrix4X4 rotateX(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Matrix4X4(
			m00, m01, m02, m03,
			m10 * cos + m20 * sin, m11 * cos + m21 * sin, m12 * cos + m22 * sin, m13 * cos + m23 * sin,
			m20 * cos - m10 * sin, m21 * cos - m11 * sin, m22 * cos - m12 * sin, m23 * cos - m13 * sin,
			m30, m31, m32, m33
		);
	}
	
	public Matrix4X4 rotateY(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Matrix4X4(
			m00 * cos - m20 * sin, m01 * cos - m21 * sin, m02 * cos - m22 * sin, m03 * cos - m23 * sin,
			m10, m11, m12, m13,
			m20 * cos + m00 * sin, m21 * cos + m01 * sin, m22 * cos + m02 * sin, m23 * cos + m03 * sin,
			m30, m31, m32, m33
		);
	}
	
	public Matrix4X4 rotateZ(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Matrix4X4(
			m00 * cos + m10 * sin, m01 * cos + m11 * sin, m02 * cos + m12 * sin, m03 * cos + m13 * sin,
			m10 * cos - m00 * sin, m11 * cos - m01 * sin, m12 * cos - m02 * sin, m13 * cos - m03 * sin,
			m20, m21, m22, m23,
			m30, m31, m32, m33
		);
	}
	
	public Matrix4X4 scale(Vec3D vec) {
		return new Matrix4X4(
			m00 * vec.x(), m01 * vec.x(), m02 * vec.x(), m03 * vec.x(),
			m10 * vec.y(), m11 * vec.y(), m12 * vec.y(), m13 * vec.y(),
			m20 * vec.z(), m21 * vec.z(), m22 * vec.z(), m23 * vec.z(),
			m30, m31, m32, m33
		);
	}
	
}