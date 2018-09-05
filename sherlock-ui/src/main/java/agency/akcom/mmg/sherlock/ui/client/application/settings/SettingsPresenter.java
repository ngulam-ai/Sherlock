package agency.akcom.mmg.sherlock.ui.client.application.settings;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;

public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy>
		implements SettingsUiHandlers {
	interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {

		//void displayLogs(List<ImportLog> importLogs);

	}

	@ProxyStandard
	@NameToken(NameTokens.SETTINGS)
	interface MyProxy extends ProxyPlace<SettingsPresenter> {
	}

	private final DispatchAsync dispatcher;

	@Inject
	SettingsPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

		getView().setUiHandlers(this);

		this.dispatcher = dispatcher;
	}

	@Override
	protected void onReset() {
		super.onReset();

//		dispatcher.execute(new GetImportLogAction(), new AsyncCallbackImpl<GetImportLogResult>() {
//
//			@Override
//			public void onSuccess(GetImportLogResult result) {
//				getView().displayLogs(result.getLogs());
//
//			}
//		});

	}
}