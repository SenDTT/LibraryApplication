package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;
	DataAccessFacade da = new DataAccessFacade();

	public void login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if (!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if (!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		currentAuth = map.get(id).getAuthorization();

	}

	@Override
	public List<String> allMemberIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		return retval;
	}

	@Override
	public List<String> allBookIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		return retval;
	}

	@Override
	public boolean addBookCopies(Book book, int quantity) {
		try {
			for (int i = 0; i < quantity; i++) {
				book.addCopy();
			}
		} catch (Exception e) {
			return false;
		}

		HashMap<String, Book> books = da.readBooksMap();
		books.put(book.getIsbn(), book);
		da.updateBookMap(books);

		return true;
	}

	@Override
	public boolean addNewBook(String isbn, String title, int maxCheckoutLength, List<Author> authors) {
		try {
			Book newBook = new Book(isbn, title, maxCheckoutLength, authors);
			da.saveNewBook(newBook);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	@Override
	public LibraryMember findMember(String memberId) throws LibrarySystemException {
		HashMap<String, LibraryMember> membersDatabase = da.readMemberMap();
		LibraryMember member = membersDatabase.get(memberId);
		
		return member;
	}

	@Override
	public Book findBook(String isbn) throws LibrarySystemException {
		if (isbn.isEmpty()) {
			throw new LibrarySystemException("Status: Please input Book ISBN!!!");
		}

		HashMap<String, Book> booksDatabase = da.readBooksMap();
		
		Book book = booksDatabase.get(isbn);
		
		return book;
	}

	@Override
	public CheckoutEntry checkoutBook(String memberId, String isbn, User user) {
		DataAccess da = new DataAccessFacade();
        CheckoutEntry resCheckoutEntry = da.checkoutBook(memberId, isbn, user);
        
        return resCheckoutEntry;
	}
}
