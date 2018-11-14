package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.DeleteDspAction;
import agency.akcom.mmg.sherlock.ui.shared.action.DeleteDspResult;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteDspHandler extends MyAbstractActionHandler<DeleteDspAction, DeleteDspResult> {
    public DeleteDspHandler() {
        super(DeleteDspAction.class);
    }

    @Override
    public DeleteDspResult execute(DeleteDspAction action, ExecutionContext context) throws ActionException {
        new DspDao().delete(action.getId());
        return new DeleteDspResult();
    }

}
