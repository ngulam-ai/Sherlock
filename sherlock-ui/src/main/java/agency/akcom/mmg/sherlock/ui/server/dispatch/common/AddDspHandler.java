package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.AddDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.AddDspResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.DspDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class AddDspHandler extends MyAbstractActionHandler<AddDspAction, AddDspResult> {
    public AddDspHandler() {
        super(AddDspAction.class);
    }

    @Override
    public AddDspResult execute(AddDspAction action, ExecutionContext context) throws ActionException {
        String name = action.getName();
        Partner partner = Partner.valueOf(action.getPartnerName());
        TypeConnection typeConnection = TypeConnection.valueOf(action.getTypeConnection());

        DspDao dspDao = new DspDao();
        Dsp dsp = dspDao.saveAndReturn(new Dsp(partner, name, typeConnection));
        DspDto dspDto = new DspDto();
        dspDto.setAttributes(dsp.getPartner(), dsp.getName(), typeConnection);

        return new AddDspResult(dspDto);
    }
}
