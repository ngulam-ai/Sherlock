
package agency.akcom.mmg.sherlock.ui.client.application.settings.widget;

import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetAllDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.AvazuConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import java.util.ArrayList;

public class ConfigConnectionPresenter extends PresenterWidget<ConfigConnectionPresenter.MyView> implements ConfigConnectionUiHandlers {
    interface MyView extends View, HasUiHandlers<ConfigConnectionUiHandlers> {
        void displayConfig(ArrayList<DspDto> dspDtos);
        void displayConfigWithSecret(AvazuConnectionDto avazuConnectionDto);
    }

    private final DispatchAsync dispatcher;
    private ArrayList<DspDto> dspDtos;
    private DspDto curentDsp;
    private AvazuConnectionDto curentAvazuConfigConnection;

    @Inject
    ConfigConnectionPresenter(final EventBus eventBus, final MyView view, final DispatchAsync dispatcher) {
        super(eventBus, view);
        this.dispatcher = dispatcher;
//        getView().setUiHandlers(this);
    }

    private void displayConfig(ArrayList<DspDto> dspDtos) {
    }

    private void displayConfigWithSecret(AvazuConnectionDto avazuConnection) {
    }

    @Override
    protected void onBind() {
        GWT.log("OnBindEnter");
        dispatcher.execute(new GetAllDspAction(), new AsyncCallbackImpl<GetAllDspResult>() {
            @Override
            public void onSuccess(GetAllDspResult result) {
                dspDtos = result.getDspDtos();
                if (dspDtos.size()==0){
                    GWT.log("onFailure");
                    dspDtos = new ArrayList<>();
                    DspDto dspDto = new DspDto();
                    AvazuConnectionDto connection = new AvazuConnectionDto("Client_id", "Client_secret");
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
}
