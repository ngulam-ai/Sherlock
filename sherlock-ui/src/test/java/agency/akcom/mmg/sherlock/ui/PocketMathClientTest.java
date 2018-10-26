package agency.akcom.mmg.sherlock.ui;

import java.util.List;

import agency.akcom.mmg.sherlock.ui.server.pocket.PocketUtils;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.ReportDatum;
import agency.akcom.mmg.sherlock.ui.server.task.PocketMathImportTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PocketMathClientTest {
	public static void main(String... args) {
		
		//Set date:
		String date = "20170326";
		
		String startDate = PocketMathImportTask.getFromDate(date);
		String endDate = PocketMathImportTask.getToDate(date);
		System.out.println(startDate);
		System.out.println(endDate);
		
		long time = System.nanoTime();
		List<ReportDatum> report = PocketUtils.getReport(startDate, endDate);
		for(ReportDatum rep : report) {
			log.info(PocketMathImportTask.prepareMessage(rep, PocketMathImportTask.getYesterday(startDate)));
		}
		log.info("Done!");
		System.out.println(System.nanoTime() - time);
	}
}
//5291299402
//4102237005
//4363533655

//9635169956
//9582269976
//9867052777