package com.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.model.Apply;
import com.util.DateUtil;
import com.util.FileUtil;
import com.util.LatitudeUtils;
import com.util.StrUtils;

/**
 * @author 钱洋
 * @date 2017年7月14日 下午3:50:09
 */
public class TeamController extends BaseController{
	private static final String TEAM = "team";
	private static final String ROOT_DIRECTORY = "/upload/team/";
	private static final String[] IMG_EXTENSIONS={"jpg","png","jpeg","gif","bmp"};
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void showTeamInfo(){
		String teamId = getPara("teamId");
		String userId = getPara("userId");
		int uuid = Integer.parseInt(userId);
		ReturnResult3 result = new ReturnResult3();
		Record record = Db.findFirst("Select * from team where id = ?", teamId);
		double[] lat = new double[2];
		lat = LatitudeUtils.getLat(record.getStr("teamAddress"));
		
		if(record != null){
			result.setSuccess(true);
			if(record.getInt("principalId")==uuid){
				result.setInfo("队长");
				result.setInfo1(lat[0]);
				result.setInfo2(lat[1]);
			}
			else{
				result.setInfo("队员");
				result.setInfo1(lat[0]);
				result.setInfo2(lat[1]);
			}
			result.setData(record);
		}
		else{
			result.setSuccess(false);
			result.setInfo("查询小队详细信息失败");
		}
		
		renderJson(result);
	}
	
	//只有组员可以退出小队，退出小队的同时退出所有活动
	public void quitTeam(){
		String teamId = getPara("teamId");
		String userId = getPara("userId");
		int uuid = Integer.parseInt(userId);
		ReturnResult result = new ReturnResult();
		int flag1 = Db.update("delete from team_user where teamId = ? and userId = ?", teamId,userId);
		List<Record> record = Db.find("select actId,userId from activity t1 inner join activity_user t2 on t1.id = t2.actId where teamId = ? and userId = ?", teamId,userId);
		int flag2 = 0;
		for (int i=0;i<record.size();i++){
			int actId = record.get(i).getInt("actId");
			int yonghuId = record.get(i).getInt("userId");
			int flag3 = Db.update("delete from activity_user where actId = ? and userId = ?", actId,yonghuId);
			if(flag3 == 1){
				flag2++;
			}
		}
		int flag3 = Db.update("update team set peopleNumber = peopleNumber - 1 where id = ?", teamId);
		if(flag1 == 1 && flag2 == record.size()&& flag3 == 1){
			result.setSuccess(true);
			result.setInfo("成功退出小队及所有活动");
		}else{
			result.setSuccess(false);
			result.setInfo("退出小队及所有活动失败");
		}
		renderJson(result);
	}
	
	public void showApplyList(){
		String teamId = getPara("teamId");
		String userId = getPara("userId");
		ReturnResult result = new ReturnResult();
		//申请加入活动的列表
		List<Record> record1 = Db.find("select userId,actId,username,actName from apply t1 inner join user t2 on t1.userId = t2.id inner join activity t3 on t1.actId = t3.id where t1.teamId = ?", teamId);
		//申请发布活动的列表
		List<Record> record2 = Db.find("select t1.id,t1.builderId,actName,username from activity t1 inner join user t2 on t1.builderId = t2.id where teamId = ? and status = ?", teamId,"申请中");
		List<Apply> apply = new ArrayList<Apply>();
		for (int i = 0 ;i<record1.size();i++){
			Apply oneRec = new Apply();
			oneRec.setApplyUserId(record1.get(i).getInt("userId"));
			oneRec.setUserName(record1.get(i).getStr("username"));
			oneRec.setActId(record1.get(i).getInt("actId"));
			oneRec.setActName(record1.get(i).getStr("actName"));
			oneRec.setVariety("申请加入活动");
			apply.add(oneRec);
		}
		
		for (int i = 0 ;i<record2.size();i++){
			Apply oneRec = new Apply();
			oneRec.setApplyUserId(record2.get(i).getInt("builderId"));
			oneRec.setUserName(record2.get(i).getStr("username"));
			oneRec.setActId(record2.get(i).getInt("id"));
			oneRec.setActName(record2.get(i).getStr("actName"));
			oneRec.setVariety("申请发布活动");
			apply.add(oneRec);
		}
		
		if(record1.isEmpty()&&record2.isEmpty()){
			result.setSuccess(false);
			result.setInfo("两种申请列表都为空");
		}
		else{
			result.setData(apply);
			result.setSuccess(true);
			result.setInfo("查找两种申请列表成功");
		}
		
		renderJson(result);
	}
	
