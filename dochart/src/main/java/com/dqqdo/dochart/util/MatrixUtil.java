package com.dqqdo.dochart.util;

import android.graphics.Matrix;

public class MatrixUtil {
	
	/**
	 * x方向的放大倍数
	 * @param Matrix
	 * @return MSCALE_X
	 */
	public static float getScale(Matrix m) {
		float[] values = new float[9];
		m.getValues(values);
		return values[Matrix.MSCALE_X];
	}
	
	/**
	 * x方向的位移
	 * @param Matrix
	 * @return MTRANS_X
	 */
	public static int getTransX(Matrix m) {
		float[] values = new float[9];
		m.getValues(values);
		return (int)values[Matrix.MTRANS_X];
	}
	
	/**
	 *y方向的位移
	 * @param Matrix
	 * @return MTRANS_Y
	 */
	public static int getTransY(Matrix m) {
		float[] values = new float[9];
		m.getValues(values);
		return (int)values[Matrix.MTRANS_Y];
	}
}
