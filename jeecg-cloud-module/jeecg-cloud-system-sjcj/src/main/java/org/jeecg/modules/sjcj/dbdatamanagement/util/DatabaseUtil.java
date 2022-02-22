package org.jeecg.modules.sjcj.dbdatamanagement.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作工具类
 * @Author: test
 */
public class DatabaseUtil {

	private String driver, url, sql;
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private PrintWriter writer;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**Oracle数据库*/
	private final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	/**MySql数据库*/
	private final String DRIVER_MYSQL = "com.mysql.cj.jdbc.Driver";
	/**SQL Server数据库*/
	private final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/**达梦数据库*/
	private final String DRIVER_DM = "dm.jdbc.driver.DmDriver";
	/**金仓数据库*/
	private final String DRIVER_KINGBASE = "com.kingbase8.Driver";

	/**oracle*/
	private final String ORACLE_NAME="oracle";
	/**mysql*/
	private final String MYSQL_NAME="mySql";
	/**sqlsever*/
	private final String SQLSERVER_NAME="sqlServer";
	/**dm*/
	private final String DM_NAME="dm";
	/**kingbase*/
	private final String KINGBASE_NAME="kingbase";
	/**
	 * 获取Connection
	 */
	public Connection getConn(String dbType, String dbIp, String port, String dbName, String userName,
			String password) {
//		if (ORACLE_NAME.equals(dbType)) {
//			driver = DRIVER_ORACLE;
//		} else if (MYSQL_NAME.equals(dbType)) {
//			driver = DRIVER_MYSQL;
//			url = "jdbc:mysql://";
//		} else if (SQLSERVER_NAME.equals(dbType)) {
//			driver = DRIVER_SQLSERVER;
//			url = "jdbc:sqlserver://";
//		} else if (DM_NAME.equals(dbType)) {
//			driver = DRIVER_DM;
//			url = "jdbc:dm://";
//		} else if (KINGBASE_NAME.equals(dbType)) {
//			driver = DRIVER_KINGBASE;
//			url = "jdbc:kingbase8://";
//		}
//
//		if (ORACLE_NAME.equals(dbType)) {
//			url = "jdbc:oracle:thin:@" + dbIp + ":" + port + ":orcl";
//		} else if (SQLSERVER_NAME.equals(dbType)) {
//			url += dbIp + ":" + port + ";DatabaseName=" + dbName;
//		} else {
//			url += dbIp + ":" + port + "/" + dbName;
//		}
//
//		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userName, password);
//		} catch (Exception e) {
//			conn = null;
//		}
		return conn;
	}

	/**
	 * 测试连接数据库是否成功
	 */
	public String isConnect(String dbType, String dbIp, String port, String dbName, String userName, String password) {
		String result = "";
//		conn = this.getConn(dbType, dbIp, port, dbName, userName, password);
//		try {
//			if (null == conn) {
//				result = "测试连接数据库失败";
//			} else if (!conn.isClosed()) {
//				result = "测试连接数据库成功";
//				conn.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return result;
	}

	/**
	 * 采集数据库数据
	 */
	public Map<String, Object> collectData(String dbType, String dbIp, String port, String dbName, String userName,
			String password, String sqlStatement, MinioUtil minioUtil) {
		// 数据库文件本地暂存路径
		Map<String, Object> map = new HashMap<>(2000);
//		String dbDataPath = minioUtil.getDbDataPath();
//		conn = this.getConn(dbType, dbIp, port, dbName, userName, password);
//		try {
//			// 数据库连接成功
//			if (!conn.isClosed()) {
//				String select="SELECT";
//				// 处理SQL语句(SQL语句必须以SELECT或select开头且必须以分号结尾 )
//				Integer end=6;
//				if (select.equals(sqlStatement.substring(0, end).toUpperCase())) {
//					// 过滤SQL语句(截取首个分号前的语句, 不包含分号)
//					sql = sqlStatement.substring(0, sqlStatement.indexOf(";"));
//					String testSqlContains = sql.toUpperCase();
//					// MySQL数据库数据写入
//					String sign="LIMIT";
//					if (MYSQL_NAME.equals(dbType) && !testSqlContains.contains(sign)) {
//						int index = 0;
//						boolean flag = false;
//						map = this.writeMySqlFile(index, flag, sql, conn, writer, dbDataPath);
//					} else {
//						map = this.writeFile(sql, conn, writer, dbDataPath);
//					}
//					String collectingResult = (String) map.get("collectingResult");
//					String sign1=".sqa";
//					if (collectingResult.contains(sign1)) {
//						String fileName = collectingResult.substring(collectingResult.lastIndexOf("/") + 1);
//						// 桶内存放路径
//						String fileFullPath = "databaseData/" + dbType + "/" + map.get("timeStr") + "/" + fileName;
//						// 将采集的数据库数据文件上传至MinIO上
//						minioUtil.uploadToBucket(fileFullPath, collectingResult);
//						// 删除本地文件
//						new File(collectingResult).delete();
//						// 数据文件名称
//						map.put("fileName", fileName);
//						// 数据文件路径
//						map.put("filePath", fileFullPath);
//						map.put("collectingResult", "数据库数据采集成功");
//					}
//				}
//				// 数据库连接失败
//			} else if (null == conn || conn.isClosed()) {
//				map.put("collectingResult", "数据库连接失败");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return map;
	}

