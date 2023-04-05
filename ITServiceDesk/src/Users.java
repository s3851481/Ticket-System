import java.io.Serializable;
import java.util.Hashtable;

public class Users implements Serializable {
	private String email;
	private String fullName;
	private String phoneNum;
	private String password;
	private userType type;
	private Hashtable<String, String> securityQuestions = new Hashtable<>();
	
	public Users(String email, String fullName, String phoneNum, String password, userType type, Hashtable<String,String> securityQuestions) {
		this.setEmail(email);
		this.setFullName(fullName);
		this.setPhoneNum(phoneNum);
		this.setPassword(password);
		this.setType(type);
		this.setSecurityQuestions(securityQuestions);
	}

	public String getEmail() {
		return email;
	}

	public String getFullName() {
		return fullName;
	}

	public String getPhoneNum() {
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

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public userType getType() {
		return type;
	}

	public void setType(userType type) {
		this.type = type;
	}
	public Hashtable<String, String> getSecurityQuestions() {
		return securityQuestions;
	}

	public void setSecurityQuestions(Hashtable<String, String> securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
}