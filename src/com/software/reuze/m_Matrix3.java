package com.software.reuze;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//package com.badlogic.gdx.math;

import java.io.Serializable;
//import com.badlogic.gdx.utils.GdxRuntimeException;


/**
 * A 3x3 column major matrix for 2D transforms.
 * 
 * @author mzechner
 * 
 */
public class m_Matrix3 implements Serializable {	
	private static final long serialVersionUID = 7907569533774959788L;
	private final static float DEGREE_TO_RAD = (float)Math.PI / 180;
	public float[] vals = new float[9];

	public m_Matrix3 () {
		idt();
	}

	/**
	 * Sets this matrix to the identity matrix
	 * @return this matrix
	 */
	public m_Matrix3 idt () {
		this.vals[0] = 1;
		this.vals[1] = 0;
		this.vals[2] = 0;

		this.vals[3] = 0;
		this.vals[4] = 1;
		this.vals[5] = 0;

		this.vals[6] = 0;
		this.vals[7] = 0;
		this.vals[8] = 1;

		return this;
	}

	/**
	 * Multiplies this matrix with the other matrix in the order this * m.
	 * @return this matrix
	 */
	public m_Matrix3 mul (m_Matrix3 m) {
		float v00 = vals[0] * m.vals[0] + vals[3] * m.vals[1] + vals[6] * m.vals[2];
		float v01 = vals[0] * m.vals[3] + vals[3] * m.vals[4] + vals[6] * m.vals[5];
		float v02 = vals[0] * m.vals[6] + vals[3] * m.vals[7] + vals[6] * m.vals[8];

		float v10 = vals[1] * m.vals[0] + vals[4] * m.vals[1] + vals[7] * m.vals[2];
		float v11 = vals[1] * m.vals[3] + vals[4] * m.vals[4] + vals[7] * m.vals[5];
		float v12 = vals[1] * m.vals[6] + vals[4] * m.vals[7] + vals[7] * m.vals[8];

		float v20 = vals[2] * m.vals[0] + vals[5] * m.vals[1] + vals[8] * m.vals[2];
		float v21 = vals[2] * m.vals[3] + vals[5] * m.vals[4] + vals[8] * m.vals[5];
		float v22 = vals[2] * m.vals[6] + vals[5] * m.vals[7] + vals[8] * m.vals[8];

		vals[0] = v00;
		vals[1] = v10;
		vals[2] = v20;
		vals[3] = v01;
		vals[4] = v11;
		vals[5] = v21;
		vals[6] = v02;
		vals[7] = v12;
		vals[8] = v22;

		return this;
	}

	/**
	 * Sets this matrix to a rotation matrix that will rotate any vector in counter clockwise order around the z-axis.
	 * @param angle the angle in degrees.
	 * @return this matrix
	 */
	public m_Matrix3 setToRotation (float angle) {
		angle = DEGREE_TO_RAD * angle;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);

		this.vals[0] = cos;
		this.vals[1] = sin;
		this.vals[2] = 0;

		this.vals[3] = -sin;
		this.vals[4] = cos;
		this.vals[5] = 0;

		this.vals[6] = 0;
		this.vals[7] = 0;
		this.vals[8] = 1;

