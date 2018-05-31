package agency.akcom.mmg.sherlock.ui.server.avazu;

import java.util.ArrayList;
import java.util.List;

import agency.akcom.mmg.sherlock.ui.server.avazu.client.AvazuClient;
import agency.akcom.mmg.sherlock.ui.server.avazu.client.AvazuClientBuilder;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Auth;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.AuthRequest;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.Report.Datum;
import agency.akcom.mmg.sherlock.ui.server.avazu.model.ReportRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AvazuUtils {

	private static final AvazuClientBuilder avazuClientBuilder = new AvazuClientBuilder();
	private static final AvazuClient avazuClient = avazuClientBuilder.getAvazuClient();

	public static List<Datum> getFullReportDatum(String command, String startdate, String enddate, String groupby) {

		AuthRequest authRequest = new AuthRequest();
		log.info(authRequest.toString());
		Auth auth = avazuClient.getAuth(authRequest);
		log.info(auth.toString());

		int page = 1;
		int lastPage = 0;
		List<Datum> datums = null;

		do {
			ReportRequest reportRequest = new ReportRequest(auth.getAccess_token(), command, startdate, enddate,
					groupby, page);
			log.info(reportRequest.toString());
			Report report = avazuClient.getReport(reportRequest);
			log.info(report.toString());
			
			if (lastPage == 0) {
				//first loop - do initializations
				datums = new ArrayList<Report.Datum>(report.getTotalcount());
				lastPage = report.getTotalcount() / report.getPagemaxcount() + 1;				
			}

			datums.addAll(report.getData());			

		} while (++page <= lastPage);

		return datums;
	}

}
