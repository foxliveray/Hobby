package com.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;


public class BaseController extends Controller{
	public Record record = new Record();
	public ReturnResult returnResult = new ReturnResult();
	/**
	 * 成功后返回Json
	 * @param info 提示信息
	 * @param data 返回数据
	 */
	public void successRender(String info,Object data) {
		returnResult = new ReturnResult();
		returnResult.setSuccess(true);
		returnResult.setData(data);
		returnResult.setInfo(info);
		renderJson(returnResult);
	}
	/**
	 * 失败后返回Json
	 * @param info 提示信息
	 * @param data 返回数据
	 */
	public void failRender(String info,Object data) {
		returnResult = new ReturnResult();
		returnResult.setSuccess(false);
		returnResult.setData(data);
		returnResult.setInfo(info);
		renderJson(returnResult);
	}
	
	public void renderResult(boolean flag,String info)
	{
		returnResult.setSuccess(flag);
		returnResult.setInfo(info);
		renderJson(returnResult);
	}
	

}
