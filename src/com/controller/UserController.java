package com.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.model.LeanCloudUser;
import com.util.DateUtil;
import com.util.PasswordCode;

/**
 * @author 钱洋
 * @date 2017年7月6日 下午3:40:20
 */
public class UserController extends BaseController {
	private static final String USER = "user";
	private static final String ROOT_DIRECTORY = "/upload/user/";
	private static final String RANDOM_CODE_KEY = "1";

	public static void main(String[] args) {
		UserController uc = new UserController();
		uc.modifyTime();

	}

	public void hello() {
		renderText("hello");
	}

	public void reg() {
		// 获取传递过来的用户名和密码
		String phone = getPara("phone");
		String pwd = getPara("pwd");
		String ChangePwd = PasswordCode.getMD5(pwd);

		ReturnResult result = new ReturnResult();

		// 判断用户名是否已经存在
		Record record = Db.findFirst("select * from user where username=?", phone);
		if (record == null) {
			// 保存用户信息
			record = new Record();
			record.set("username", phone);
			record.set("password", ChangePwd);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			record.set("birthday",timestamp);
			record.set("sex", "男");
			// record.set("name", phone);
			Db.save(USER, record);

			result.setSuccess(true);
			result.setInfo("注册成功");
			result.setData(record);
		} else {
			result.setSuccess(false);
			result.setInfo("注册失败，用户名已经存在");
		}
		renderJson(result);
	}

	public void login() {
		String username = getPara("phone");
		String password = getPara("pwd");
		String ChangePwd = PasswordCode.getMD5(password);
		ReturnResult result = new ReturnResult();

		// 判断用户名和密码是否匹配
		Record record = Db.findFirst("select * from user where username = ? and password = ?", username, ChangePwd);
		if (record != null) {
			result.setSuccess(true);
			result.setInfo("登陆成功");
			result.setData(record);
		} else {
			result.setSuccess(false);
			result.setInfo("登陆失败，用户名或密码错误");
		}

		renderJson(result);
	}

	// 验证码图片生成
	public void check() {
		CaptchaRender img = new CaptchaRender(RANDOM_CODE_KEY);
		render(img);
	}

	// 根据传过来的userinfoTag确定在个人信息界面展示的内容
	public void showUserInfo() {
		String userid = getPara("userid");
		String tag = getPara("userinfoTag");
		ReturnResult result = new ReturnResult();
		if (tag.equals("tab1")) {
			Record record = Db.findFirst("select * from user where id = ?", userid);
			if (record != null) {
				result.setSuccess(true);
				result.setInfo("查询个人详细信息成功");
				result.setData(record);
			} else {
				result.setSuccess(false);
				result.setInfo("查询个人详细信息失败");
			}
		} else if (tag.equals("tab2")) {
			List<Record> record = Db.find("Select * from interest_tag where userid = ?", userid);
			if (record != null) {
				result.setSuccess(true);
				result.setInfo("成功返回个人兴趣列表");
				result.setData(record);
			} else {
				result.setSuccess(false);
				result.setInfo("未能找到符合条件的个人兴趣列表");
			}
		} else if (tag.equals("tab3")) {
			List<Record> record = Db.find("Select * from activity where builderId = ? order by startTime", userid);
			if (record != null) {
				result.setSuccess(true);
				result.setInfo("成功返回活动信息列表");
				result.setData(record);
			} else {
				result.setSuccess(false);
				result.setInfo("未能找到符合条件的活动信息列表");
			}
		} else if (tag.equals("tab4")) {
			List<Record> record = Db.find(
					"Select * from activity_user t1 inner join activity t2 on t1.actId = t2.id where userId = ?",
					userid);
			if (record != null) {
				result.setSuccess(true);
				result.setInfo("成功返回活动信息列表");
				result.setData(record);
			} else {
				result.setSuccess(false);
				result.setInfo("未能找到符合条件的活动信息列表");
			}
		}

		renderJson(result);
	}

	public void getUserinfo() {
		// 获取传递过来的用户名和密码
		String userid = getPara("userid");
		ReturnResult result = new ReturnResult();
		Record record = Db.findFirst("select * from user where id=?", userid);
		if (record != null) {
			result.setSuccess(true);
			result.setData(record);
			result.setInfo("成功加载用户详细信息");
		} else {
			result.setSuccess(false);
			result.setInfo("未找到用户信息");
		}
		renderJson(result);
	}

