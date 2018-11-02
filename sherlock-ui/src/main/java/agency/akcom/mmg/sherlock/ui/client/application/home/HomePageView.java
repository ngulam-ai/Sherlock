package agency.akcom.mmg.sherlock.ui.client.application.home;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class HomePageView extends ViewImpl implements HomePagePresenter.MyView {
    interface Binder extends UiBinder<Widget, HomePageView> {
    }

    @Inject
    HomePageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}