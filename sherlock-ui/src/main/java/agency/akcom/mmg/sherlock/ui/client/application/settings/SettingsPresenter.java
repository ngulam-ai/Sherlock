package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.client.security.IsLoggedInGatekeeper;
import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import javax.inject.Inject;
import java.util.ArrayList;

public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy>
		implements SettingsUiHandlers {

	private final DispatchAsync dispatcher;
	private ArrayList<DspDto> dspDtos;

	interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
		void displayConfig(DspDto dspDto);
		void displayConfigWithSecret(AvazuConnectionDto avazuConnectionDto);
		//void displayLogs(List<ImportLog> importLogs);
	}


	@ProxyStandard
	@NameToken(NameTokens.SETTINGS)
	interface MyProxy extends ProxyPlace<SettingsPresenter> {
	}

	private void displayConfig(DspDto dsp) {
	}

	private void displayConfigWithSecret(AvazuConnectionDto avazuConnection) {
	}

	@Inject
	SettingsPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
		this.dispatcher = dispatcher;
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReset() {
		super.onReset();
//		dispatcher.execute(new GetImportLogAction(), new AsyncCallbackImpl<GetImportLogResult>() {
//			@Override
//			public void onSuccess(GetImportLogResult result) {
//				getView().displayLogs(result.getLogs());
//
//			}
//		});
	}

	@Override
	protected void onBind() {
		super.onBind();
		dspDtos = new ArrayList<>();
		DspDto dspDto = new DspDto();
		AvazuConnectionDto connection = new AvazuConnectionDto("Client_id","Client_secret");
		dspDto.setPartner(Partner.AVAZU);
		dspDto.setName("Avazu");
		ArrayList<ConfigConnectionDto> configConnectionDtos = new ArrayList<ConfigConnectionDto>();
		configConnectionDtos.add(connection);
		dspDto.setConfigConnectionDtos(configConnectionDtos);
		dspDtos.add(dspDto);
	}


	@Override
	protected void onReveal() {
		super.onReveal();
		getView().displayConfig(dspDtos.get(0));
	}

	@Override
	public void onSaveClick(DspDto dspDto) {
		Window.alert(dspDto.getName());
	}

}