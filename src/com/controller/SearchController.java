package com.controller;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author 钱洋
 * @date 2017年7月20日 下午12:19:23
 */
public class SearchController extends BaseController{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void showHotTags(){
		String userId = getPara("userId");
		ReturnResult2 result = new ReturnResult2();
		List<Record> record = Db.find("select * from activity order by likeNum desc limit 10");
		List<Record> record2 = Db.find("select * from search_history where userId = ? order by id desc limit 10", userId);
		
		if(record != null){
			result.setSuccess(true);
			result.setInfo("成功返回热门活动标签和当前用户的历史记录");
			result.setData1(record);
			result.setData2(record2);
		}
		
		else{
			result.setSuccess(false);
			result.setInfo("返回热门活动标签和历史记录失败");
		}
		
		renderJson(result);
	}
	
	public void showHistory(){
		String userId = getPara("userId");
		ReturnResult result = new ReturnResult();
		List<Record> record = Db.find("select * from search_history where userId = ?", userId);
		
		if(record != null){
			result.setSuccess(true);
			result.setInfo("成功返回当前用户的历史记录");
			result.setData(record);
		}
		
		else{
			result.setSuccess(false);
			result.setInfo("返回当前用户的历史记录失败");
		}
		
		renderJson(result);
	}
	
	public void searchActivity(){
		boolean flag = false,flag2 = false;
		ReturnResult result = new ReturnResult();
		String tag = getPara("tag");
		String userId = getPara("userId");
		String content = getPara("content");
		if(tag.equals("活动")){
			List<Record> record = Db.find("select * from activity where actName like '%"+content+"%'");
			Record r1 = new Record();
			r1.set("search_type","活动");
			r1.set("content", content);
			r1.set("userId", userId);
			List<Record> record2 = Db.find("select * from search_history where content = ? and search_type = ? and userId = ?",content,"活动",userId);
			for (int i = 0;i<record2.size();i++){
				if(content.equals(record2.get(i).getStr("content"))){
					flag = true;
				}
			}
			if(flag == false){
				flag2 = Db.save("search_history", r1);
			}
			
			if((record != null && flag == true &&flag2 == false) || (record != null && flag == false &&flag2 == true)){
				result.setInfo("查询活动成功");
				result.setSuccess(true);
				result.setData(record);
			}
			else{
				result.setInfo("查询活动失败");
				result.setSuccess(false);
			}
		}
		else if(tag.equals("类别")){
			boolean flag3 = false,flag4 = false;
			List<Record> record1 = Db.find("select * from activity where teamId in (select id from team where variety like '%"+content+"%')");
			Record r1 = new Record();
			r1.set("search_type","类别");
			r1.set("content", content);
			r1.set("userId", userId);
			List<Record> record2 = Db.find("select * from search_history where content = ? and search_type = ? and userId = ?",content,"类别",userId);
			for (int i = 0;i<record2.size();i++){
				if(content.equals(record2.get(i).getStr("content"))){
					flag3 = true;
				}
			}
			
			if(flag3 == false){
				flag4 = Db.save("search_history", r1);
			}
			if((record1 != null && flag3 == true &&flag4 == false) || (record1 != null && flag3 == false &&flag4 == true)){
				result.setData(record1);
				result.setInfo("查询该类别活动成功");
				result.setSuccess(true);
			}
			else{
				result.setInfo("查询该类别活动失败");
				result.setSuccess(false);
			}
		}
		
		renderJson(result);
	}
	
	public void removeHistory(){
		ReturnResult result = new ReturnResult();
		String userId = getPara("userId");
		int flag = Db.update("delete from search_history where userId = ?", userId);
		
		if(flag == 1){
			result.setSuccess(true);
			result.setInfo("删除记录成功");
		}
		else{
			result.setSuccess(false);
			result.setInfo("删除记录失败");
		}
	}

}
