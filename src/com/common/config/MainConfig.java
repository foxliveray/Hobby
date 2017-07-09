package com.common.config;

import com.controller.UserController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

/**
 * @author 钱洋
 * @date 2017年7月6日 下午3:34:17
 */
public class MainConfig extends JFinalConfig{

	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/hobby", 5);

	}

	@Override
	public void configConstant(Constants me) {
		PropKit.use("config.properties");
		me.setDevMode(true);
		
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/user",UserController.class);
		
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}


	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin druidPlugin = createDruidPlugin();
		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 所有映射在 MappingKit 中自动化搞定
		//_MappingKit.mapping(arp);
		me.add(arp);		
	}


	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

}
