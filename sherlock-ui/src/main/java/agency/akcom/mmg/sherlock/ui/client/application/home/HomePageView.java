package agency.akcom.mmg.sherlock.ui.client.application.home;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;

class HomePageView extends ViewWithUiHandlers<HomeUiHandler> implements HomePagePresenter.MyView {
	interface Binder extends UiBinder<Widget, HomePageView> {
	}

	@Inject
	HomePageView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}