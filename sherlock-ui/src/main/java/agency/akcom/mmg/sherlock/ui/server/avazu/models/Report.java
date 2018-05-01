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
	public int code; // Status code
	public String msg; // Description details
	public int totalcount; // Total amount of items
	public int page; // Page number currently displayed
	public int pagemaxcount; // Maximum items of each page
	public List<Datum> data;
	
	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class Datum {

		public String campaignId;
		public String creativeId;
		public String day;
		public String impressions;
		public String clicks;
		public String conversions;
		public String spend;
		public String creativeName;
		public String campaignName;
		
//		date	No	String	Timezone: UTC
//		Format: YYYY-MM-DD
//		Note that the field "date" only for campaign and creative dimension reports
//		campaign_id	No	String	Campaign id
//		creative_id	No	String	Creative id
//		impressions	Yes	String	Total amount of impressions
//		clicks	Yes	String	Total amount of clicks
//		conversions	Yes	String	Total amount of conversions
//		spend	Yes	float	Total amount of money spent, in US Dollars
//		campaign_name	No	String	Campaign name
//		creative_name	No	String	Creative name
//		geo_name	No	String	Country name
//		city_name	No	String	City name
//		gender_name	No	String	Gender name
//		carriers_name	No	String	Carriers name
//		isp_name	No	String	ISP name
//		device_name	No	String	Device name
//		campaign_name	No	String	Campaign name
//		devicetype_name	No	String	Device type name
//		browser	No	String	Mobile browser name
//		os_name	No	String	OS name
//		osv	No	String	OS version name
//		connection_name	No	String	Connection type name
//		inventory_name	No	String	Inventory type name
//		inventory_id	No	String	Inventory id
//		publisher_name	No	String	Publisher(Seller) name
//		site_name	No	String	Site name
//		site_id	No	String	Site id
//		inventorytype_name	No	String	Inventory type name

	}
}
