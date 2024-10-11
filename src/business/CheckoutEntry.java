package business;

import java.io.Serializable;
import java.time.LocalDate;


import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

final public class CheckoutEntry implements Serializable {
	private LibraryMember libraryMember;
	private BookCopy bookCopy;
	private User user;
    private LocalDate dueDate;
    private LocalDate checkedOutDate;
    private double fine;
    private boolean isCheckedIn;

    public CheckoutEntry(LocalDate dueDate, BookCopy bookCopy, LibraryMember libraryMember, User user) {
    	this.user = user;
        this.dueDate = dueDate;
        this.bookCopy = bookCopy;
        this.libraryMember = libraryMember;
        this.checkedOutDate = LocalDate.now();
        this.isCheckedIn = false;
        this.fine = 0;
    }

    public Object[] toRowData() {
        return new Object[]{
                -1,
                bookCopy.getBook().getIsbn(),
                bookCopy.getBook().getTitle(),
                checkedOutDate,
                dueDate,
        };
    }

    ;

    public LocalDate getCheckedOutDate() {
        return checkedOutDate;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public double getFine() {
        return fine;
    }

    public boolean getCheckInStatus() {
        return isCheckedIn;
    }

    public BookCopy getCheckOutBook() {
        return bookCopy;
    }
    
    public User getUser() {
        return user;
    }

    public String toString() {
        return bookCopy.getBook().getIsbn() + "\t\t" + bookCopy.getBook().getTitle() + "\t\t\t" + checkedOutDate + "\t\t" + dueDate + "\t\t" + bookCopy.getCopyNum() + "\n";
    }
    
	private static final long serialVersionUID = -2226197306790714013L;
}
