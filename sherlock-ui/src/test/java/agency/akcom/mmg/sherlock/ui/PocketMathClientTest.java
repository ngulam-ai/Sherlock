package agency.akcom.mmg.sherlock.ui;

import java.util.List;

import agency.akcom.mmg.sherlock.ui.server.pocket.PocketUtils;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportPublisher;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.ReportDatum;
import agency.akcom.mmg.sherlock.ui.server.task.PocketMathImportTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PocketMathClientTest {
	public static void main(String... args) {
		
		//Set date:
		String date = "20181001";
		String token = "f569c162f3c4c9142d8813355928b272aec227b4801cfe0273b7e6120a4886ac";
		
		String startDate = PocketMathImportTask.getFromDate(date);
		String endDate = PocketMathImportTask.getToDate(date);
		System.out.println(startDate);
		System.out.println(endDate);
		
		long time = System.nanoTime();
		List<ReportDatum> reportDatum = PocketUtils.getReport(token, startDate, endDate);
		for (ReportDatum rep : reportDatum) {
			log.info("Total for OrderId:" + rep.getOrder().getId() + "spend:" + rep.getOrder().getSpend()
					+ " click:" + rep.getOrder().getClicks() + " impress:" + rep.getOrder().getImpressions());
			Float spend = 0f;
			int click = 0;
			int impression = 0;
			for (ReportPublisher report : rep.getReportPublisher()) {
//				log.info(PocketMathImportTask.prepareMessage(report, PocketMathImportTask.getYesterday(startDate)));
				spend += Float.parseFloat(report.getSpend());
				click += Integer.parseInt(report.getClicks());
				impression += Integer.parseInt(report.getImpressions());
				
			}
			log.info("click:" + click + " spend:" + spend + " impress:" + impression);
		}
		log.info("Done!");
		System.out.println(System.nanoTime() - time);
		System.out.println(reportDatum.size());
	}
}
//5291299402
//4102237005
//4363533655

//9635169956
//9582269976
//9867052777