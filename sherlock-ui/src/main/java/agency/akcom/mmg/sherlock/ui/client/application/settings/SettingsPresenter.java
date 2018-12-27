package agency.akcom.mmg.sherlock.ui.client.application.settings;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.client.widget.ExtRow;
import agency.akcom.mmg.sherlock.ui.shared.action.*;
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
    }

    @Override
    protected void onBind() {
        super.onBind();

        GWT.log("OnBindEnter");
        dispatcher.execute(new GetAllDspAction(), new AsyncCallbackImpl<GetAllDspResult>() {
            @Override
            public void onSuccess(GetAllDspResult result) {
                if (result.getDspDtos() != null && result.getDspDtos().size() > 0) {
                    dspDtos = result.getDspDtos();
                    getView().displayConfig(dspDtos);
                }
            }
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }
        });
        checkAndAddDefaultDsp();
        getView().displayConfig(dspDtos);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        GWT.log("onReval");
        checkAndAddDefaultDsp();
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

    @Override
    public void CheckConfigConnections(ExtRow curentRow) {
        dispatcher.execute(new CheckConfigConnectionsAction(curentRow.getPartner(), curentRow.getCurentConnection()), new AsyncCallbackImpl<CheckConfigConnectionsResult>() {
            @Override
            public void onSuccess(CheckConfigConnectionsResult result) {
                curentRow.setResultCheckConncetion(result.getResult());
            }
             });
    }

    private void checkAndAddDefaultDsp() {
        if (dspDtos == null) {
            dspDtos = new ArrayList<>();
            dspDtos.add(getDefaultAvazu());
            dspDtos.add(getDefaultPocketmath());
        } else {
            if (dspDtos.size() == 1) {
                if (dspDtos.get(0).getPartner() == Partner.AVAZU) {
                    dspDtos.add(getDefaultPocketmath());
                } else {
                    dspDtos.add(getDefaultAvazu());
                }
            }
        }
        checkAndAddDefaultConfigConnections();
    }

    private void checkAndAddDefaultConfigConnections(){
        for(DspDto curent:dspDtos) {
            switch (curent.getTypeConnection()){
                case SECRET_ID:{
                    if (curent.getConfigConnectionDtos().size()==0){
                        curent.setConfigConnectionDtos(getDefaultSecretIdConfigConnections());
                    }
                    break;
                }
                case TOKEN:{
                    if (curent.getConfigConnectionDtos().size()==0){
                        curent.setConfigConnectionDtos(getDefaultTokenConfigConnections());
                    }
                    break;
                }
            }
        }
    }

    private DspDto getDefaultAvazu() {
        GWT.log("create AVAZU");
        DspDto result = new DspDto();
        result.setTypeConnection(TypeConnection.SECRET_ID);
        result.setPartner(Partner.AVAZU);
        result.setName("AVAZU");
        result.setConfigConnectionDtos(getDefaultSecretIdConfigConnections());
        return result;
    }

    private ArrayList<ConfigConnectionDto> getDefaultSecretIdConfigConnections(){
        ArrayList<ConfigConnectionDto> result = new ArrayList<ConfigConnectionDto>();
        SecretIdConnectionDto secretIdConnectionDto = new SecretIdConnectionDto("Client_id", "Client_secret");
        secretIdConnectionDto.setName("Name");
        result.add(secretIdConnectionDto);
        return result;
    }

    private DspDto getDefaultPocketmath() {
        GWT.log("create POCKETMATH");
        DspDto result = new DspDto();

        result.setTypeConnection(TypeConnection.TOKEN);
        result.setPartner(Partner.POCKETMATH);
        result.setName("POCKETMATH");
        result.setConfigConnectionDtos(getDefaultTokenConfigConnections());
        return result;
    }

    private ArrayList<ConfigConnectionDto> getDefaultTokenConfigConnections(){
        ArrayList<ConfigConnectionDto> result = new ArrayList<ConfigConnectionDto>();
        TokenConnectionDto tokenConnectionDto = new TokenConnectionDto();
        tokenConnectionDto.setName("Name");
        tokenConnectionDto.setToken("Token");
        result.add(tokenConnectionDto);
        return result;
    }

}