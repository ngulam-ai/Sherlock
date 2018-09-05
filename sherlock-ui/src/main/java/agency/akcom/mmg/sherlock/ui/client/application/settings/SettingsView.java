package agency.akcom.mmg.sherlock.ui.client.application.settings;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {
	interface Binder extends UiBinder<Widget, SettingsView> {
	}

	@Inject
	SettingsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

	}

}