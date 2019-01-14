package agency.akcom.mmg.sherlock.ui.server.task;

import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.configConnection.EmailPassConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.DspDao;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.PerfectAudienceUtils;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.ReportDatum;
import agency.akcom.mmg.sherlock.ui.server.options.TaskOptions;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.google.cloud.pubsub.v1.Publisher;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PerfectAudienceTask extends AbstractTask implements TaskOptions {
    private String token = null;

    @Override
    protected String getUniqueKey() {
        return "";
    }

    @Override
    public void run() {
        log.info("PerfectAudienceImportTask runing");
        ImportLog importLog = new ImportLog(Partner.PERFECTAUDIENCE);

        //Getting credentials for PerfectAudience
        ArrayList<ConfigConnection> credentialsList = getCredentials(Partner.PERFECTAUDIENCE, importLog);
        if(credentialsList == null){
            return;
        }

        Publisher publisher = preparePublisher();
        if (publisher == null) {
            log.error("PerfectAudience Publisher was not prepared");
            saveImportLog(importLog, false);
            return;
        }

        for (ConfigConnection config : credentialsList) {
            EmailPassConnection emailPassConnection = (EmailPassConnection) config;
            log.info("import for name:" + emailPassConnection.getName());
            String email = emailPassConnection.getEmail();
            String password = emailPassConnection.getPassword();
//
//            if(checkingValidCredentials() == false) {
//                log.warn("PERFECT_AUDIENCE Invalid credentials: Token{" + token + "}");
//                continue;
//            }
//
            List<ReportDatum> reportDatum = PerfectAudienceUtils.getReport(email, password);
            for(ReportDatum rep : reportDatum){
                log.info("RESULT : " + rep.getAdStatsList().toString());
            }

//            for (ReportDatum rep : reportDatum) {
//                for (ReportPublisher report : rep.getReportPublisher()) {
//                    postToPubSub(publisher, report, yesterday);
//                }
//            }
        }

    }
}
