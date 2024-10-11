package librarysystem;

import java.util.List;
import dataaccess.User;

public interface DataCallback {
	void onLoginSuccess(boolean[] enabled);
	void setUser(User user);
	public User getUser();
}
