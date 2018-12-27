package agency.akcom.mmg.sherlock.ui.server.task;

import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.TokenConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.options.TaskOptions;
import agency.akcom.mmg.sherlock.ui.server.pocket.PocketUtils;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportPublisher;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.ReportDatum;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PocketMathImportTask extends AbstractTask implements TaskOptions {

    @Override
    public void run() {
        log.info("PocketMath import runing");
        ImportLog importLog = new ImportLog(Partner.POCKETMATH);

        DspDao dspdao = new DspDao();
        ArrayList<ConfigConnection> credentialsList;
        try {
            credentialsList = dspdao.getCredentials(Partner.POCKETMATH);
            // If got empty list
            if (credentialsList == null) {
                log.warn("Not credentials");
                importLog.setDescription("Not credentials for PocketMath");
                saveImportLog(importLog, true);
                return;
            }
        } catch (NoSuchFieldException ex) {
            log.warn("Not found credentials for PocketMath");
            importLog.setDescription("Not found credentials");
            saveImportLog(importLog, true);
            return;
        }

        Publisher publisher = preparePublisher();
        if (publisher == null) {
            log.error("PocketMath Publisher was not prepared");
            importLog.setDescription("Publisher was not prepared");
            saveImportLog(importLog, false);
            return;
        }


        String startDate = getFromDate();
//        String startDate = "2018-11-08T00:00:00.000Z";
        String endDate = getToDate();
//        String endDate = "2018-11-08T23:59:59.999Z";
        String yesterday = getYesterday(startDate);


        setPartner(Partner.POCKETMATH);
        for (ConfigConnection config : credentialsList) {
            TokenConnection tokenConnection = (TokenConnection) config;
            log.info("import for name:" + tokenConnection.getName());
            String token = tokenConnection.getToken();

            if (PocketUtils.checkingValidCredentials(token) == false) {
                log.warn("POCKETMATH. Invalid credentials: Token{" + token + "}");
                continue;
            }

            List<ReportDatum> reportDatum = PocketUtils.getReport(token, startDate, endDate);

            for (ReportDatum rep : reportDatum) {
                for (ReportPublisher report : rep.getReportPublisher()) {
                    postToPubSub(publisher, report, yesterday);
                    sumImpression(Long.parseLong(report.getImpressions()));
                    sumClick(Long.parseLong(report.getClicks()));
                    sumConversions(Long.parseLong(report.getConversions()));
                    sumSpendFloat(Float.parseFloat(report.getSpend()));
                }
            }
        }

        String description =
                "impression: " + impression
                        + "; click: " + click
                        + "; spend: " + spendFloat
                        + "; conversions: " + conversions
                        + "; from: " + startDate.substring(0, startDate.indexOf("T"));
        saveLogDescription(description);

        // When finished with the publisher, shutdown to free up resources.
        try {
            publisher.shutdown();
        } catch (Exception e) {
            log.error(e.toString());
        }
        importLog.setDescription("Download data");
        saveImportLog(importLog, true);
    }

    @Override
    protected String getUniqueKey() {
        return "";
    }

    // TODO same the logic with Avazu
    private static void postToPubSub(Publisher publisher, ReportPublisher report, String date) {
        // schedule a message to be published, messages are automatically batched
        // convert message to bytes
        ByteString data = ByteString.copyFromUtf8(prepareMessage(report, date));

        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

        ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
            @Override
            public void onSuccess(String messageId) {
                log.info("published with message id: " + messageId);
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("failed to publish: " + throwable);
            }
        });
    }

    public static String prepareMessage(ReportPublisher report, String date) {
        JSONObject jsonObject = new JSONObject();

        // in order to put it in proper day of the costdata_* tables

        jsonObject.put(Keys.getTIME_KEY(),
                LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

        jsonObject.put(Keys.getDSP_KEY(), "Pocketmath");            //Pocketmath
        jsonObject.put(Keys.getMODEL_KEY(), "cpm");                    //cpm
        jsonObject.put(Keys.getSOURCE_KEY(), report.getName());        //Publisher name
        jsonObject.put(Keys.getMEDIUM_KEY(), "Display");            //Display
        jsonObject.put(Keys.getCONTENT_KEY(), report.getReportDatum().getInfoOrder().getCreative().getName()); // Creative.name

        jsonObject.put(Keys.getCAMPAIGN_NAME_KEY(), report.getReportDatum().getInfoOrder().getName());            //"name" in order info by OrderId
        jsonObject.put(Keys.getCAMPAIGN_ID_KEY(), report.getReportDatum().getInfoOrder().getCampaign_id());        //"campaign_id" in order stats

        jsonObject.put(Keys.getDATE_KEY(), date.replaceAll("-", ""));

        jsonObject.put(Keys.getIMPRESSIONS_KEY(), report.getImpressions());            //"impressions"
        jsonObject.put(Keys.getCLICKS_KEY(), report.getClicks());                    //"clicks"
        jsonObject.put(Keys.getCONVERSION_KEY(), report.getConversions());            //"conversions"
        jsonObject.put(Keys.getSPEND_KEY(), report.getSpend());                        //"spendFloat"

        return jsonObject.toString();
    }

    /**
     * @return Representation yesterday's beginning day as String, formatted
     * "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'". Example for 2017-03-26 :
     * 2017-03-26T00:00:00.000Z
     */
    public static String getFromDate() {
        ZonedDateTime yesterday = ZonedDateTime.now().with(ChronoField.NANO_OF_DAY, 0).minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String date = dateTimeFormatter.format(yesterday);
        return date;
    }

    /**
     * @return Representation yesterday's end of the day as String, formatted
     * "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'". Example for 2017-03-26 :
     * 2017-03-26T23:59:59.999Z
     */
    public static String getToDate() {
        ZonedDateTime today = ZonedDateTime.now().with(ChronoField.NANO_OF_DAY, 0).minusNanos(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String date = dateTimeFormatter.format(today);
        return date;
    }

    /**
     * @return Representation the day as String, formatted "yyyy-MM-dd".
     */
    public static String getYesterday(String startDate) {
        ZonedDateTime yesterday = ZonedDateTime.parse(startDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTimeFormatter.format(yesterday);
    }

    // for test by date
    public static String getFromDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd")
                .parseDefaulting(ChronoField.NANO_OF_DAY, 0).toFormatter().withZone(ZoneId.systemDefault());

        ZonedDateTime yesterday = ZonedDateTime.parse(date, formatter).with(ChronoField.NANO_OF_DAY, 0);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String d = dateTimeFormatter.format(yesterday);
        return d;
    }

    public static String getToDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMdd")
                .parseDefaulting(ChronoField.NANO_OF_DAY, 0).toFormatter().withZone(ZoneId.systemDefault());

        ZonedDateTime today = ZonedDateTime.parse(date, formatter).plusDays(1).with(ChronoField.NANO_OF_DAY, 0)
                .minusNanos(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String d = dateTimeFormatter.format(today);
        return d;
    }

}