	public void updateUserHead() {
		String savePath = "";
//		String tag = getPara("tag");
		UploadFile uploadFile = null;
//		if (tag.equals("有图")) {
//			try {
//				System.out.println("PathKit.getWebRootPath():" + PathKit.getWebRootPath());
//				// file可以是输入流inputStream类型也可以是文件对象
//				uploadFile = getFile("file");
//				if (uploadFile != null) {
//					// 文件全名
//					String filename = uploadFile.getFileName();
//					System.out.println("original_filename:" + filename);
//					int one = filename.lastIndexOf(".");
//					// 文件后缀名
//					String houzhui = "png";
//					if (!filename.equals("nofilename")) {
//						filename.substring((one + 1), filename.length());
//					}
//
//					savePath = ROOT_DIRECTORY + DateUtil.format(new Date(), "yyyyMMddHHmmss") + "." + houzhui;
//					System.out.println("savepath:" + savePath);
//					if (uploadFile.getFile().length() / 1024 > 4096) {
//						uploadFile.getFile().delete();
//						System.out.println("图片大小请小于4M");
//						renderResult(false, "图片大小请小于4M");
//						return;
//					}
//					// 保存图片到服务器上
//					uploadFile.getFile().renameTo(new File(PathKit.getWebRootPath() + savePath));
//
//					// 修改时间格式（String转数据库中的Datetime)
//					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					ParsePosition pos = new ParsePosition(0);
//					Date strtodate = formatter.parse(birth, pos);
//					// 用时间戳格式来存储
//					Timestamp timestamp = new Timestamp(strtodate.getTime());
//
//					record.set("head_img", savePath);
//					String sql = "update user set username=?,address=?,head_img=?,sex=?,birthday=?,telephone=?,email=? where id=?";
//					Db.update(sql, name, addr, savePath, sex, timestamp, phone, email, userId);
//					returnResult.setSuccess(true);
//					returnResult.setInfo("成功(已修改头像)");
//				} else {
//					returnResult.setSuccess(false);
//					returnResult.setInfo("uploadFile为空，失败");
//				}
//			} catch (Exception e) {
//				returnResult.setSuccess(false);
//				returnResult.setInfo("异常");
//				e.printStackTrace();
//			}
//		} else if (tag.equals("无图")) {
//			// 修改时间格式（String转数据库中的Datetime)
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			ParsePosition pos = new ParsePosition(0);
//			Date strtodate = formatter.parse(birth, pos);
//			// 用时间戳格式来存储
//			Timestamp timestamp = new Timestamp(strtodate.getTime());
//
//			record.set("head_img", savePath);
//			String sql = "update user set username=?,address=?,sex=?,birthday=?,telephone=?,email=? where id=?";
//			Db.update(sql, name, addr, sex, timestamp, phone, email, userId);
//			returnResult.setSuccess(true);
//			returnResult.setInfo("成功(未修改头像)");
//		}
//		renderJson(returnResult);
		
		
		try {
			System.out.println("PathKit.getWebRootPath():" + PathKit.getWebRootPath());
			// file可以是输入流inputStream类型也可以是文件对象
			uploadFile = getFile("file");
			if (uploadFile != null) {
				// 文件全名
				String filename = uploadFile.getFileName();
				System.out.println("original_filename:" + filename);
				int one = filename.lastIndexOf(".");
				// 文件后缀名
				String houzhui = "png";
				if (!filename.equals("nofilename")) {
					filename.substring((one + 1), filename.length());
				}

				savePath = ROOT_DIRECTORY + DateUtil.format(new Date(), "yyyyMMddHHmmss") + "." + houzhui;
				System.out.println("savepath:" + savePath);
				if (uploadFile.getFile().length() / 1024 > 4096) {
					uploadFile.getFile().delete();
					System.out.println("图片大小请小于4M");
					renderResult(false, "图片大小请小于4M");
					return;
				}
				// 保存图片到服务器上
				uploadFile.getFile().renameTo(new File(PathKit.getWebRootPath() + savePath));
				
				String userId = getPara("userid");
				String name = getPara("name");
				String addr = getPara("addr");
				String birth = getPara("birth");
				String sex = getPara("sex");
				String phone = getPara("phone");
				String email = getPara("email");

				// 修改时间格式（String转数据库中的Datetime)
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				ParsePosition pos = new ParsePosition(0);
				Date strtodate = formatter.parse(birth, pos);

				// 用时间戳格式来存储
				Timestamp timestamp = new Timestamp(strtodate.getTime());

				record.set("head_img", savePath);
				String sql = "update user set username=?,address=?,head_img=?,sex=?,birthday=?,telephone=?,email=? where id=?";
				Db.update(sql, name, addr, savePath, sex, timestamp, phone, email, userId);
				returnResult.setSuccess(true);
				returnResult.setInfo("成功(已修改头像)");
			} else {
				returnResult.setSuccess(false);
				returnResult.setInfo("uploadFile为空，失败");
			}
		} catch (Exception e) {
			returnResult.setSuccess(false);
			returnResult.setInfo("异常");
			e.printStackTrace();
		}
		
		renderJson(returnResult);
	}


