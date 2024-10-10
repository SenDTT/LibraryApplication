package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

final public class LibraryMember extends Person implements Serializable {
	private String memberId;
	private List<CheckoutEntry> records;
	
	public LibraryMember(String memberId, String fname, String lname, String tel,Address add) {
		super(fname,lname, tel, add);
		this.memberId = memberId;	
		this.records = new ArrayList<CheckoutEntry>();
	}
	
	
	public String getMemberId() {
		return memberId;
	}

	public List<CheckoutEntry> getCheckoutEntries() {
		return this.records;
	}
	
	@Override
	public String toString() {
		return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + 
				", " + getTelephone() + " " + getAddress();
	}

	private static final long serialVersionUID = -2226197306790714013L;
}
