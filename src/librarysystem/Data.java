package librarysystem;
import java.util.*;

/**
 * This class stores "dummy data" for the Gui. 
 */
public class Data {
	
	private static List<String> bookTitles = new ArrayList<>();
    private static TitleCallback callback;
	
	public static final Data INSTANCE = new Data();
	
	
	/////////////names
	public final static String MESSIAH_OF_DUNE = "Messiah Of Dune";	
	public final static String GONE_WITH_THE_WIND = "Gone With The Wind";
	public final static String GARDEN_OF_RAMA = "Garden of Rama";
	
	private Data() {
		bookTitles.add(MESSIAH_OF_DUNE);
		bookTitles.add(GONE_WITH_THE_WIND);
		bookTitles.add(GARDEN_OF_RAMA);
	}
	
	// Method to add a book title and notify the callback
    public static void addBook(String bookTitle) {
        bookTitles.add(bookTitle);
        if (callback != null) {
            callback.onDataUpdated();
        }
    }

    // Method to get the list of book titles
    public static List<String> getBookTitles() {
        return new ArrayList<>(bookTitles);
    }

    // Method to set the callback
    public static void setCallback(TitleCallback dataCallback) {
        callback = dataCallback;
    }
	
//    public static List<String> bookTitles = new ArrayList<>() {
//    	
//    	{
//           add(MESSIAH_OF_DUNE);
//           add(GONE_WITH_THE_WIND);
//           add(GARDEN_OF_RAMA);
//    	}
//    };
    
//    public static void addBookTitle(String title) {
//    	bookTitles.add(title);
//    }
    
    public static Auth currentAuth = null;
    
    public static List<User> logins = new ArrayList<>() {
    	
    	{
           add(new User("Joe", "111", Auth.LIBRARIAN));
           add(new User("Ann", "101", Auth.ADMIN));
           add(new User("Dave", "102", Auth.BOTH));
    	}
    };
    
    
    
    
           

    
    
}