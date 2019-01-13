package agency.akcom.mmg.sherlock.ui.server.adWords;

import agency.akcom.mmg.sherlock.ui.server.adWords.model.AdWordsResponse;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201802.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201802.cm.*;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.client.reporting.ReportingConfiguration;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.jaxb.v201802.DownloadFormat;
import com.google.api.ads.adwords.lib.jaxb.v201802.ReportDefinitionDateRangeType;
import com.google.api.ads.adwords.lib.jaxb.v201802.ReportDefinitionReportType;
import com.google.api.ads.adwords.lib.selectorfields.v201802.cm.AdGroupCriterionField;
import com.google.api.ads.adwords.lib.selectorfields.v201802.cm.AdGroupField;
import com.google.api.ads.adwords.lib.selectorfields.v201802.cm.CampaignField;
import com.google.api.ads.adwords.lib.utils.DetailedReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponse;
import com.google.api.ads.adwords.lib.utils.ReportDownloadResponseException;
import com.google.api.ads.adwords.lib.utils.ReportException;
import com.google.api.ads.adwords.lib.utils.v201802.ReportDownloaderInterface;
import com.google.api.ads.adwords.lib.utils.v201802.ReportQuery;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

@Slf4j
public class AdWordsUtils {

    private final static String REFRESH_TOKEN = "1/K6bpOlL6ZteFLKapJzYNaB8bE3t4Vhoruf1YX3AzTWA";
    private final static String CLIENT_ID = "827151708808-5gurjhjbtilcc43daggrgbeujq95lj7q.apps.googleusercontent.com";
    private final static String CLIENT_SECRET = "-sCovnORMMZDMuCiVEfGdPU6";
    private final static String CLIENT_CUSTOMER_ID = "823-850-9295";
    private final static String DEVELOPER_TOKEN = "8Vay3mxTUKWjMHjlttroRA";
    private final static boolean PARTIAL_FAILURE = false;
    private static final int PAGE_SIZE = 100;


    public boolean checkingValidCredentials() throws ReportDownloadResponseException, ReportException {
        final boolean ENABLE_VALIDATE_ONLY = true;
        boolean result = false;
        AdWordsSession session = createSesion(ENABLE_VALIDATE_ONLY);
        if (session == null) {
            return result;
        }
        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

        ReportQuery query =
                new ReportQuery.Builder()
                        .fields("CampaignId")
                        .from(ReportDefinitionReportType.CRITERIA_PERFORMANCE_REPORT)
                        .build();
        ReportDownloaderInterface reportDownloader =
                adWordsServices.getUtility(session, ReportDownloaderInterface.class);
        ReportDownloadResponse response =
                reportDownloader.downloadReport(query.toString(), DownloadFormat.XML);
        int status = response.getHttpStatus();
        if (status >= 200 && status <= 299) {
            result = true;
        }
//        InputStream stream = response.getInputStream();
//        System.out.println(convertStreamToString(stream));
        System.out.println("Status response: " + status);
        return result;
    }

    public static void main(String[] args) {
        final boolean ENABLE_VALIDATE_ONLY = false;
        AdWordsSession session = createSesion(ENABLE_VALIDATE_ONLY);
        if (session == null) {
            return;
        }

        AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

        try {
            getReport(adWordsServices, session);
            getCampaigns(adWordsServices, session);
        } catch (DetailedReportDownloadResponseException dre) {
            System.err.printf(
                    "Report was not downloaded due to a %s with errorText '%s', trigger '%s' and "
                            + "field path '%s'%n",
                    dre.getClass().getSimpleName(),
                    dre.getErrorText(),
                    dre.getTrigger(),
                    dre.getFieldPath());
        } catch (ReportDownloadResponseException rde) {
            System.err.printf("Report was not downloaded due to: %s%n", rde);
        } catch (ReportException re) {
            System.err.printf("Report was not downloaded due to transport layer exception: %s%n", re);
        } catch (IOException ioe) {
            // TODO: 13.01.2019
//            System.err.printf(
//                    "Report was not written to file %s due to an IOException: %s%n", reportFile, ioe);
        }
    }

