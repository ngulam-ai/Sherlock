package agency.akcom.mmg.sherlock.ui.server.pocket.model;

import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderInfo;
import agency.akcom.mmg.sherlock.ui.server.pocket.model.PocketReport.ReportOrderStats.Order;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReportDatum {
	private Order order;
	private ReportOrderInfo infoOrder;
}
