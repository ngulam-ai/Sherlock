package agency.akcom.mmg.sherlock.ui.server.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.services.oauth2.Oauth2Scopes;

import agency.akcom.mmg.sherlock.ui.server.ServerUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Credential manager to get, saveStorage, deleteStorage user credentials.
 * 
 */
@Slf4j
public class CredentialManager {

	/**
	 * Default transportation layer for Google Apis Java client.
	 */
	public static final HttpTransport TRANSPORT = new NetHttpTransport();

	/**
	 * Default JSON factory for Google Apis Java client.
	 */
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/**
	 * Path component under war/ to locate client_secrets.json file.
	 */
	public static final String CLIENT_SECRETS_FILE_PRODUCTION_PATH = "WEB-INF/resources/client_secrets.json";
	public static final String CLIENT_SECRETS_FILE_DEVELOPMENT_PATH = "WEB-INF/resources/client_secrets_dev.json";

	/**
	 * Scopes for which to request access from the user.
	 */
	public static final List<String> SCOPES = Arrays.asList(
	// Required to identify the user in our data store.
			"openid", Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE);

	/**
	 * Client secrets object.
	 */
	private static GoogleClientSecrets clientSecrets;

	/**
	 * Credential store to get, saveStorage, deleteStorage user credentials.
	 */
	private static AppEngineDataStoreFactory appEngineDataStoreFactory = new AppEngineDataStoreFactory();
	private static DataStore<StoredCredential> credentialStore;

	static {
		try {
			clientSecrets = getClientSecrets();
			credentialStore = appEngineDataStoreFactory.getDataStore(StoredCredential.DEFAULT_DATA_STORE_ID);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Builds an empty credential object.
	 * 
	 * @return An empty credential object.
	 */
	private static Credential buildEmpty() {

		return new GoogleCredential.Builder().setClientSecrets(clientSecrets).setTransport(TRANSPORT)
				.setJsonFactory(JSON_FACTORY).build();
	}

	public static Credential getCredential(Long id) throws IOException {
		if (id == null)
			return null;

		return getCredential(id.toString());
	}

	/**
	 * Returns credentials of the given user, returns null if there are none.
	 * 
	 * @param id
	 *            The id of the user.
	 * @return A credential object or null.
	 * @throws IOException
	 */
	public static Credential getCredential(String id) throws IOException {

		if (id == null)
			return null;

		StoredCredential storedCredential = credentialStore.get(id);

		if (storedCredential == null)
			return null;

		return new GoogleCredential.Builder().setTransport(TRANSPORT).setJsonFactory(JSON_FACTORY)
				.setClientSecrets(clientSecrets)
				.addRefreshListener(new DataStoreCredentialRefreshListener(id, credentialStore)).build()
				.setAccessToken(storedCredential.getAccessToken()).setRefreshToken(storedCredential.getRefreshToken())
				.setExpirationTimeMilliseconds(storedCredential.getExpirationTimeMilliseconds());
	}

	/**
	 * Saves credentials of the given user.
	 * 
	 * @param userId
	 *            The id of the user.
	 * @param credential
	 *            A credential object to saveStorage.
	 * @throws IOException
	 */
	public static void saveCredential(String userId, Credential credential) throws IOException {
		StoredCredential storedCredential = credentialStore.get(userId);

		if (storedCredential == null)
			storedCredential = new StoredCredential(credential);
		else {
			storedCredential.setAccessToken(credential.getAccessToken());
			storedCredential.setExpirationTimeMilliseconds(credential.getExpirationTimeMilliseconds());

			if (credential.getRefreshToken() != null)
				storedCredential.setRefreshToken(credential.getRefreshToken());
		}

		credentialStore.set(userId, storedCredential);
	}

	/**
	 * Deletes credentials of the given user.
	 * 
	 * @param userId
	 *            The id of the user.
	 * @throws IOException
	 */
	public void deleteCredential(String userId) throws IOException {
		credentialStore.delete(userId);
	}

	/**
	 * Generates a consent page url.
	 * 
	 * @param needToForce
	 * 
	 * @return A consent page url string for user redirection.
	 */
	public static String getAuthorizationUrl(boolean needToForce) {
		GoogleAuthorizationCodeRequestUrl urlBuilder = new GoogleAuthorizationCodeRequestUrl(clientSecrets.getWeb()
				.getClientId(), clientSecrets.getWeb().getRedirectUris().get(0), SCOPES).setApprovalPrompt(
				needToForce ? "force" : "auto").setAccessType("offline");
		return urlBuilder.build();
	}

	/**
	 * Retrieves a new access token by exchanging the given code with OAuth2 end-points.
	 * 
	 * @param code
	 *            Exchange code.
	 * @return A credential object.
	 */
	public static Credential retrieveCredential(String code) {
		try {
			GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY,
					clientSecrets.getWeb().getClientId(), clientSecrets.getWeb().getClientSecret(), code, clientSecrets
							.getWeb().getRedirectUris().get(0)).execute();

			Credential credential = buildEmpty().setAccessToken(response.getAccessToken())
					.setExpiresInSeconds(response.getExpiresInSeconds()).setRefreshToken(response.getRefreshToken());

			// if (response.getRefreshToken() != null)
			// credential.setRefreshToken(response.getRefreshToken());

			return credential;

		} catch (IOException e) {
			new RuntimeException("An unknown problem occured while retrieving token");
		}
		return null;
	}

	/**
	 * Reads client_secrets.json and creates a GoogleClientSecrets object.
	 * 
	 * @return A GoogleClientsSecrets object.
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	private static GoogleClientSecrets getClientSecrets() throws URISyntaxException, FileNotFoundException {
		// TODO do not read on each request
		String fileName = ServerUtils.isProduction() ? CLIENT_SECRETS_FILE_PRODUCTION_PATH
				: CLIENT_SECRETS_FILE_DEVELOPMENT_PATH;
		
//		File curDir = new File("target");
//		File[] files = curDir.listFiles();
//		for (File file: files) {
//		  log.info(file.getName());
//		}
		
//		log.info(fileName);
		
		File file = new File(fileName);	
		
		FileInputStream fileInputStream = new FileInputStream(file);
		
		InputStreamReader streamReader = new InputStreamReader(fileInputStream);

		try {
			return GoogleClientSecrets.load(JSON_FACTORY, streamReader);
		} catch (IOException e) {
			throw new RuntimeException("No client_secrets.json found");
		}
	}

}
