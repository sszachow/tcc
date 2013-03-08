package mypackage;

import java.util.Random;

import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;

import net.rim.blackberry.api.pdap.BlackBerryContactList;
import net.rim.blackberry.api.pdap.BlackBerryContact;

public class ContactsCreator implements Runnable {
	int toCreate, createdContacts;
	String chars, numbers, indexingChars;
	Random rng;
	
	public ContactsCreator() {
		toCreate = 0;
		createdContacts = 0;
		chars = "abcdefghijklmnopqrstuvwxyz";
		numbers = "0123456789";
		indexingChars = chars.toUpperCase() + numbers;
		rng = new Random();
	}
	
	private String genText(int length, String base) {
		char[] text = new char[length];
	    
		for (int i = 0; i < length; i++) {
	        text[i] = base.charAt(rng.nextInt(base.length()));
	    }
	    
	    return new String(text);
	}
	
	private String genStr(int length) {
		return genText(length, chars);
	}
	
	private String genStrCap(int length) {
		StringBuffer result = new StringBuffer(genStr(length));
		result.setCharAt(0, Character.toUpperCase(result.charAt(0)));
		
		return result.toString();
	}
	
	private String genNum(int length) {
		return genText(length, numbers);
	}
	
	private String[] genNames(int i) {
		String num = Integer.toString(i + 1);
		char indxChar = indexingChars.charAt(i % indexingChars.length());
		String[] result = {"", "", ""};
		result[0] = indxChar + "_First_" + num;
		result[1] = indxChar + "_Middle_" + num;
		result[2] = indxChar + "_Last_" + num;
				
		return result;
	}
	
	private String genFormattedName(int i) {
		String[] names = genNames(i);
		
		return names[0] + " " + names[1] + " " + names[2];
	}
	
	private String genEmail() {
		return genStr(5) + "." + genStr(8) + "@" + genStr(5) + "." + genStr(2);
	}
	
	private String genURL() {
		return "http://www." + genStr(9) + ".com";
	}
	
	private byte[] genPhoto(int i) {
		int photoIndex = i % StringsArray.STRINGS_ARRAY.length;
		return StringsArray.STRINGS_ARRAY[photoIndex].getBytes();
	}
	
	private int getPhotoSize(int i) {
		int photoIndex = i % StringsArray.SIZES_ARRAY.length;
		return StringsArray.SIZES_ARRAY[photoIndex];
	}
	
	private void addContactNames(int i, BlackBerryContact c, BlackBerryContactList cList) {
		String[] name = new String[cList.stringArraySize(BlackBerryContact.NAME)];
		String[] names = genNames(i);
		
		if (cList.isSupportedField(BlackBerryContact.FORMATTED_NAME))
			c.addString(BlackBerryContact.FORMATTED_NAME, PIMItem.ATTR_NONE, genFormattedName(i));
		if (cList.isSupportedArrayElement(BlackBerryContact.NAME, BlackBerryContact.NAME_FAMILY))
			name[BlackBerryContact.NAME_FAMILY] = names[2];
		if (cList.isSupportedArrayElement(BlackBerryContact.NAME, BlackBerryContact.NAME_OTHER))
			name[BlackBerryContact.NAME_OTHER] = names[1];
		if (cList.isSupportedArrayElement(BlackBerryContact.NAME, BlackBerryContact.NAME_GIVEN))
			name[BlackBerryContact.NAME_GIVEN] = names[0];
		c.addStringArray(BlackBerryContact.NAME, PIMItem.ATTR_NONE, name);		
	}
	
