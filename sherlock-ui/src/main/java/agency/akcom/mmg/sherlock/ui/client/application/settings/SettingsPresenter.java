package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.ChangeDspResult;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.TokenConnectionDto;
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
    }

    private final DispatchAsync dispatcher;
    private ArrayList<DspDto> dspDtos;


    @ProxyStandard
    @NameToken(NameTokens.SETTINGS)
    interface MyProxy extends ProxyPlace<SettingsPresenter> {
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
        GWT.log("onReset");
    }

    @Override
    protected void onBind() {
        super.onBind();

        GWT.log("OnBindEnter");
        dispatcher.execute(new GetAllDspAction(), new AsyncCallbackImpl<GetAllDspResult>() {
            @Override
            public void onSuccess(GetAllDspResult result) {
                GWT.log("onSuccess onBind");
                if (result.getDspDtos() != null && result.getDspDtos().size() > 0) {
                    dspDtos = result.getDspDtos();
                    getView().displayConfig(dspDtos);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                GWT.log("onFailure");
            }
        });

        if (dspDtos == null) {
            GWT.log("create AVAZU");
            dspDtos = new ArrayList<>();

            DspDto dspDto = new DspDto();
            SecretIdConnectionDto secretIdConnectionDto = new SecretIdConnectionDto("Client_id", "Client_secret");
            secretIdConnectionDto.setName("AVAZU");
            dspDto.setTypeConnection(TypeConnection.SECRET_ID);
            dspDto.setPartner(Partner.AVAZU);
            dspDto.setName("Avazu");
            ArrayList<ConfigConnectionDto> configConnectionDtos = new ArrayList<ConfigConnectionDto>();
            configConnectionDtos.add(secretIdConnectionDto);
            dspDto.setConfigConnectionDtos(configConnectionDtos);
            dspDtos.add(dspDto);

            GWT.log("create POCKETMATH");
            DspDto dspDtoPocketMath = new DspDto();
            TokenConnectionDto tokenConnectionDto = new TokenConnectionDto();
            tokenConnectionDto.setName("POCKETMATH_NAME");
            tokenConnectionDto.setToken("POCKETMATH_TOKEN");
            dspDtoPocketMath.setTypeConnection(TypeConnection.TOKEN);
            dspDtoPocketMath.setPartner(Partner.POCKETMATH);
            dspDtoPocketMath.setName("POCKETMATH");
            ArrayList<ConfigConnectionDto> configConnectionDtosPocketMath = new ArrayList<ConfigConnectionDto>();
            configConnectionDtosPocketMath.add(tokenConnectionDto);
            dspDtoPocketMath.setConfigConnectionDtos(configConnectionDtosPocketMath);
            dspDtos.add(dspDtoPocketMath);
        }
        GWT.log("OnBind Exit");
        getView().displayConfig(dspDtos);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        GWT.log("onReval");
        getView().displayConfig(dspDtos);
    }

    @Override
    public void onSaveClick(DspDto dspDto) {
        dispatcher.execute(new ChangeDspAction(dspDto), new AsyncCallbackImpl<ChangeDspResult>() {
            @Override
            public void onSuccess(ChangeDspResult result) {
                int indexDsp = dspDtos.indexOf(dspDto);
                dspDtos.remove(indexDsp);
                dspDtos.add(indexDsp, result.getOutDspDto());
            }
        });
    }

}