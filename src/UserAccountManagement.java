import org.mapdb.*;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * The class is used for storing accounts using mapdb and 
 * concurrent navigable maps to provide a thread safe environment
 * for the login system.
 */
public class UserAccountManagement {
	// The accounts are stored using a concurrent navigable map to prevent
	// deadlock if accounts were logging in at the same time.
	DB accountDB = DBMaker.newFileDB(new File("accountDB")).closeOnJvmShutdown().encryptionEnable("password").make();
	ConcurrentNavigableMap<Integer, UserAccount> accountNavmap = accountDB.getTreeMap("accountNavmap");
	DB lockedDB = DBMaker.newFileDB(new File("lockedDB")).closeOnJvmShutdown().encryptionEnable("password").make();
	ConcurrentNavigableMap<Integer, UserAccount> lockedNavmap = lockedDB.getTreeMap("lockedNavmap");

	public UserAccountManagement() {}

	/**
	 * Adds a new account to the available accounts map.
	 * @param anAccount	The account to be added.
	 * @return	True if added.
	 */
	public boolean addAccount(UserAccount anAccount) {
		String name = anAccount.getUserName();
		String password = anAccount.getPassword();
		// Check if the account already exists.
		for (int i = 0; i < this.accountNavmap.size(); i++) {
			if (accountNavmap.get(i).getUserName().equals(name)) {
				System.out.println("Account is already in system");
				return false;
			}
		}
		// Check if the account is already logged in.
		for (int i = 0; i < lockedNavmap.size(); i++) {
			if (lockedNavmap.get(i).getUserName().equals(name)) {
				System.out.println("Account is already in system");
				return false;
			}
		}
		// Creates a new account and adds it the the map.
		UserAccount newAccount = new UserAccount(name, password);
		accountNavmap.put(accountNavmap.size(), newAccount);
		return true;
	}

	/**
	 * Checks to see if the account being logged in is valid.
	 * @param anAccount The account being checked.
	 * @return	True if allowed;
	 */
	public boolean allowLogin(UserAccount anAccount) {
		// Check account in avaiableList
		for (int i = 0; i < accountNavmap.size(); i++) {
			try {
				if (accountNavmap.get(i).isTheSameAccount(anAccount)) {
					// allow Login
					System.out.println("Allow login");
					lockedNavmap.put(lockedNavmap.size(), anAccount);
					accountNavmap.remove(i);
					return true;
				}
			} catch (Exception e) {}
		}
		return false;
	}

	/**
	 * Returns the user account at the index.
	 * @param i	Index of the user.
	 * @return	The user account at the index.
	 */
	public UserAccount getUser(int i) {
		return lockedNavmap.get(i);
	}

	/**
	 * Closes each of the account and locked mapdb.
	 */
	public void closeDB() {
		accountDB.close();
		lockedDB.close();
	}
}
