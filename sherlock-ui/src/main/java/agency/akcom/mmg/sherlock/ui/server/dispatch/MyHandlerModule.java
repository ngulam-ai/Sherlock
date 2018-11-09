package agency.akcom.mmg.sherlock.ui.server.dispatch;

import agency.akcom.mmg.sherlock.ui.server.dispatch.common.*;
import agency.akcom.mmg.sherlock.ui.shared.action.*;
import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;

import agency.akcom.mmg.sherlock.ui.server.dispatch.auth.GetCurrentUserHandler;
import agency.akcom.mmg.sherlock.ui.server.dispatch.auth.SignOutHandler;
import agency.akcom.mmg.sherlock.ui.server.dispatch.validator.AdminActionValidator;
import agency.akcom.mmg.sherlock.ui.server.dispatch.validator.LoggedInActionValidator;

public class MyHandlerModule extends HandlerModule {
	@Override
	protected void configureHandlers() {

		// Bind Action to Action Handler
		bindHandler(GetCurrentUserAction.class, GetCurrentUserHandler.class);
		bindHandler(SignOutAction.class, SignOutHandler.class);
		
		// ADMIN action handles (with validation)
		bindHandler(GetUsersAction.class, GetUsersHandler.class, AdminActionValidator.class);
		bindHandler(DeleteUserAction.class, DeleteUserHandler.class, AdminActionValidator.class);
		bindHandler(SetUserAdminAction.class, SetUserAdminHandler.class, AdminActionValidator.class);
		
		bindHandler(GetImportLogAction.class, GetImportLogHandler.class, LoggedInActionValidator.class);
		bindHandler(AddDspAction.class, AddDspHandler.class);
	}
}