	private String[] genAddressArray(BlackBerryContactList cList) {
		String[] addr = new String[cList.stringArraySize(BlackBerryContact.ADDR)];
		
		if (cList.isSupportedArrayElement(BlackBerryContact.ADDR, BlackBerryContact.ADDR_COUNTRY))
			addr[BlackBerryContact.ADDR_COUNTRY] = genStrCap(5);
		if (cList.isSupportedArrayElement(BlackBerryContact.ADDR, BlackBerryContact.ADDR_LOCALITY))
			addr[BlackBerryContact.ADDR_LOCALITY] = genStrCap(8);
		if (cList.isSupportedArrayElement(BlackBerryContact.ADDR, BlackBerryContact.ADDR_POSTALCODE))
			addr[BlackBerryContact.ADDR_POSTALCODE] = genNum(3) + "-" + genNum(3);
		if (cList.isSupportedArrayElement(BlackBerryContact.ADDR, BlackBerryContact.ADDR_STREET))
			addr[BlackBerryContact.ADDR_STREET] = genNum(3) + " " + genStrCap(5) + " " + genStrCap(8);
		
		return addr;
	}
	
	private void addContactAddresses(BlackBerryContact c, BlackBerryContactList cList) {
		String[] addrHome = genAddressArray(cList);
		String[] addrWork = genAddressArray(cList);
		
		if (cList.isSupportedField(BlackBerryContact.ADDR)) {
			c.addStringArray(BlackBerryContact.ADDR, BlackBerryContact.ATTR_HOME, addrHome);
			c.addStringArray(BlackBerryContact.ADDR, BlackBerryContact.ATTR_WORK, addrWork);
		}
	}
	
	private void addContactNumbers(BlackBerryContact c, BlackBerryContactList cList) {
		if (cList.isSupportedField(BlackBerryContact.TEL)) {
			c.addString(BlackBerryContact.TEL, BlackBerryContact.ATTR_HOME, genNum(9));
			c.addString(BlackBerryContact.TEL, BlackBerryContact.ATTR_MOBILE, genNum(12));
			c.addString(BlackBerryContact.TEL, BlackBerryContact.ATTR_WORK, genNum(9));
		}
	}
	
	private void addContactEmails(BlackBerryContact c, BlackBerryContactList cList) {
		if (cList.isSupportedField(BlackBerryContact.EMAIL)) {
			c.addString(BlackBerryContact.EMAIL, BlackBerryContact.ATTR_HOME, genEmail());
			c.addString(BlackBerryContact.EMAIL, BlackBerryContact.ATTR_WORK, genEmail());
			c.addString(BlackBerryContact.EMAIL, BlackBerryContact.ATTR_PREFERRED, genEmail());
		}
	}
	
	private void addContactWebpage(BlackBerryContact c, BlackBerryContactList cList) {
		if (cList.isSupportedField(BlackBerryContact.URL)) {
			c.addString(BlackBerryContact.URL, BlackBerryContact.ATTR_WORK, genURL());
		}
		
	}
	
	private void addContactPhoto(int i, BlackBerryContact c) {
		if(c.countValues( BlackBerryContact.PHOTO ) > 0)
			c.setBinary(BlackBerryContact.PHOTO, 0, PIMItem.ATTR_NONE, genPhoto(i), 0, getPhotoSize(i));
		else
			c.addBinary(BlackBerryContact.PHOTO, PIMItem.ATTR_NONE, genPhoto(i), 0, getPhotoSize(i));		
	}
		
	private void addContactFields(int i, BlackBerryContact c, BlackBerryContactList cList) {
		addContactNames(i, c, cList);
		addContactAddresses(c, cList);
		addContactNumbers(c, cList);
		addContactEmails(c, cList);
		addContactWebpage(c, cList);
		addContactPhoto(i, c);
	}
	
	public void run() {
		int created = 0;
		BlackBerryContactList contacts = null;
		
		try {
			contacts = (BlackBerryContactList) PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
		} 
		catch (Exception e) {
			//return;
		}

		for(int i = 0; i < toCreate; i++) {
			BlackBerryContact contact = (BlackBerryContact) contacts.createContact();
			addContactFields(i, contact, contacts);
			
			try {
				contact.commit();
				created++;
			}
			catch(PIMException e) {
			}
		}
		
		try {
			contacts.close();
		} 
		catch (PIMException e) {
		}
		
		createdContacts = created;
	}
	
}
