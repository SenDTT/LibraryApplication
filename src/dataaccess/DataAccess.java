package dataaccess;

import java.util.HashMap;

import business.Author;
import business.Book;
import business.CheckoutEntry;
import business.LibraryMember;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public void saveNewMember(LibraryMember member); 
	public void updateBookMap(HashMap<String,Book> books);
	public CheckoutEntry checkoutBook(String memberId, String isbn);
	public void saveNewAuthor(Author author);
}
