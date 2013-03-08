package mypackage;

import javax.microedition.pim.PIM;

import net.rim.blackberry.api.pdap.BlackBerryContactList;

public class ContactsNumber implements Runnable {
	int number;
	
	public ContactsNumber() {
		number = 0;
	}
	
	public void run() {
    	try {
    		BlackBerryContactList contactList = (BlackBerryContactList)
    				PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
    		number = contactList.size();
    		contactList.close();
    	}
    	catch(Exception e) {
    		//return;
    	}
	}	
}
