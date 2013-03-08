package mypackage;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

public class TestContactsCreatorScreen extends MainScreen {
	BasicEditField basicEditField;
	ContactsNumber contactsNumber;
	ContactsDeleter contactsDeleter;
	ContactsCreator contactsCreator;
	
	public TestContactsCreatorScreen() {
		super( MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR );
		setTitle( "Test Contacts Creator" );

        basicEditField = new BasicEditField( "To create:", "10", 100, BasicEditField.EDITABLE );
        add( basicEditField );
        
        contactsNumber = new ContactsNumber();
        contactsDeleter = new ContactsDeleter();
        contactsCreator = new ContactsCreator();
        
        ButtonField getNumberButton = new ButtonField( "Get number", ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
        add( getNumberButton );
        getNumberButton.setChangeListener( new FieldChangeListener() {
            public void fieldChanged( Field arg0, int arg1 ) {
                getNumber();
            }
        } );
        
        ButtonField deleteAllButton = new ButtonField( "Delete all", ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
        add( deleteAllButton );
        deleteAllButton.setChangeListener( new FieldChangeListener() {
            public void fieldChanged( Field arg0, int arg1 ) {
                deleteContacts();
            }
        } );

        ButtonField createButton = new ButtonField( "Create", ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT );
        add( createButton );
        createButton.setChangeListener( new FieldChangeListener() {
            public void fieldChanged( Field arg0, int arg1 ) {
            	createContacts();
            }
        } ); 
	}
	
	protected boolean onSavePrompt() {
		return true;
	}
	
	private void getNumber() {
    	final SimplePopup myPopup = new SimplePopup("Getting number...");

    	Thread getThreaded  = new Thread() {
    		public void run() {
                // First, display this screen
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().pushScreen(myPopup);
                    }
                });
                
                contactsNumber.run();
                
                // Now dismiss this screen
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().popScreen(myPopup);
                        Dialog.inform( "Contacts: " + Integer.toString(contactsNumber.number) );
                    }
                });                
    		};
    	};
    	getThreaded.start();		
	}
	
	private void deleteContacts() {
    	final SimplePopup myPopup = new SimplePopup("Deleting...");

    	Thread deleteThreaded  = new Thread() {
    		public void run() {
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().pushScreen(myPopup);
                    }
                });
                
                contactsDeleter.run();
                
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().popScreen(myPopup);
                        Dialog.inform( "Deleted: " + Integer.toString(contactsDeleter.deleted) );
                    }
                });                
    		};
    	};
    	deleteThreaded.start();		
	}

	private void createContacts() {
		int toCreate = 0;
		
		try {
			toCreate = Integer.parseInt(basicEditField.getText());
		} 
		catch (Exception e) {
			Dialog.inform( e.toString() );
			return;
		}
		
    	final SimplePopup myPopup = new SimplePopup("Creating...");
    	final int _toCreate = toCreate;

    	Thread createThreaded  = new Thread() {
    		public void run() {
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().pushScreen(myPopup);
                    }
                });
                
                contactsCreator.toCreate = _toCreate;
                contactsCreator.run();
                
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        UiApplication.getUiApplication().popScreen(myPopup);
                        Dialog.inform( "Created: " + Integer.toString(contactsCreator.createdContacts) );
                    }
                });                
    		};
    	};
    	createThreaded.start();		
	}
}
