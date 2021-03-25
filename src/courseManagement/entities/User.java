package courseManagement.entities;


public abstract class User {
	
	private static long count = 3000;
	private long  userId;
	private String name;
	public static UserType userType = UserType.STUDENT;
	private boolean logout = false;
	
	public static enum UserType{
		ADMINISTRATOR,
		INSTRUCTOR,
		STUDENT
	}
	
	public User( String name, UserType userType ) {
		this.name = name;
		User.userType = userType;
		count++;
		userId = count;
		
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public abstract void display();
	
	public UserType getUserType() {
		return userType;
	}
	
	public boolean isLogout() {
		return logout;
	}
	
	public void setLogout(boolean logout) {
		this.logout = logout;
	}
	
}
