package com.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author 钱洋
 * @date 2017年7月10日 上午11:32:15
 */
public class ActivityController extends BaseController{
	private static final String ACTIVITY = "activity";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ActivityController ac = new ActivityController();
		ac.activityInfoList();
	}

	public void activityInfoList(){
		ReturnResult result = new ReturnResult();
		String tag = getPara("tag");
		if (tag.equals("推荐")){
			List<Record> list = Db.find("Select * from activity order by likeNum desc");
			if(record != null){
				result.setSuccess(true);
				result.setInfo("成功返回活动信息列表");
				result.setData(list);
			}
			else{
				result.setSuccess(false);
				result.setInfo("未能找到符合条件的活动信息列表");
			}
		}
		else if (tag.equals("已参加")){
			List<Record> list = Db.find("Select * from activity order by likeNum asc");
			if(record != null){
				result.setSuccess(true);
				result.setInfo("成功返回活动信息列表");
				result.setData(list);
			}
			else{
				result.setSuccess(false);
				result.setInfo("未能找到符合条件的活动信息列表");
			}
		}
		else if (tag.equals("最新")){
			List<Record> list = Db.find("Select * from activity order by peopleNumber asc");
			if(record != null){
				result.setSuccess(true);
				result.setInfo("成功返回活动信息列表");
				result.setData(list);
			}
			else{
				result.setSuccess(false);
				result.setInfo("未能找到符合条件的活动信息列表");
			}
		}
		else{
			result.setSuccess(false);
			result.setInfo("未能找到符合条件的活动信息列表");
		}
		
		renderJson(result);
	}
	
}
