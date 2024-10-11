package business;

import java.util.List;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public List<String> allBookIds();
	public boolean addBookCopies(Book book, int quantity);
	public boolean addNewBook(String isbn, String title, int maxCheckoutLength, List<Author> authors);
	
}
