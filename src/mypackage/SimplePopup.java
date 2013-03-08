package mypackage;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class SimplePopup extends PopupScreen {
	public SimplePopup(String text) {
		super(new VerticalFieldManager(VerticalFieldManager.VERTICAL_SCROLL | VerticalFieldManager.VERTICAL_SCROLLBAR));
        this.add(new LabelField(text, Field.FIELD_HCENTER));
	}
}
