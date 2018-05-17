package agency.akcom.mmg.sherlock.ui.server.auth;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.gson.Gson;

import agency.akcom.mmg.sherlock.ui.domain.AppUser;
import agency.akcom.mmg.sherlock.ui.domain.Customer;
import agency.akcom.mmg.sherlock.ui.server.ServerUtils;
import agency.akcom.mmg.sherlock.ui.server.dao.AppUserDao;
import agency.akcom.mmg.sherlock.ui.server.dao.CustomerDao;
import agency.akcom.mmg.sherlock.ui.shared.TooManyResultsException;

/**
 * Abstract servlet that sets up credentials and provides some convenience methods.
 */
@SuppressWarnings("serial")
public abstract class AbstractAuthServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(AbstractAuthServlet.class.getName());

	@Inject
	protected CurrentAppUserProvider currentAppUserProvider;

	/**
	 * Initializes the Servlet.
	 */
	@Override
	public void init() throws ServletException {
		super.init();
	}

	/**
	 * Dumps the given object as JSON and responds with given HTTP status code.
	 * 
	 * @param resp
	 *            Response object.
	 * @param code
	 *            HTTP status code to respond with.
	 * @param obj
	 *            An object to be dumped as JSON.
	 */
	protected void sendJson(HttpServletResponse resp, int code, Object obj) {
		try {
			// TODO(burcud): Initialize Gson instance for once.
			resp.setContentType("application/json");
			resp.getWriter().print(new Gson().toJson(obj).toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dumps the given object to JSON and responds with HTTP 200.
	 * 
	 * @param resp
	 *            Response object.
	 * @param obj
	 *            An object to be dumped as JSON.
	 */
	protected void sendJson(HttpServletResponse resp, Object obj) {
		sendJson(resp, 200, obj);
	}

	/**
	 * Responds with the given HTTP status code and message.
	 * 
	 * @param resp
	 *            Response object.
	 * @param code
	 *            HTTP status code to respond with.
	 * @param message
	 *            Message body.
	 */
	protected void sendError(HttpServletResponse resp, int code, String message) {
		try {
			resp.sendError(code, message);
		} catch (IOException e) {
			throw new RuntimeException(message);
		}
	}

	/**
	 * Transforms a GoogleJsonResponseException to an HTTP response.
	 * 
	 * @param resp
	 *            Response object.
	 * @param e
	 *            Exception object to transform.
	 */
	protected void sendGoogleJsonResponseError(HttpServletResponse resp, GoogleJsonResponseException e) {
		sendError(resp, e.getStatusCode(), e.getLocalizedMessage());
	}

	/**
	 * Redirects to OAuth2 consent page if user is not logged in.
	 * 
	 * @param resp
	 *            Response object.
	 * @throws IOException
	 */
	protected void loginIfRequired(HttpServletResponse resp) throws IOException {
		Credential credential = CredentialManager.getCredential(getUserId());

		if (credential == null || credential.getRefreshToken() == null) {
			// redirect to authorization url
			try {
				boolean needToForce = credential != null && credential.getRefreshToken() == null;
				resp.sendRedirect(CredentialManager.getAuthorizationUrl(needToForce));
			} catch (IOException e) {
				throw new RuntimeException("Can't redirect to auth page");
			}
		} else {
			resp.sendRedirect("/"); // + ServerUtils.getDevSuffix());
		}
	}

	/**
	 * If OAuth2 redirect callback is invoked and there is a code query param, retrieve user credentials and profile.
	 * Then, redirect to the home page.
	 * 
	 * @param req
	 *            Request object.
	 * @param resp
	 *            Response object.
	 * @throws IOException
	 */
	protected void handleCallbackIfRequired(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String code = req.getParameter("code");
		if (code != null) {
			// retrieve new credentials with code
			Credential credential = CredentialManager.retrieveCredential(code);
			// request userinfo
			Oauth2 outh2Service = ServerUtils.getOauth2Service(credential);

			try {
				Userinfoplus about = outh2Service.userinfo().get().execute();

				// ---
				String email = about.getEmail();
				AppUserDao appUserDao = new AppUserDao();
				AppUser appUser = appUserDao.findByEmail(email);

				if (appUser == null) { // new user -> sign up him

					// sendError(resp, resp.SC_FORBIDDEN, "User with email " + email +
					// " not registered in application");
					// Analytics analyticsService = ServerUtils.getAnalyticsService(credential);

					CustomerDao customerDao = new CustomerDao();
					Customer customer = new Customer();
					customer.setEmail(email);
					customer.setPictureURL(about.getPicture());
					customer.setName(about.getName());
					customer.setDescription("initially signed up with Google");

					// we should use logged in user credentials to access Analytics data
					// customer.setGaOnBehalfOfApp(false);

					// TODO implement ability to select desired Id
					// TODO probably we have to store new user information even if he don't have Analytics account yet
					try {
						// customer.setGaProfileId(getFirstProfileId(analyticsService));

						customer = customerDao.saveAndReturn(customer);
						log.info("new Cuctomer saved:  " + customer.getName());
						appUser = customer.getUser();
						log.info("newly created User: " + appUser + " " + email);

					} catch (Exception e) {
						// if (e.getMessage().contains("403"))
						appUser = null;
						sendError(resp, HttpServletResponse.SC_FORBIDDEN, "Error for user with email " + email + " : "
								+ e.getMessage());
					}
				}

				if (appUser != null) {
					currentAppUserProvider.set(appUser);

					// TODO update user attributes (name, pictureURL, etc.) from "about"

					// test stored and received credentials and if there is no refresh tokens, then force
					// authentication in order to re-get refresh token
					if (credential.getRefreshToken() == null) {
						// test stored credential
						Credential storedCredential = CredentialManager.getCredential(getUserId());
						if (storedCredential == null || storedCredential.getRefreshToken() == null)
							// try to force in order to re-get refresh token
							resp.sendRedirect(CredentialManager.getAuthorizationUrl(true));
					}

					CredentialManager.saveCredential(getUserId(), credential);
				}

			} catch (IOException e) {
				throw new RuntimeException("Can't handle the OAuth2 callback, " + "make sure that code is valid.");
			} catch (TooManyResultsException e) {
				throw new RuntimeException("Duplicated USERS with the same email" + e.getMessage());
			}

			if (!resp.isCommitted())
				resp.sendRedirect("/"); // + (ServerUtils.isProduction() ? "" : "?gwt.codesvr=127.0.0.1:9997"));
		}
	}

	private String getUserId() {
		AppUser appUser = currentAppUserProvider.get();

		return appUser != null ? appUser.getId().toString() : null;
	}

}
