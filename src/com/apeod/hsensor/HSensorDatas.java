package com.apeod.hsensor;

import java.io.Serializable;

public class HSensorDatas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 300234988954045534L;
	float temp;
	float presure;
	float humdity;

	public HSensorDatas(float temp, float presure, float humdity) {
		this.temp = temp;
		this.presure = presure;
		this.humdity = humdity;
	}

	public float getHumdity() {
		return humdity;
	}

	public float getTemp() {
		return temp;
	}

	public float getPresure() {
		return presure;
	}
}
