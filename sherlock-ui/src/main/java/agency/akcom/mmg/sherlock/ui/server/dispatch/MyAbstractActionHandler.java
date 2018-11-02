package agency.akcom.mmg.sherlock.ui.server.dispatch;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.rpc.shared.Action;
import com.gwtplatform.dispatch.rpc.shared.Result;
import com.gwtplatform.dispatch.shared.ActionException;

public abstract class MyAbstractActionHandler<A extends Action<R>, R extends Result> extends
		AbstractActionHandler<A, R> {

	public MyAbstractActionHandler(Class<A> actionType) {
		super(actionType);
	}

	@Override
	public void undo(A action, R result, ExecutionContext context) throws ActionException {
	}
}
