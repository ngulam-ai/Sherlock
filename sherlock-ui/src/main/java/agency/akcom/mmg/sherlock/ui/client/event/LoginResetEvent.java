package agency.akcom.mmg.sherlock.ui.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class LoginResetEvent extends GwtEvent<LoginResetEvent.LoginResetHandler> {

	public static Type<LoginResetHandler> TYPE = new Type<LoginResetHandler>();

	public interface LoginResetHandler extends EventHandler {
		void onLoginReset(LoginResetEvent event);
	}

	public LoginResetEvent() {
	}

	@Override
	protected void dispatch(LoginResetHandler handler) {
		handler.onLoginReset(this);
	}

	@Override
	public Type<LoginResetHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoginResetHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoginResetEvent());
	}
}
