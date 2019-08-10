import java.io.Serializable;

/**
 * Stores the data for user accounts.
 */
public class UserAccount implements Serializable{
	private String userName;
	private String password;
	
	/**
	 * Constructor
	 * @param username
	 * @param password
	 */
	public UserAccount(String username, String password){
		this.userName = username;
		this.password = password;
	}

	/**
	 * Gets the user name linked to the user account.
	 * @return The user name of the account.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Gets the password linked to the user account.
	 * @return The password of the account.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Changes the password of the account.
	 * @param password New password.
	 */
	public void changePasswordTo(String password) {
		this.password = password;
	}
	
	/**
	 * Checks to see if two accounts are the same.
	 * @param account The account being compared to.
	 * @return	True if the same, false if not.
	 */
	public boolean isTheSameAccount(UserAccount account){
		if(this.userName.equals(account.userName) && this.password.equals(account.password)){
			return true;
		}
		return false;
	}
	
}
