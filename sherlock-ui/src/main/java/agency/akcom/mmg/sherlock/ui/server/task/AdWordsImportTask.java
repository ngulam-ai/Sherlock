package agency.akcom.mmg.sherlock.ui.server.task;

import agency.akcom.mmg.sherlock.ui.server.options.TaskOptions;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdWordsImportTask extends AbstractTask implements TaskOptions {
    @Override
    protected String getUniqueKey() {
        return null;
    }

    @Override
    public void run() {
        log.info("AdWordsImportTask running");
        ImportLog importLog = new ImportLog(Partner.AD_WORDS);


    }
}