	/**
	 * MySQL数据库数据写入文件
	 */
	public synchronized Map<String, Object> writeMySqlFile(int index, boolean flag, String sql, Connection conn, PrintWriter writer,
			String dbDataPath) {
		Map<String, Object> map = new HashMap<>(2000);
//		String result = "";
//		Date date = null;
//		String ymd = "";
//		String hms = "";
//		if (flag) {
//			return null;
//		}
//		if (0 != index) {
//			sql = sql.substring(0, sql.indexOf(" LIMIT"));
//		}
//		sql += " LIMIT " + index + ", 10";
//		try {
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(sql);
//			if (rs.isBeforeFirst()) {
//				try {
//					if (index == 0) {
//						date = new Date();
//						String[] times = sdf.format(date).split(" ");
//						ymd = times[0].replace("-", "_");
//						hms = times[1].replace(":", "_");
//
//						// 加入本地暂存放路径
//						result = dbDataPath + "DatabaseData_" + ymd + "_" + hms + ".sqa";
//						writer = new PrintWriter(new OutputStreamWriter(
//								new BufferedOutputStream(new FileOutputStream(result)), "UTF-8"));
//					}
//				} catch (UnsupportedEncodingException | FileNotFoundException e) {
//					e.printStackTrace();
//				}
//				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//					if (i == rs.getMetaData().getColumnCount()) {
//						writer.println(rs.getMetaData().getColumnName(i));
//					} else {
//						writer.print(rs.getMetaData().getColumnName(i) + " || ");
//					}
//				}
//				while (rs.next()) {
//					for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//						if (i == rs.getMetaData().getColumnCount()) {
//							writer.println(rs.getString(i) + ";");
//						} else {
//							writer.print(rs.getString(i) + " || ");
//						}
//					}
//				}
//				index += 10;
//			} else {
//				flag = true;
//			}
//			this.writeMySqlFile(index, flag, sql, conn, writer, dbDataPath);
//		} catch (SQLException e) {
//			result = e.getMessage();
//			e.printStackTrace();
//		} finally {
//			writer.close();
//		}
//		map.put("collectingResult", result);
//		map.put("collectingTime", date);
//		map.put("timeStr", ymd);
		return map;
	}

	/**
	 * 将数据库数据写入文件
	 */
	public synchronized Map<String, Object> writeFile(String sql, Connection conn, PrintWriter writer, String dbDataPath) {
		Map<String, Object> map = new HashMap<>(2000);
//		String result = "";
//		Date date = null;
//		String ymd = "";
//		String hms = "";
//		try {
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(sql);
//			if (rs.isBeforeFirst()) {
//				try {
//					date = new Date();
//					String[] times = sdf.format(date).split(" ");
//					ymd = times[0].replace("-", "_");
//					hms = times[1].replace(":", "_");
//					result = dbDataPath + "DatabaseData_" + ymd + "_" + hms + ".sqa";
//					writer = new PrintWriter(
//							new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(result)), "UTF-8"));
//				} catch (UnsupportedEncodingException | FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//
//			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//				if (i == rs.getMetaData().getColumnCount()) {
//					writer.println(rs.getMetaData().getColumnName(i));
//				} else {
//					writer.print(rs.getMetaData().getColumnName(i) + " || ");
//				}
//			}
//			while (rs.next()) {
//				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//					if (i == rs.getMetaData().getColumnCount()) {
//						writer.println(rs.getString(i) + ";");
//					} else {
//						writer.print(rs.getString(i) + " || ");
//					}
//				}
//			}
//		} catch (SQLException e) {
//			result = e.getMessage();
//			e.printStackTrace();
//		} finally {
//			writer.close();
//		}
//		map.put("collectingResult", result);
//		map.put("collectingTime", date);
//		map.put("timeStr", ymd);
		return map;
	}

}
