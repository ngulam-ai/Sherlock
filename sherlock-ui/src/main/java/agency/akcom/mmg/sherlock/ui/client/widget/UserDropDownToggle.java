package agency.akcom.mmg.sherlock.ui.client.widget;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.constants.ImageType;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Span;

public class UserDropDownToggle extends Anchor {

	Image userPic = new Image();
	Span userText = new Span();

	public UserDropDownToggle() {
		super();

		setDataToggle(Toggle.DROPDOWN);
		setStyleName(Styles.DROPDOWN_TOGGLE);
		userPic.setPixelSize(20, 20);
		userPic.setType(ImageType.ROUNDED);
		add(userPic);
		add(userText);
		Span caretSpan = new Span();
		caretSpan.setStyleName(Styles.CARET);
		add(caretSpan);
	}

	public void displayUserPicture(String pictureURL) {
		userPic.setUrl(pictureURL);
	}

	public void displayUserName(String text) {
		userText.setText(text);
	}

}
