package agency.akcom.mmg.sherlock.ui.server.task;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvazuImportTask extends AbsractTask {

	@Override
	public void run() {
		log.info("AvazuImportTask runing");
	}

	@Override
	protected String getUniqueKey() {
		return "";
	}

}
