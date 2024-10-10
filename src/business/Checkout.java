package business;

import java.io.Serializable;
import java.time.LocalDate;


import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

final public class Checkout implements Serializable {
	private String memberId;
	
	public Checkout(String memberId, String fname, String lname, String tel,Address add) {
		this.memberId = memberId;		
	}
	
	
	public String getMemberId() {
		return memberId;
	}

	
	
	@Override
	public String toString() {
		return "";
	}

	private static final long serialVersionUID = -2226197306790714013L;
}
