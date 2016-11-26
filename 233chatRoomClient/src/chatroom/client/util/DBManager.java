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
			//����jdbc����
			Class.forName(JDBC_DRIVER);
			
			//���ӵ�Զ�����ݿ�
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//���ݿ��ѯ���
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
			
			resultSet = preparedStatement.executeQuery();  //����Ҳ������׳��쳣�����᷵�ؿ�
					
			user = new User();
			resultSet.next();    //resultSet��ʼָ���ѯ�����ݵ�ǰһ�У���Ҫ��next�ƶ��α��ȡ��һ�����ݣ���Ϊ���Ψһ���ʲ���ѭ��
			user.setUsername(resultSet.getString("username"));
			user.setPassword("The_True_Password_Won't_Show_To_Anybody"); // ��½�ɹ��󲻻����ʵ���봢��
			user.setSex(resultSet.getBoolean("sex"));
			
		} catch (Exception e) {
			return null;
		}
		return user;
	}
	
	//���û�ע��������ݿ� 
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
		} catch (SQLException e) { //insertʧ�ܣ��Ѵ����û������ݿ���������false
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
	//ֻʹ��һ�����ڽ���ĺ���
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
