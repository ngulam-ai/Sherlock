package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspResult;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import javax.inject.Inject;
import java.util.ArrayList;

public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy>
        implements SettingsUiHandlers {


    interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {
        void displayConfig(ArrayList<DspDto> dspDtos);

        void displayConfigWithSecret(SecretIdConnectionDto secretIdConnectionDto);
        //void displayLogs(List<ImportLog> importLogs);
    }

    private final DispatchAsync dispatcher;
    private ArrayList<DspDto> dspDtos;
    private DspDto curentDsp;


    @ProxyStandard
    @NameToken(NameTokens.SETTINGS)
    interface MyProxy extends ProxyPlace<SettingsPresenter> {
    }

    private void displayConfig(ArrayList<DspDto> dspDtos) {
    }

    private void displayConfigWithSecret(SecretIdConnectionDto avazuConnection) {
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
        getView().displayConfig(dspDtos);
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
        GWT.log("OnBindEnter");
        dispatcher.execute(new GetAllDspAction(), new AsyncCallbackImpl<GetAllDspResult>() {
            @Override
            public void onSuccess(GetAllDspResult result) {
                dspDtos = result.getDspDtos();
                if (dspDtos.size()==0){
                    GWT.log("onFailure");
                    dspDtos = new ArrayList<>();
                    DspDto dspDto = new DspDto();
                    SecretIdConnectionDto connection = new SecretIdConnectionDto("Client_id", "Client_secret");
                    connection.setName("AVAZU");
                    dspDto.setTypeConnection(TypeConnection.SECRET_ID);
                    dspDto.setPartner(Partner.AVAZU);
                    dspDto.setName("Avazu");
                    ArrayList<ConfigConnectionDto> configConnectionDtos = new ArrayList<ConfigConnectionDto>();
                    configConnectionDtos.add(connection);
                    dspDto.setConfigConnectionDtos(configConnectionDtos);
                    curentDsp = dspDto;
                    GWT.log("ADD to dspDtos: " + dspDtos.add(curentDsp));
                } else {
                    GWT.log("onSuccess");
                    curentDsp = dspDtos.get(0);
                }

            }
        });
        GWT.log("OnBind Exit");
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        GWT.log("onReval");
        dspDtos.add(curentDsp);
        getView().displayConfig(dspDtos);
    }

    @Override
    public void onSaveClick(ArrayList<DspDto> dspDtos) {
        GWT.log("onSaveClickPresenter DSP:" + dspDtos.get(0).getName());
        dispatcher.execute(new ChangeDspAction(dspDtos.get(0)), new AsyncCallbackImpl<ChangeDspResult>() {
            @Override
            public void onSuccess(ChangeDspResult result) {
                ArrayList<DspDto> dspDtos = new ArrayList<>();
                dspDtos.add(result.getOutDspDto());
                GWT.log("save" + result.getOutDspDto().getName());
//                getView().displayConfig(dspDtos);
            }
        });
    }

}