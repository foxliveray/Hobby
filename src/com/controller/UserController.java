package com.controller;


import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.util.PasswordCode;

/**
 * @author 钱洋
 * @date 2017年7月6日 下午3:40:20
 */
public class UserController extends BaseController{
	private static final String USER= "user";
	private static final String RANDOM_CODE_KEY = "1";  
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public void hello(){
		renderText("hello");
	}
	
	public void reg()
	{
		//获取传递过来的用户名和密码
		String phone = getPara("phone");
		String pwd = getPara("pwd");
		String ChangePwd = PasswordCode.getMD5(pwd);
		
		ReturnResult result = new ReturnResult();
		
		//判断用户名是否已经存在
		Record record = Db.findFirst("select * from user where username=?",phone);
		if(record==null)
		{
			//保存用户信息
			record = new Record();
			record.set("username", phone);
			record.set("password", ChangePwd);
			//record.set("name", phone);
			Db.save(USER, record);
			
			result.setSuccess(true);
			result.setInfo("注册成功");
			result.setData(record);
		}
		else {
			result.setSuccess(false);
			result.setInfo("注册失败，用户名已经存在");
		}
		renderJson(result);	
	}
	
	public void login(){
		String username = getPara("phone");
		String password = getPara("pwd");
		String ChangePwd = PasswordCode.getMD5(password);
		ReturnResult result = new ReturnResult();
		
		//判断用户名和密码是否匹配
		Record record = Db.findFirst("select * from user where username = ? and password = ?", username,ChangePwd);
		if(record != null){
			result.setSuccess(true);
			result.setInfo("登陆成功");
			result.setData(record);
		}
		else{
			result.setSuccess(false);
			result.setInfo("登陆失败，用户名或密码错误");
		}
		
		renderJson(result);
	}
	
	//验证码图片生成
	public void check(){
		CaptchaRender img = new CaptchaRender(RANDOM_CODE_KEY);  
        render(img);  
	}
	
	public void showUserInfo(){
		
	}

}
