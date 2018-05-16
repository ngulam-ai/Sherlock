package agency.akcom.mmg.sherlock.ui.server.dispatch;

import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;

import agency.akcom.mmg.sherlock.ui.server.dispatch.auth.SignOutHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.SignOutAction;

public class MyHandlerModule extends HandlerModule {
	@Override
	protected void configureHandlers() {

		// Bind Action to Action Handler
		//bindHandler(GetCurrentUserAction.class, GetCurrentUserHandler.class);
		bindHandler(SignOutAction.class, SignOutHandler.class);

		// ADMIN action handles (with validation)
		// bindHandler(GetUsersAction.class, GetUsersHandler.class,
		// AdminActionValidator.class);
		// bindHandler(DeleteUserAction.class, DeleteUserHandler.class,
		// AdminActionValidator.class);
		// bindHandler(SetUserAdminAction.class, SetUserAdminHandler.class,
		// AdminActionValidator.class);

	}
}
