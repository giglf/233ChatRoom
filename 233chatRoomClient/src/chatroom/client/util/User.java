package chatroom.client.util;

/**
 * This is a User class for a normal user.<br/>
 * You should pay attention to the sex information.<br/>
 * true->male, false->female
 */
public class User {
	
	private String username;
	private String password;
	private boolean sex; //true for male, and false for female.
	
	public static final boolean MALE = true;
	public static final boolean FEMALE = false;
	
	public User(){
	}
	
	public User(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.sex = user.getSex();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	
}