    /**
     * @throws DetailedReportDownloadResponseException if the report request failed with a detailed
     *                                                 error from the reporting service.
     * @throws ReportDownloadResponseException         if the report request failed with a general error from
     *                                                 the reporting service.
     * @throws ReportException                         if the report request failed due to a transport layer error.
     * @throws IOException                             if the report's contents could not be written to {@code reportFile}.
     */
    public static void getReport(
            AdWordsServicesInterface adWordsServices, AdWordsSession session)
            throws ReportDownloadResponseException, ReportException, IOException {
        // Create query.
        ReportQuery query =
                new ReportQuery.Builder()
                        .fields(
                                "CampaignId",
                                "AdGroupId",
                                "Id",
                                "Criteria",
                                "CriteriaType",
                                "Impressions",
                                "Clicks",
                                "Cost")
                        .from(ReportDefinitionReportType.CRITERIA_PERFORMANCE_REPORT)
                        .where("Status").in("ENABLED", "PAUSED")
                        .during(ReportDefinitionDateRangeType.LAST_7_DAYS)
                        .build();

        ReportingConfiguration reportingConfiguration =
                new ReportingConfiguration.Builder()
                        .skipReportHeader(false)
                        .skipColumnHeader(false)
                        .skipReportSummary(false)
                        // Set to false to exclude rows with zero impressions.
                        .includeZeroImpressions(true)
                        .build();
        session.setReportingConfiguration(reportingConfiguration);

        ReportDownloaderInterface reportDownloader =
                adWordsServices.getUtility(session, ReportDownloaderInterface.class);

        ReportDownloadResponse response =
                reportDownloader.downloadReport(query.toString(), DownloadFormat.XML);
        InputStream stream = response.getInputStream();
        try {
            ArrayList<AdWordsResponse> response1 = parserXML(stream);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        System.out.println(convertStreamToString(stream));
        System.out.println("Status response: " + response.getHttpStatus());
        //todo make handler status response
    }

    static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void getCampaigns(
            AdWordsServicesInterface adWordsServices, AdWordsSession session) throws RemoteException {
        // Get the CampaignService.
        CampaignServiceInterface campaignService =
                adWordsServices.get(session, CampaignServiceInterface.class);

        int offset = 0;

        // Create selector.
        SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(CampaignField.Id, CampaignField.Name)
                .orderAscBy(CampaignField.Name)
                .offset(offset)
                .limit(PAGE_SIZE)
                .build();

        CampaignPage page;
        do {
            // Get all campaigns.
            page = campaignService.get(selector);

            // Display campaigns.
            if (page.getEntries() != null) {
                for (Campaign campaign : page.getEntries()) {
                    System.out.printf("Campaign with name '%s' and ID %d was found.%n", campaign.getName(),
                            campaign.getId());
                    getAdGroups(adWordsServices, session, campaign.getId());
                }
            } else {
                System.out.println("No campaigns were found.");
            }

            offset += PAGE_SIZE;
            selector = builder.increaseOffsetBy(PAGE_SIZE).build();
        } while (offset < page.getTotalNumEntries());
    }


    public static void getAdGroups(
            AdWordsServicesInterface adWordsServices, AdWordsSession session, Long campaignId)
            throws RemoteException {
        // Get the AdGroupService.
        AdGroupServiceInterface adGroupService =
                adWordsServices.get(session, AdGroupServiceInterface.class);

        int offset = 0;
        boolean morePages = true;

        // Create selector.
        SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(AdGroupField.Id, AdGroupField.Name)
                .orderAscBy(AdGroupField.Name)
                .offset(offset)
                .limit(PAGE_SIZE)
                .equals(AdGroupField.CampaignId, campaignId.toString())
                .build();

        while (morePages) {
            // Get all ad groups.
            AdGroupPage page = adGroupService.get(selector);

            // Display ad groups.
            if (page.getEntries() != null) {
                for (AdGroup adGroup : page.getEntries()) {
                    System.out.printf("Ad group with name '%s' and ID %d was found.%n", adGroup.getName(),
                            adGroup.getId());
                    getKeywords(adWordsServices, session, adGroup.getId());
                }
            } else {
                System.out.println("No ad groups were found.");
            }

            offset += PAGE_SIZE;
            selector = builder.increaseOffsetBy(PAGE_SIZE).build();
            morePages = offset < page.getTotalNumEntries();
        }
    }

    public static void getKeywords(
            AdWordsServicesInterface adWordsServices, AdWordsSession session, Long adGroupId)
            throws RemoteException {
        // Get the AdGroupCriterionService.
        AdGroupCriterionServiceInterface adGroupCriterionService =
                adWordsServices.get(session, AdGroupCriterionServiceInterface.class);

        int offset = 0;
        boolean morePages = true;

        // Create selector.
        SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(
                        AdGroupCriterionField.Id,
                        AdGroupCriterionField.CriteriaType,
                        AdGroupCriterionField.KeywordMatchType,
                        AdGroupCriterionField.KeywordText)
                .orderAscBy(AdGroupCriterionField.KeywordText)
                .offset(offset)
                .limit(PAGE_SIZE)
                .in(AdGroupCriterionField.AdGroupId, adGroupId.toString())
                .in(AdGroupCriterionField.CriteriaType, "KEYWORD")
                .build();

        while (morePages) {
            // Get all ad group criteria.
            AdGroupCriterionPage page = adGroupCriterionService.get(selector);

            // Display ad group criteria.
            if (page.getEntries() != null && page.getEntries().length > 0) {
                // Display results.
                Arrays.stream(page.getEntries())
                        .map(adGroupCriterionResult -> (Keyword) adGroupCriterionResult.getCriterion())
                        .forEach(
                                keyword ->
                                        System.out.printf(
                                                "Keyword with text '%s', match type '%s', criteria type '%s',"
                                                        + " and ID %d was found.%n",
                                                keyword.getText(),
                                                keyword.getMatchType(),
                                                keyword.getType(),
                                                keyword.getId()));
            } else {
                System.out.println("No ad group criteria were found.");
            }

            offset += PAGE_SIZE;
            selector = builder.increaseOffsetBy(PAGE_SIZE).build();
            morePages = offset < page.getTotalNumEntries();
        }
    }

    public static ArrayList<AdWordsResponse> parserXML(InputStream input) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<AdWordsResponse> result = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
        doc.getDocumentElement().normalize();

        Node dataNode = doc.getElementsByTagName("date-range").item(0);
        Element dataElement = (Element) dataNode;
        String[] dataString = dataElement.getAttribute("date").split("-");
        Date start = parseData(dataString[0]);
        Date end = parseData(dataString[1]);

        NodeList nodeList = doc.getElementsByTagName("row");
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            AdWordsResponse curent = new AdWordsResponse();

            Node node = nodeList.item(temp);
            Element element = (Element) node;

            //todo check input data
            curent.setStart(start);
            curent.setEnd(end);
            curent.setCampaignID(Long.parseLong(element.getAttribute("campaignID")));
            curent.setGroupID(Long.parseLong(element.getAttribute("adGroupID")));
            curent.setKeywordID(Long.parseLong(element.getAttribute("keywordID")));
            curent.setKeyword(element.getAttribute("keywordPlacement"));
            curent.setCriteriaType(element.getAttribute("criteriaType"));
            curent.setImpressions(Long.parseLong(element.getAttribute("impressions")));
            curent.setClicks(Long.parseLong(element.getAttribute("clicks")));
            curent.setCost(Double.parseDouble(element.getAttribute("cost")));

            result.add(curent);
        }
        return result;
    }

    private static Date parseData(String input) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        try {
            return dateFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static AdWordsSession createSesion(boolean enableValidateOnly) {
        AdWordsSession result;
        try {
            // Generate a refreshable OAuth2 credential.
            Credential oAuth2Credential =
                    new OfflineCredentials.Builder()
                            .forApi(Api.ADWORDS)
                            .withClientSecrets(CLIENT_ID, CLIENT_SECRET)
                            .withRefreshToken(REFRESH_TOKEN)
                            .build()
                            .generateCredential();
            if (enableValidateOnly) {
                result =
                        new AdWordsSession
                                .Builder()
                                .withDeveloperToken(DEVELOPER_TOKEN)
                                .withOAuth2Credential(oAuth2Credential)
                                .withClientCustomerId(CLIENT_CUSTOMER_ID)
                                .enableValidateOnly()
                                .build();
            } else {
                result =
                        new AdWordsSession
                                .Builder()
                                .withDeveloperToken(DEVELOPER_TOKEN)
                                .withOAuth2Credential(oAuth2Credential)
                                .withClientCustomerId(CLIENT_CUSTOMER_ID)
                                .enablePartialFailure()
                                .build();
            }
        } catch (ValidationException ve) {
            System.err.printf(
                    "Invalid configuration in the %s file. Exception: %s%n",
                    DEFAULT_CONFIGURATION_FILENAME, ve);
            return null;
        } catch (OAuthException oe) {
            System.err.printf(
                    "Failed to create OAuth credentials. Check OAuth settings in the %s file. "
                            + "Exception: %s%n",
                    DEFAULT_CONFIGURATION_FILENAME, oe);
            return null;
        }
        return result;
    }


}
