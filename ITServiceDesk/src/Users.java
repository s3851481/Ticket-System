
public class Users {
	private String email;
	private String fullName;
	private int phoneNum;
	private String password;
	
	public Users(String email, String fullName, int phoneNum, String password) {
		super();
		this.email = email;
		this.fullName = fullName;
		this.phoneNum = phoneNum;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getFullName() {
		return fullName;
	}

	public int getPhoneNum() {
		return phoneNum;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setPhoneNum(int phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
