package chatroom.client.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class use to operate the database.<br/> 
 * Temporarily intended to use MySQL.
 */
public class DBManager {

	private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final static String DB_URL = "jdbc:mysql://localhost:3306/233chatroom?useSSL=true";
	
	private final static String USER = "chatroom";
	private final static String PASS = "thePasswordThatEasyKnow";
	
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
		
		String sql = "SELECT username, sex FROM UserInfo WHERE username=? and password=?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			
			resultSet = preparedStatement.executeQuery();  //如果找不到会抛出异常，不会返回空
					
			user = new User();
			resultSet.next();    //resultSet开始指向查询到数据的前一行，需要用next移动游标获取第一行数据，因为结果唯一，故不用循环
			user.setUsername(resultSet.getString("username"));
			user.setPassword("The_True_Password_Won't_Show_To_Anybody"); // 登陆成功后不会把真实密码储存
			user.setSex(resultSet.getBoolean("sex"));
			
		} catch (Exception e) {
			return null;
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
			preparedStatement.executeUpdate();
		} catch (SQLException e) { //insert失败，已存在用户或数据库满，返回false
			return false;
		}
		return true;
	}
	
	public void close(){
		try {
			connection.close();
			System.out.println("Connection to Database interrupt.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//use for create user information table, but it is only run once.
	//只使用一次用于建表的函数
	/*
	public static void main(String[] args) throws SQLException {
		createTable();
	}
	
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
						 "username VARCHAR(32) PRIMARY KEY, " +
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
