package com.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author 钱洋
 * @date 2017年7月11日 下午7:49:33
 */
public class InterestController extends BaseController{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

	public void personalInterestTag(){
		//String userid = getPara("userid");
		int userid = 1;
		ReturnResult result = new ReturnResult();
		List<Record> record = Db.find("Select * from interest_tag where userid = ?", userid);
		if(record != null){
			result.setSuccess(true);
			result.setInfo("成功返回个人兴趣列表");
			result.setData(record);
		}
		else{
			result.setSuccess(false);
			result.setInfo("未能找到符合条件的个人兴趣列表");
		}
		
		renderJson(result);
	}
}
