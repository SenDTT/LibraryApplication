package business;

import java.util.List;

import dataaccess.User;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public List<String> allBookIds();
	public boolean addBookCopies(Book book, int quantity);
	public boolean addNewBook(String isbn, String title, int maxCheckoutLength, List<Author> authors);
	public LibraryMember findMember(String memberId) throws LibrarySystemException;
	public Book findBook(String isbn) throws LibrarySystemException;
	public CheckoutEntry checkoutBook(String memberId, String isbn, User user);
}
