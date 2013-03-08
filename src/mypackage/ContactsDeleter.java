package mypackage;

import java.util.Enumeration;

import javax.microedition.pim.PIM;

import net.rim.blackberry.api.pdap.BlackBerryContactList;
import net.rim.blackberry.api.pdap.BlackBerryContact;

public class ContactsDeleter implements Runnable {
	int deleted;
	
	public ContactsDeleter() {
		deleted = 0;
	}
	
	public void run() {
		int contacts = 0;
    	
    	try {
    		BlackBerryContactList contactList = (BlackBerryContactList)
    				PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
    		
    		Enumeration e = contactList.items();

    		while(e.hasMoreElements()) {
    			BlackBerryContact contact = (BlackBerryContact)e.nextElement();
    			contactList.removeContact(contact);
    			contacts++;
    		}
    		
    		contactList.close();
    	}
    	catch(Exception e) {
    		//return;
    	}
        
    	deleted = contacts;		
	}
}