	// 测试修改时间用的
	public void modifyTime() {
		String userId = getPara("userid");
		String birth = getPara("birth");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(birth, pos);

		Timestamp timestamp = new Timestamp(strtodate.getTime());
		String sql = "update user set birthday=? where id=?";
		Db.update(sql, timestamp, userId);
		returnResult.setSuccess(true);
		returnResult.setInfo("成功");
		renderJson(returnResult);
	}
	
	public void updateUserInfo(){
		
		String userId = getPara("userid");
		String name = getPara("name");
		String addr = getPara("addr");
		String birth = getPara("birth");
		String sex = getPara("sex");
		String phone = getPara("phone");
		String email = getPara("email");
		
		 //修改时间格式（String转数据库中的Datetime)
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(birth, pos);
		// 用时间戳格式来存储
		Timestamp timestamp = new Timestamp(strtodate.getTime());
		
		String sql = "update user set username=?,address=?,sex=?,birthday=?,telephone=?,email=? where id=?";
		int flag = Db.update(sql, name, addr, sex, timestamp, phone, email, userId);

		if(flag == 1){
			returnResult.setSuccess(true);
			returnResult.setInfo("成功修改个人信息(未修改头像)");
		}
		else{
			returnResult.setSuccess(false);
			returnResult.setInfo("未成功修改个人信息");
		}
	}
	
	public void searchPhoneNumber(){
		ReturnResult result = new ReturnResult();
		String username = getPara("userName");
		Record record = Db.findFirst("select telephone from user where username = ?",username);
		
		if(record != null){
			result.setSuccess(true);
			result.setInfo("成功返回用户手机号");
			result.setData(record);
		}
		
		else{
			result.setSuccess(false);
			result.setInfo("没有查询到对应的手机号");
		}
		
		renderJson(result);
	}
		
	public void modifyPassword(){
		ReturnResult result = new ReturnResult();
		String username = getPara("userName");
		String password = getPara("password");
		String ChangePwd = PasswordCode.getMD5(password);
		
		int flag = Db.update("update user set password = ? where username = ?", ChangePwd,username);
		
		if (flag == 1){
			result.setSuccess(true);
			result.setInfo("修改密码成功");
		}
		
		else {
			result.setSuccess(false);
			result.setInfo("修改密码失败");
		}
		
		renderJson(result);
	}
	
	public void userTeamList(){
		ReturnResult result = new ReturnResult();
		String userId = getPara("userId");
		List<Record> record = Db.find("select * from team where id in (select teamId from team_user where userId = ?)", userId);
		
		if(record != null){
			result.setSuccess(true);
			result.setInfo("成功返回用户参与的所有小队");
			result.setData(record);
		}
		else{
			result.setSuccess(false);
			result.setInfo("返回用户参与的所有小队失败");
		}
		
		renderJson(result);
	}
	
	public void getAllUser(){
		ReturnResult result = new ReturnResult();
		List<Record> record = Db.find("select id,username,head_img from user");
		
		List<LeanCloudUser> userList = new ArrayList<LeanCloudUser>();
		for(int i=0;i<record.size();i++){
			LeanCloudUser user = new LeanCloudUser();
			user.setUserId(String.valueOf(record.get(i).getInt("id")));
			user.setName(record.get(i).getStr("username"));
			user.setAvatarUrl(record.get(i).getStr("head_img"));
			userList.add(user);
		}
		if(record != null){
			result.setSuccess(true);
			result.setInfo("成功返回所有用户信息");
			result.setData(userList);
		}
		else{
			result.setSuccess(false);
			result.setInfo("返回所有用户信息失败");
		}
		
		renderJson(result);
	}
}
