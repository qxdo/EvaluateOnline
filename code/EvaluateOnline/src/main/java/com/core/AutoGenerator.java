package com.core;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

public class AutoGenerator {

	public static DataSource getDataSource() {
		// 加载配置文件
		Prop prop = PropKit.use("jdbc.properties");
		// 配置Druid数据源 有5个参数:url,username,password,driverclass,filters
		DruidPlugin druidPlugin = new DruidPlugin(prop.get("jdbc.url"), prop.get("jdbc.username").trim(),
				prop.get("jdbc.password").trim(), prop.get("jdbc.driver"));
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "com.common.basemodel";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/common/basemodel";
		// String baseModelOutputDir = PathKit.getWebRootPath() +
		// "/test/main/java/com/common/model";

		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.core.common.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/core/common/model";

		// 创建生成器
		Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName,
				modelOutputDir);
		// Generator generator = new Generator(getDataSource(), baseModelPackageName,
		// baseModelOutputDir);
		/*
		 * BaseModelGenerator baseModelGenerator = new
		 * BaseModelGenerator(baseModelPackageName, baseModelOutputDir);
		 * 
		 * List<TableMeta> tableMetas = new ArrayList<TableMeta>(); MetaBuilder
		 * metaBuilder = new MetaBuilder(getDataSource()); tableMetas =
		 * metaBuilder.build();
		 * 
		 * baseModelGenerator.generate(tableMetas); Generator generator = new
		 * Generator(getDataSource(), baseModelGenerator);
		 */

		// 设置数据库方言
		generator.setDialect(new MysqlDialect());
		// 添加不需要生成的表名
		// generator.addExcludedTable("adv");
		// 设置是否在 Model 中生成 dao 对象
		generator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		generator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非
		// OscUser
		// gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		generator.generate();
	}

}