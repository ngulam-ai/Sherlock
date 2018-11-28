package agency.akcom.mmg.sherlock.ui.server.pocket.model;

import java.util.List;

import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportPublisher;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderInfo;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats.Order;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReportDatum {
	private Order order;
	private ReportOrderInfo infoOrder;
	private List<ReportPublisher> reportPublisher;
}
