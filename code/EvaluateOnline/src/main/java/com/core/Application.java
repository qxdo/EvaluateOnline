package com.core;

import java.util.Date;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.common.interceptor.StatusInteceptor;
import com.common.route.AdminRoute;
import com.common.route.CommonRoute;
import com.common.route.DockerRoute;
import com.common.route.IndexRoute;
import com.common.route.StudentRoute;
import com.common.route.SwaggerRoute;
import com.common.route.TeacherRoute;
import com.core.common.model.Admin;
import com.core.common.model.Student;
import com.core.common.model.Teacher;
import com.core.common.model._MappingKit;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

public class Application extends JFinalConfig {

	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 *
	 * 使用本方法启动过第一次以后，会在开发工具的 debug、run config 中自动生成 一条启动配置，可对该自动生成的配置再添加额外的配置项，例如 VM
	 * argument 可配置为： -XX:PermSize=64M -XX:MaxPermSize=256M
	 */
	public static void main(String[] args) {
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
		JFinal.start("src/main/webapp", 8088, "/", 5);
	}

	/**
	 * 全局配置
	 */
	@Override
	public void configConstant(Constants constants) {
		// TODO Auto-generated method stub
		PropKit.use("application.properties");
		PropKit.use("log4j.properties");
		// 设置开发者模式
		constants.setDevMode(PropKit.getBoolean("devMode"));

		// 设置Action ReportAfter什么时候出现，默认为true
		constants.setReportAfterInvocation(false);
		// 设置模版引擎
		constants.setViewType(ViewType.JFINAL_TEMPLATE);

		// 设置上传路径
		constants.setBaseUploadPath("static/modules/upload");

		// 设置下载路径
		constants.setBaseDownloadPath("static/modules/template");

		// 设置接收参数分隔符，默认'-'
		// constants.setUrlParaSeparator("");

		// 设置国际化
		constants.setI18nDefaultLocale("i18n");
		constants.setI18nDefaultBaseName("zh_CN");

		// 设置错误代码跳转视图
		// constants.setError401View("");
		// constants.setError403View("");
		constants.setError404View("/views/404.html");
		constants.setError500View("/views/500.html");
		// constants.setErrorView(400, "");

		// 设置字符集
		constants.setEncoding("UTF-8");

		// 设置默认JSON中时间格式
		// constants.setJsonDatePattern("");
		// renderJson和JsonKit底层依赖于JsonManager中设置的JsonFactory
		// constants.setJsonFactory(FastJsonFactory.me());
		// 设置自己的log工厂实现
		// constants.setLogFactory();
	}

	/**
	 * 引擎模板
	 */
	@Override
	public void configEngine(Engine engine) {
		// TODO Auto-generated method stub
		// 设置页面模版
		// engine.addSharedFunction("/_view/common/_layout.html");
		// engine.addSharedFunction("/_view/common/_paginate.html");
		// engine.addSharedFunction("/_view/_admin/common/_admin_layout.html");
	}

	/**
	 * 配置处理器
	 * Handler可以接管所有 web 请求，并对应用拥有完全的控制权，可以很方便地实现更高层的功能性扩展
	 */
	@Override
	public void configHandler(Handlers handlers) {
		// TODO Auto-generated method stub
		DruidStatViewHandler dvh =  new DruidStatViewHandler("/druid");
    	handlers.add(dvh);
	}

	/**
	 * 配置 JFinal 的全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors interceptors) {
		// TODO Auto-generated method stub
		interceptors.add(new StatusInteceptor());
		interceptors.add(new SessionInViewInterceptor());
	}

	/**
	 * 插件
	 */
	@Override
	public void configPlugin(Plugins plugins) {
		// TODO Auto-generated method stub
		Prop prop = PropKit.use("jdbc.properties");
		// 配置Druid数据源 有5个参数:url,username,password,driverclass,filters
		DruidPlugin druidPlugin = new DruidPlugin(prop.get("jdbc.url"), prop.get("jdbc.username").trim(),
				prop.get("jdbc.password").trim(), prop.get("jdbc.driver").trim());
		// 设置druid监控
		druidPlugin.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		druidPlugin.addFilter(wall);

		plugins.add(druidPlugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		
		// 设置sql管理
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath());
        arp.addSqlTemplate("all.sql");
        _MappingKit.mapping(arp);
		
        if (PropKit.getBoolean("devMode", false)) {
			// 开启sql输出
			arp.setShowSql(true);
		}
        
		// 直接配置数据表映射
		plugins.add(arp);
		
	}

	/**
	 * 路由
	 */
	@Override
	public void configRoute(Routes routes) {
		// TODO Auto-generated method stub
		
		routes.add(new AdminRoute()); // 设置管理员端路由
		routes.add(new TeacherRoute());  // 设置教师端路由
		routes.add(new StudentRoute());  // 设置不生端路由
		
		routes.add(new IndexRoute());   // 首页路由
		routes.add(new CommonRoute());  // 公共路径由
		routes.add(new SwaggerRoute());  // Swagger Api
		routes.add(new DockerRoute());//docker 路由
	}
	
    /* (non-Javadoc)
     * @see com.jfinal.config.JFinalConfig#afterJFinalStart()
     */
    @Override
    public void afterJFinalStart() {
        // TODO Auto-generated method stub
        super.afterJFinalStart();
        // 系统初始化时,创建系统管理员
        Admin admin = Admin.dao.findFirst("SELECT * FROM admin WHERE number = ? and type = ? and flag = 1", "admin", 1);
        if (null == admin) {
			// 系统管理员不存在,初始化创建
        	new Admin().set("name", "admin")
        			   .set("number", "admin")
        			   .set("password", HashKit.md5("admin"))
        			   .set("type", 1)
        			   .set("createtime", new Date())
        			   .set("createby", "admin")
        			   .set("modifytime", new Date())
        			   .set("modifyby", "admin")
        			   .save();
        	new Admin().set("name", "admin")
					   .set("number", "admin")
					   .set("password", HashKit.md5("admin"))
					   .set("type", 2)
					   .set("createtime", new Date())
					   .set("createby", "admin")
					   .set("modifytime", new Date())
					   .set("modifyby", "admin")
					   .save();
		}
        Teacher teacher = Teacher.dao.findFirst("SELECT * FROM teacher where number = ? and flag = 1", "admin");
        if (null == teacher) {
			new Teacher().set("name", "admin")
						 .set("number", "admin")
						 .set("password", HashKit.md5("123456"))
						 .set("createtime", new Date())
						 .set("createby", "admin")
						 .set("modifytime", new Date())
						 .set("modifyby", "admin")
						 .save();
		}
        Student student = Student.dao.findFirst("SELECT * FROM student where number = ? and flag = 1", "admin");
        if (null == student) {
			new Student().set("name", "admin")
						 .set("number", "admin")
						 .set("password", HashKit.md5("123456"))
						 .set("createtime", new Date())
						 .set("createby", "admin")
						 .set("modifytime", new Date())
						 .set("modifyby", "admin")
						 .save();
		}
        System.out.println("项目初始化完毕");
    }

    @Override
    public void beforeJFinalStop() {
        // TODO Auto-generated method stub
        System.out.println("项目即将关闭");
        super.beforeJFinalStop();
    }

}
