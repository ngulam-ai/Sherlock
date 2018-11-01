package agency.akcom.mmg.sherlock.ui.client.application.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.AvazuConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.TypeConnection;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy> {
	interface MyView extends View {
		void displayConfig(Dsp dsp);
		void displayConfigWithSecret(AvazuConnection avazuConnection);
	}

//	private final DispatchAsync dispatchAsync;
	@ProxyStandard
	@NameToken(NameTokens.HOME)
	interface MyProxy extends ProxyPlace<HomePagePresenter> {
		
	}
	
	private void displayConfig(Dsp dsp) {
		
	}
	
	private void displayConfigWithSecret(AvazuConnection avazuConnection) {
		
	}
	/*private void displayDsp(ArrayList<Dsp> dsps) {
		dispatchAsync.execute(new )
		for(Dsp curentDsp:dsps) {
			Partner partner = curentDsp.getPartner() ;
			String name = curentDsp.getName();
			getView().b  
		}
	}
*/
	@Inject
	HomePagePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
	}
}