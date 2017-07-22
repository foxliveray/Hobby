package com.controller;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.model.User;

/**
 * @author 钱洋
 * @date 2017年7月15日 下午1:26:05
 */
public class CommentController extends BaseController{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void showActivityComment(){
		ReturnResult2 result = new ReturnResult2();
		String actId = getPara("actId");
		List<Record> record = Db.find("select * from maincomment where actId = ?", actId);
		List<Record> record2 = Db.find("select * from subcomment where mainCommentId in (select id from maincomment where actId = ?)", actId);
		List<User> record3 = new ArrayList<User>();
		for (int i = 0;i<record.size();i++){
			User user = new User();
			Record r = Db.findFirst("select * from user where username = ?",record.get(i).getStr("userName"));
			user.setId(r.getInt("id"));
			user.setUsername(r.getStr("username"));
			user.setPassword(r.getStr("password"));
			user.setEmail(r.getStr("email"));
			user.setAddress(r.getStr("address"));
			user.setBirthday(r.getDate("birthday"));
			user.setSex(r.getStr("sex"));
			user.setTelephone(r.getStr("telephone"));
			user.setHead_img(r.getStr("head_img"));
			record3.add(user);
		}
		if(!record.isEmpty()&&!record3.isEmpty()){
			result.setSuccess(true);
			result.setInfo("成功查询该活动的所有评论和回复");
			result.setData1(record);
			result.setData2(record2);
			result.setData3(record3);
		}
		else{
			result.setSuccess(false);
			result.setInfo("查询该活动的所有评论和回复失败");
		}
		
		renderJson(result);
	}
	
	public void makeComment(){
		ReturnResult result = new ReturnResult();
		String tag = getPara("tag");
		String content = getPara("content");
		Date publishTime = new Date();
		//修改时间格式（String转数据库中的Datetime)
		//用时间戳格式来存储
		Timestamp timestamp = new Timestamp(publishTime.getTime());
		
		if(tag.equals("main")){
			String actId = getPara("actId");
			String userId = getPara("userId");
			String userName = getPara("userName");
			Record record = new Record();
			record.set("actId", actId);
			record.set("userName", userName);
			record.set("content", content);
			record.set("publishTime", timestamp);
			Db.save("maincomment", record);
			
			result.setSuccess(true);
			result.setInfo("评论成功");
			result.setData(record);
		}
		else if(tag.equals("sub")){
			String mainCommentId = getPara("mainCommentId");
			String lUserName = getPara("lUserName");
			String rUserName = getPara("rUserName");
			Record record = new Record();
			record.set("mainCommentId", mainCommentId);
			record.set("lUserName", lUserName);
			record.set("rUserName", rUserName);
			record.set("content", content);
			record.set("publishTime", timestamp);
			Db.save("subcomment", record);
			
			result.setSuccess(true);
			result.setInfo("回复成功");
			result.setData(record);
		}
		
		else{
			result.setSuccess(false);
			result.setInfo("评论或回复失败");
		}
		
		renderJson(result);
	}
}
