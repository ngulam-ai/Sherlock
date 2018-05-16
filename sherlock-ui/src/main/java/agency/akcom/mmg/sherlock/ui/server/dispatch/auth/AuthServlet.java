package agency.akcom.mmg.sherlock.ui.server.dispatch.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AuthServlet extends AbstractAuthServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// handle OAuth2 callback
		handleCallbackIfRequired(req, resp);

		if (!resp.isCommitted())
			// Making sure that we have user credentials
			loginIfRequired(resp);

	}
}
	