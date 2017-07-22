package com.controller;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.util.DateUtil;
import com.util.FileUtil;
import com.util.StrUtils;

/**
 * @author 钱洋
 * @date 2017年7月10日 上午11:32:15
 */
public class ActivityController extends BaseController{
	private static final String ACTIVITY = "activity";
	private static final String ROOT_DIRECTORY = "/upload/activity/";
	private static final String[] IMG_EXTENSIONS={"jpg","png","jpeg","gif","bmp"};

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ActivityController ac = new ActivityController();
		ac.activityInfoList();
	}

	public void activityInfoList(){
		String userid = getPara("userid");
		System.out.println(userid);
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
			String userId = getPara("userId");
			List<Record> list = Db.find("select * from activity where id in(select actId from activity_user where userId = ?)",userId);
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
			List<Record> list = Db.find("Select * from activity order by startTime desc");
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
	
	public void createActivity(){
		String savePath = "";
		ReturnResult result = new ReturnResult();
		UploadFile uploadFile = null;		
		try {
			System.out.println("PathKit.getWebRootPath():"+PathKit.getWebRootPath());
			uploadFile = getFile("desImage");
			if(uploadFile!=null)
			{
				String filename=uploadFile.getFileName();//文件全名
				System.out.println("filename:"+filename);
				int one = filename.lastIndexOf(".");
				String houzhui = filename.substring((one+1),filename.length());//文件后缀名
				//String houzhui = "png";
				savePath = ROOT_DIRECTORY+DateUtil.format(new Date(), "yyyyMMddHHmmss")+ "."+houzhui;
				System.out.println("savepath:"+savePath);
				/*if(uploadFile.getFile().length()/1024 > 1024){
					uploadFile.getFile().delete();
					renderResult(false, "图片大小请小于1M");	
					return;
				}*/
				if(!StrUtils.haveStr(FileUtil.getFileExtension(uploadFile.getFileName()), IMG_EXTENSIONS)){
					uploadFile.getFile().delete();
					renderResult(false, "文件格式错误");		
					return;
				}
				//保存图片到服务器上
				uploadFile.getFile().renameTo(new File(PathKit.getWebRootPath()+savePath));//移动图片
				String tag = getPara("tag");
				String actName = getPara("actName");
				String userid = getPara("userId");
				String startTime = getPara("startTime");
				String endTime = getPara("endTime");
				String description = getPara("description");
				String address = getPara("address");
				String teamId = getPara("teamId");
				
				Record record = new Record();
				record.set("actName", actName);
				record.set("builderId", userid);
				record.set("startTime", startTime);
				record.set("endTime", endTime);
				record.set("description", description);
				record.set("address", address);
				record.set("teamId", teamId);
				record.set("desImage", savePath);
				if(tag.equals("发布活动")){
					record.set("status", "已发布");
					//record.set("desImage",savePath);
					
					Db.save(ACTIVITY, record);
					Record record2 = Db.findFirst("select id from activity where actName = ?", actName);
					Record record3 = new Record();
					record3.set("actId", record2.getInt("id"));
					record3.set("userId", userid);
					Db.save("activity_user", record3);
					int flag4 = Db.update("update team set actNumber = actNumber + 1 where id = ?", teamId);
					result.setSuccess(true);
					result.setInfo("发布活动成功");
				}
				else if(tag.equals("申请活动")){
					record.set("status", "申请中");
					//record.set("desImage",savePath);
					
					Db.save(ACTIVITY, record);
					result.setSuccess(true);
					result.setInfo("申请活动成功");
				}
			}
			else {
				result.setSuccess(false);
				result.setInfo("uploadFile为空，失败");
				renderJson(result);	
			}			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setInfo("异常");
			renderJson(result);	
			e.printStackTrace();
		}
		
		renderJson(result);
	}
	
	public void showDetailedActivity(){
		String actId = getPara("actId");
		String userId = getPara("userId");
		ReturnResult result = new ReturnResult();
//		Record record1 = Db.findFirst("select t1.teamId,userId from activity t1 inner join team_user t2 on t1.teamId = t2.teamId where t1.id = ? and t2.userId = ?", actId,userId);
//		if(record1 != null){
//			//表明该用户已经加入了小队
//			Record record2 = Db.findFirst("select t1.builderId,t1.teamId,t1.actName,t1.address,t1.description,t1.desImage,t1.startTime,t1.endTime,t1.peopleNumber,t2.username,t3.teamName from activity t1 inner join user t2 on t1.builderId = t2.id inner join team t3 on t1.teamId = t3.id where t1.id = ?" , actId);
//			result.setSuccess(true);
//			result.setInfo("是小队成员");
//			result.setData(record2);
//		}
//		else if(record1 == null){
//			Record record2 = Db.findFirst("select t1.builderId,t1.teamId,t1.actName,t1.address,t1.description,t1.desImage,t1.startTime,t1.endTime,t1.peopleNumber,t2.username,t3.teamName from activity t1 inner join user t2 on t1.builderId = t2.id inner join team t3 on t1.teamId = t3.id where t1.id = ?" , actId);
//			result.setSuccess(false);
//			result.setInfo("不是小队成员");
//			result.setData(record2);
//		}
		
		Record record1 = Db.findFirst("select * from activity_user where actId = ? and userId = ?", actId,userId);
		if(record1 != null){
			//表明该用户已经加入该活动
			Record record2 = Db.findFirst("select t1.builderId,t1.teamId,t1.actName,t1.address,t1.description,t1.desImage,t1.startTime,t1.endTime,t1.peopleNumber,t2.username,t3.teamName,t3.headImage from activity t1 inner join user t2 on t1.builderId = t2.id inner join team t3 on t1.teamId = t3.id where t1.id = ?" , actId);
			if(record2 != null){
				result.setSuccess(true);
				result.setInfo("已参加该活动");
				result.setData(record2);
			}
			else{
				result.setSuccess(false);
				result.setInfo("查询活动详细信息失败");
			}
		}
		else if(record1 == null){
			Record record2 = Db.findFirst("select t1.builderId,t1.teamId,t1.actName,t1.address,t1.description,t1.desImage,t1.startTime,t1.endTime,t1.peopleNumber,t2.username,t3.teamName from activity t1 inner join user t2 on t1.builderId = t2.id inner join team t3 on t1.teamId = t3.id where t1.id = ?" , actId);
			Record record3 = Db.findFirst("select * from apply where actId = ? and userId = ?", actId,userId);
			if(record2 != null&&record3 == null){
				result.setSuccess(true);
				result.setInfo("未参加该活动");
				result.setData(record2);
			}
			else if(record2 != null&&record3 != null){
				result.setSuccess(true);
				result.setInfo("等待申请中");
				result.setData(record2);
			}
			else{
				result.setSuccess(false);
				result.setInfo("查询活动详细信息失败");
			}
		}
		
		renderJson(result);
	}
	
	public void attendActivity(){
		String actId = getPara("actId");
		String userId = getPara("userId");
		
		ReturnResult result = new ReturnResult();
		
		Record record1 = Db.findFirst("select t1.teamId,userId from activity t1 inner join team_user t2 on t1.teamId = t2.teamId where t1.id = ? and t2.userId = ?", actId,userId);
		if(record1 != null){
			//表明该用户已经加入了小队
			Record r1 = new Record();
			r1.set("actId", actId);
			r1.set("userId", userId);
			result.setSuccess(true);
			result.setInfo("是小队成员");
			boolean flag = Db.save("activity_user", r1);
			int flag2 = Db.update("update activity set peopleNumber = peopleNumber + 1 where id = ?", actId);
			if(flag == true && flag2 == 1){
				result.setSuccess(true);
				result.setInfo("成功参与活动");
			}
			else{
				result.setSuccess(false);
				result.setInfo("参与活动失败");
			}
		}
		else if(record1 == null){
			Record r2 = new Record();
			Record r3 = Db.findFirst("select teamId from activity where id = ?", actId);
			r2.set("teamId", r3.getInt("teamId"));
			r2.set("userId", userId);
			r2.set("actId", actId);
			Date date = new Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			r2.set("applyTime", timestamp);
			boolean flag2 = Db.save("apply", r2);
			if(flag2 == true){
				result.setSuccess(true);
				result.setInfo("成功发送参与活动申请");
			}
			else{
				result.setSuccess(false);
				result.setInfo("申请参与活动失败");
			}
		}
		
		renderJson(result);
	}
	
	public void setLikeNum(){
		String actId = getPara("actId");
		String like = getPara("like");
		int likeNum = Integer.parseInt(like);
		ReturnResult result = new ReturnResult();
		Record record = Db.findFirst("select likeNum from activity where id = ?", actId);
		int flag = Db.update("update activity set likeNum = ? where id = ?",likeNum,actId);
		
		if (flag == 1){
			result.setSuccess(true);
			if(record.getInt("likeNum")>likeNum){
				result.setInfo("取消赞成功");
			}
			else{
				result.setInfo("点赞成功");
			}
		}
		
		else{
			result.setSuccess(false);
			result.setInfo("点赞或取消赞失败");
		}
		
		renderJson(result);
	}
	
	public void quitActivity(){
		String actId = getPara("actId");
		String userId = getPara("userId");

		ReturnResult result = new ReturnResult();
		int flag = Db.update("delete from activity_user where actId = ? and userId = ?", actId,userId);
		int flag2 = Db.update("update activity set peopleNumber = peopleNumber-1 where id = ?", actId);
		
		if(flag == 1 && flag2 == 1){
			result.setSuccess(true);
			result.setInfo("成功退出活动");
		}
		else{
			result.setSuccess(false);
			result.setInfo("退出活动失败");
		}
	
		renderJson(result);
	}
	
}