		return this;
	}

	/**
	 * Sets this matrix to a translation matrix.
	 * @param x the translation in x
	 * @param y the translation in y
	 * @return this matrix
	 */
	public m_Matrix3 setToTranslation (float x, float y) {
		this.vals[0] = 1;
		this.vals[1] = 0;
		this.vals[2] = 0;

		this.vals[3] = 0;
		this.vals[4] = 1;
		this.vals[5] = 0;

		this.vals[6] = x;
		this.vals[7] = y;
		this.vals[8] = 1;

		return this;
	}

	/**
	 * Sets this matrix to a scaling matrix
	 * 
	 * @param sx the scale in x
	 * @param sy the scale in y
	 * @return this matrix
	 */
	public m_Matrix3 setToScaling (float sx, float sy) {
		this.vals[0] = sx;
		this.vals[1] = 0;
		this.vals[2] = 0;

		this.vals[3] = 0;
		this.vals[4] = sy;
		this.vals[5] = 0;

		this.vals[6] = 0;
		this.vals[7] = 0;
		this.vals[8] = 1;

		return this;
	}

	public String toString () {
		return "[" + vals[0] + "|" + vals[3] + "|" + vals[6] + "]\n" + "[" + vals[1] + "|" + vals[4] + "|" + vals[7] + "]\n" + "["
			+ vals[2] + "|" + vals[5] + "|" + vals[8] + "]";
	}

	/**
	 * @return the determinant of this matrix
	 */
	public float det () {
		return vals[0] * vals[4] * vals[8] + vals[3] * vals[7] * vals[2] + vals[6] * vals[1] * vals[5] - vals[0] * vals[7]
			* vals[5] - vals[3] * vals[1] * vals[8] - vals[6] * vals[4] * vals[2];
	}

	/**
	 * Inverts this matrix given that the determinant is != 0
	 * @return this matrix
	 */
	public m_Matrix3 inv () throws Exception {
		float det = det();
		if (det == 0) throw new Exception("Can't invert a singular matrix");

		float inv_det = 1.0f / det;
		float tmp[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		tmp[0] = vals[4] * vals[8] - vals[5] * vals[7];
        tmp[1] = vals[2] * vals[7] - vals[1] * vals[8];
        tmp[2] = vals[1] * vals[5] - vals[2] * vals[4];
        tmp[3] = vals[5] * vals[6] - vals[3] * vals[8];
        tmp[4] = vals[0] * vals[8] - vals[2] * vals[6];
        tmp[5] = vals[2] * vals[3] - vals[0] * vals[5];
        tmp[6] = vals[3] * vals[7] - vals[4] * vals[6];
        tmp[7] = vals[1] * vals[6] - vals[0] * vals[7];
        tmp[8] = vals[0] * vals[4] - vals[1] * vals[3];
        
        vals[0] = inv_det * tmp[0];
        vals[1] = inv_det * tmp[1];
        vals[2] = inv_det * tmp[2];
        vals[3] = inv_det * tmp[3];
        vals[4] = inv_det * tmp[4];
        vals[5] = inv_det * tmp[5];
        vals[6] = inv_det * tmp[6];
        vals[7] = inv_det * tmp[7];
        vals[8] = inv_det * tmp[8];
                        
        return this;
	}

//	public static void main (String[] argv) {
//		float refX = 50, refY = -50;
//		float scaleX = 2, scaleY = 1;
//		float rotation = 45;
//		float x = -232, y = 123;
//
//		Matrix3 transform = new Matrix3();
//		Matrix3 tmp = new Matrix3();
//
//		tmp.vals = new float[] {-2, -1, 2, 2, 1, 0, -3, 3, -1};
//		System.out.println(tmp.det());
//
//		transform.idt();
//		transform.setToTranslation(-refX, -refY);
//		transform.mul(tmp.setToScaling(scaleX, scaleY));
//		transform.mul(tmp.setToRotation(rotation));
//		transform.mul(tmp.setToTranslation(refX, refY));
//		transform.mul(tmp.setToTranslation(x, y));
//		System.out.println(new Vector2().mul(transform));
//
//		Matrix4 transform4 = new Matrix4();
//		Matrix4 tmp4 = new Matrix4();
//		transform4.idt();
//		transform4.setToTranslation(-refX, -refY, 0);
//		transform4.mul(tmp4.setToScaling(scaleX, scaleY, 1));
//		transform4.mul(tmp4.setToRotation(new Vector3(0, 0, 1), rotation));
//		transform4.mul(tmp4.setToTranslation(refX, refY, 0));
//		transform4.mul(tmp4.setToTranslation(x, y, 0));
//		System.out.println(new Vector3().mul(transform4));
//	}

	public m_Matrix3 set (m_Matrix3 mat) {
		vals[0] = mat.vals[0];
		vals[1] = mat.vals[1];
		vals[2] = mat.vals[2];
		vals[3] = mat.vals[3];
		vals[4] = mat.vals[4];
		vals[5] = mat.vals[5];
		vals[6] = mat.vals[6];
		vals[7] = mat.vals[7];
		vals[8] = mat.vals[8];
		return this;
	}
	/**
	 * Sets the matrix to the given matrix as a float array. For fewer elements, last element is replicated.
	 * 
	 * @param values The matrix
	 * @return This matrix for chaining
	 */
	public m_Matrix3 set (float... values) {
		int n=values.length;
		for (int i=0; i<9; i++) {
			if (i>=n) vals[i]=values[n-1];
			else vals[i]=values[i];
		}
		return this;
	}
	public float[] getValues () {
		return vals;
	}
	/**
	 * Multiplies this vector by the given matrix
	 * @param mat the matrix
	 * @return this vector
	 */
	public ga_Vector2 mul (final ga_Vector2 v) {
		float x = v.x * this.vals[0] + v.y * this.vals[3] + this.vals[6];
		float y = v.x * this.vals[1] + v.y * this.vals[4] + this.vals[7];
		v.x = x;
		v.y = y;
		return v;
	}
}
