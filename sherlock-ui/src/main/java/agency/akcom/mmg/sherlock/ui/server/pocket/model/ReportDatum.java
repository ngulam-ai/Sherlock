package agency.akcom.mmg.sherlock.ui.server.pocket.model;

import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrders;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrdersStats.Order;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReportDatum {
	private Order order;
	private ReportOrders infoOrder;
}
