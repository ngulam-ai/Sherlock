package agency.akcom.mmg.sherlock.ui.client.application.log;

import java.util.List;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.client.security.IsLoggedInGatekeeper;
import agency.akcom.mmg.sherlock.ui.shared.action.GetImportLogAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetImportLogResult;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;

public class LogPresenter extends Presenter<LogPresenter.MyView, LogPresenter.MyProxy> implements LogUiHandlers {
	interface MyView extends View, HasUiHandlers<LogUiHandlers> {

		void displayLogs(List<ImportLog> importLogs);

	}

	@ProxyStandard
	@NameToken(NameTokens.LOG)
	@UseGatekeeper(IsLoggedInGatekeeper.class)
	interface MyProxy extends ProxyPlace<LogPresenter> {
	}

	private final DispatchAsync dispatcher;

	@Inject
	LogPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

		getView().setUiHandlers(this);

		this.dispatcher = dispatcher;
	}

	@Override
	protected void onReset() {
		super.onReset();

		dispatcher.execute(new GetImportLogAction(), new AsyncCallbackImpl<GetImportLogResult>() {

			@Override
			public void onSuccess(GetImportLogResult result) {
				getView().displayLogs(result.getLogs());

			}
		});

	}
}