	//同意两种申请
	public void approveApply(){
		ReturnResult result = new ReturnResult();
		String userId = getPara("userId");
		String applyUserId = getPara("applyUserId");
		String actId = getPara("actId");
		String Tag = getPara("tag");
		
		if(Tag.equals("申请加入活动")){
			Record record = Db.findFirst("select teamId from apply where userId = ? and actId = ?", applyUserId,actId);
			int teamId = record.getInt("teamId");
			int flag1 = Db.update("delete from apply where userId = ? and actId = ?", applyUserId,actId);
			Record r1 = new Record();
			r1.set("actId", actId);
			r1.set("userId", applyUserId);
			boolean flag2 = Db.save("activity_user", r1);
			int flag4 = Db.update("update activity set peopleNumber = peopleNumber + 1 where id = ?", actId);
			Record r2 = new Record();
			r2.set("teamId", teamId);
			r2.set("userId", applyUserId);
			boolean flag3 = Db.save("team_user", r2);
			int flag5 = Db.update("update team set peopleNumber = peopleNumber + 1 where id = ?", teamId);
			if(flag1 == 1 && flag2 == true && flag3 == true && flag4 == 1 && flag5 == 1){
				result.setSuccess(true);
				result.setInfo("通过了加入活动的申请");
			}
			else{
				result.setSuccess(false);
				result.setInfo("申请加入活动失败");
			}
		}
		else if(Tag.equals("申请发布活动")){
			Record record = Db.findFirst("select teamId from activity where builderId = ? and id = ?", applyUserId,actId);
			int teamId = record.getInt("teamId");
			int flag1 = Db.update("update activity set status = ? where id = ? and builderId = ?", "已发布",actId,applyUserId);
			Record r1 = new Record();
			r1.set("actId", actId);
			r1.set("userId", applyUserId);
			boolean flag2 = Db.save("activity_user", r1);
			int flag3 = Db.update("update activity set peopleNumber = peopleNumber + 1 where id = ?", actId);
			int flag4 = Db.update("update team set actNumber = actNumber + 1 where id = ?", teamId);
			
			if(flag1 == 1 && flag2 == true && flag3 == 1){
				result.setSuccess(true);
				result.setInfo("通过了发布活动的申请");
			}
			else{
				result.setSuccess(false);
				result.setInfo("申请发布活动失败");
			}
		}
		
		renderJson(result);
	}

	public void createTeam(){
		ReturnResult result = new ReturnResult();
		UploadFile uploadFile = null;
		UploadFile uploadFile2 = null;
		String savePath = null;
		String savePath2 = null;
		try {
			System.out.println("PathKit.getWebRootPath():"+PathKit.getWebRootPath());
			uploadFile = getFile("file1");
			
			if(uploadFile!=null)
			{
				String filename=uploadFile.getFileName();//文件全名
				System.out.println("filename:"+filename);
				int one = filename.lastIndexOf(".");
				String houzhui = filename.substring((one+1),filename.length());//文件后缀名
				savePath = ROOT_DIRECTORY+"head_img"+DateUtil.format(new Date(), "yyyyMMddHHmmss")+ "."+houzhui;
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
				
				uploadFile2 = getFile("file2");
				if(uploadFile2!=null){
					String filename2=uploadFile2.getFileName();//文件全名
					System.out.println("filename:"+filename2);
					int two = filename2.lastIndexOf(".");
					String houzhui2 = filename2.substring((two+1),filename2.length());//文件后缀名
					savePath2 = ROOT_DIRECTORY+"des_img"+DateUtil.format(new Date(), "yyyyMMddHHmmss")+ "."+houzhui2;
					System.out.println("savepath2:"+savePath2);
					/*if(uploadFile.getFile().length()/1024 > 1024){
						uploadFile.getFile().delete();
						renderResult(false, "图片大小请小于1M");	
						return;
					}*/
					if(!StrUtils.haveStr(FileUtil.getFileExtension(uploadFile2.getFileName()), IMG_EXTENSIONS)){
						uploadFile2.getFile().delete();
						renderResult(false, "文件格式错误");		
						return;
					}
						
					//保存图片到服务器上
					uploadFile2.getFile().renameTo(new File(PathKit.getWebRootPath()+savePath2));//移动图片
				}
						
				String teamName = getPara("teamName");
				String principalId = getPara("principalId");
				String variety = getPara("variety");
				String teamDes = getPara("teamDes");	
				String teamAddress = getPara("teamAddress");
				
				Record record = new Record();
				record.set("teamName", teamName);
				record.set("principalId", principalId);
				record.set("variety", variety);
				record.set("teamDes", teamDes);
				record.set("headImage",savePath);
				record.set("desImage", savePath2);
				record.set("teamAddress", teamAddress);
				record.set("peopleNumber", 1);
				
				Db.save(TEAM, record);
				result.setSuccess(true);
				result.setInfo("成功");
				renderJson(result);	
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
	}
	
	public void showAllMembers(){
		ReturnResult result = new ReturnResult();
		String teamId = getPara("teamId");
		String userId = getPara("userId");
		
		List<Record> record = Db.find("select * from user where id in (select userId from team_user where teamId = ?)", teamId);
		Record record2 = Db.findFirst("select principalId from team where id = ?", teamId);
		
		if(!record.isEmpty()&&record2!=null){
			result.setSuccess(true);
			result.setInfo(String.valueOf(record2.getInt("principalId")));
			result.setData(record);
		}
		else{
			result.setSuccess(false);
			result.setData("没有找到对应的小队成员列表和队长信息");
		}
		
		renderJson(result);
	}
	
	public void showAllActivity(){
		ReturnResult result = new ReturnResult();
		String teamId = getPara("teamId");
		String userId = getPara("userId");
		
		List<Record> record = Db.find("select * from activity where teamId = ?", teamId);
		
		if(!record.isEmpty()){
			result.setSuccess(true);
			result.setInfo("成功返回小队所有活动信息");
			result.setData(record);
		}
		else{
			result.setSuccess(false);
			result.setData("没有找到对应的小队活动信息");
		}
		renderJson(result);
	}
}
