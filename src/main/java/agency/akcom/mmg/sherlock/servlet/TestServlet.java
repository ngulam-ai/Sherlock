package agency.akcom.mmg.sherlock.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.Analytics.Data.Ga.Get;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			Analytics analytics = initializeAnalytics();
			GaData gaData = getResults(analytics);
			print(gaData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private Analytics initializeAnalytics() throws Exception {
		// Initializes an authorized analytics service object.
		// Construct a GoogleCredential object with the service account email
		// and p12 file downloaded from the developer console.

		final String APPLICATION_NAME = "Test Analytics";
		final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
		final String KEY_FILE_LOCATION = "C:\\Users\\User\\Desktop\\Sherlock\\src\\main\\java\\agency\\akcom\\mmg\\sherlock\\servlet\\client_secrets.p12";
		final String SERVICE_ACCOUNT_EMAIL = "app-engine-upwork-danil-accoun@sherlock-184721.iam.gserviceaccount.com";

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(JSON_FACTORY).setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountPrivateKeyFromP12File(new File(KEY_FILE_LOCATION))
				.setServiceAccountScopes(AnalyticsScopes.all()).build();

		// Construct the Analytics service object.
		return new Analytics.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	private static GaData getResults(Analytics analytics) throws IOException {
		String tableId = "ga:120729299";
		Get apiQuery = analytics.data().ga().get(tableId, // Table Id.
				"2017-05-01", // Start date.
				"2017-12-24", // End date.
				"ga:sessions") // Metrics.
				.setDimensions("ga:source,ga:keyword")
				.setSort("-ga:sessions,ga:source")
				.setFilters("ga:medium==organic")
				.setMaxResults(25);
		return apiQuery.execute();
	}

	private void print(GaData gaData) {
		System.out.println("Column Headers:");

		for (ColumnHeaders header : gaData.getColumnHeaders()) {
			System.out.println("Column Name: " + header.getName());
			System.out.println("Column Type: " + header.getColumnType());
			System.out.println("Column Data Type: " + header.getDataType());
		}
		System.out.println();

		if (gaData.getTotalResults() > 0) {
			System.out.println("Data Table:");

			// Print the column names.
			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				System.out.format("%-32s", header.getName() + '(' + header.getDataType() + ')');
			}
			System.out.println();

			// Print the rows of data.
			for (List<String> rowValues : gaData.getRows()) {
				for (String value : rowValues) {
					System.out.format("%-32s", value);
				}
				System.out.println();
			}
		} else {
			System.out.println("No Results Found");
		}

		System.out.println();
		System.out.println("Contains Sampled Data: " + gaData.getContainsSampledData());
		System.out.println("Kind: " + gaData.getKind());
		System.out.println("ID:" + gaData.getId());
		System.out.println("Self link: " + gaData.getSelfLink());
	}
}
