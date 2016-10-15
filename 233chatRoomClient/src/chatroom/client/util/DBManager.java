package chatroom.client.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class use to operate the database.<br/> 
 * Temporarily intended to use MySQL.
 */
public class DBManager {

	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://2bsticker.cn:3306/233chatroom?useSSL=true";
	
	private final String USER = "chatroom";
	private final String PASS = "thePasswordThatEasyKnow";
	
	private Connection connection;
	
	public DBManager(){
		try {
			//加载jdbc驱动
			Class.forName(JDBC_DRIVER);
			
			//连接到远程数据库
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//数据库查询语句
	//use to check whether the database has this user. SELECT operation.
	public User select(String username, String password){
		User user = null;
		ResultSet resultSet= null;
		
		String sql = "SELECT * FROM UserInfo WHERE username=? and password=?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet!=null){
				user = new User();
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
				user.setSex(resultSet.getBoolean("sex"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	//新用户注册插入数据库 
	//new user register insert to database.
	public boolean insert(User user){
		String sql = "INSERT INTO UserInfo (username,password,sex) values(?,?,?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setBoolean(3, user.getSex());
		} catch (SQLException e) { //insert失败，已存在用户或数据库满，返回false
			return false;
		}
		return true;
	}
	
	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//use for create user information table, but it is only run once.
	/* 只使用一次用于建表的函数
	public static void createTable() throws SQLException {
		Connection connection = null;
		Statement statement = null;
		
		try {
			//Load jdbc driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//Connect to database.
			System.out.println("Connecting to a selected database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");
			
			System.out.println("Creating table in given database...");
			statement = connection.createStatement();
			
			String sql = "CREATE TABLE UserInfo (" + 
						 "username VARCHAR(255) UNIQUE NOT NULL, " +
						 "password CHAR(32) NOT NULL, " + 
						 "sex BOOLEAN NOT NULL )";
			
			statement.executeUpdate(sql);
			System.out.println("Created table in given database...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(statement!=null){
				statement.close();
			}
			if(connection!=null){
				connection.close();
			}
		}
	}
	*/
}
