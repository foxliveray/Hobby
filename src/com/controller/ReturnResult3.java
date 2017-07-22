package com.controller;

/**
 * @author 钱洋
 * @date 2017年7月22日 上午12:30:49
 */
public class ReturnResult3 {
	private boolean success;
	private String info = "";
	private double info1 = 0.0;
	private double info2 = 0.0;
	private Object data = new Object();
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * @return the info1
	 */
	public double getInfo1() {
		return info1;
	}
	/**
	 * @param info1 the info1 to set
	 */
	public void setInfo1(double info1) {
		this.info1 = info1;
	}
	/**
	 * @return the info2
	 */
	public double getInfo2() {
		return info2;
	}
	/**
	 * @param info2 the info2 to set
	 */
	public void setInfo2(double info2) {
		this.info2 = info2;
	}
	/**
	 * @return the data1
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data1 the data1 to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
