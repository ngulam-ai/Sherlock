package agency.akcom.mmg.sherlock.ui.server.avazu.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Report {
	private int code; // Status code
	private String msg; // Description details
	private int totalcount; // Total amount of items
	private int page; // Page number currently displayed
	private int pagemaxcount; // Maximum items of each page
	private List<Datum> data;

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class Datum {

		private String campaign_id; // Campaign id
		private String creative_id; // Creative id
		// date // Timezone: UTC
		// Format: YYYY-MM-DD
		// Note that the field "date" only for campaign and creative dimension reports
		private String day;

		private int impressions; // Total amount of impressions
		private int clicks; // Total amount of clicks
		private int conversions; // Total amount of conversions
		private float spend; // Total amount of money spent, in US Dollars
		private String creative_name; // Creative name
		private String campaign_name; // Campaign name
		private String geo_name; // Country name
		private String city_name; // City name
		private String gender_name; // Gender name
		private String carriers_name; // Carriers name
		private String isp_name; // ISP name
		private String device_name; // Device name
		private String devicetype_name; // Device type name
		private String browser; // Mobile browser name
		private String os_name; // OS name
		private String osv; // OS version name
		private String connection_name; // Connection type name
		private String inventory_name; // Inventory type name
		private String inventory_id; // Inventory id
		private String publisher_name; // Publisher(Seller) name
		private String site_name; // Site name
		private String site_id; // Site id
		private String inventorytype_name; // Inventory type name

	}
}
