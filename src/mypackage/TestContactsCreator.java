package mypackage;

import net.rim.device.api.ui.UiApplication;

public class TestContactsCreator extends UiApplication {
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args){
    	TestContactsCreator theApp = new TestContactsCreator();
    	theApp.enterEventDispatcher();
    }
    
    public TestContactsCreator() {
    	pushScreen(new TestContactsCreatorScreen());
    }
    
}
