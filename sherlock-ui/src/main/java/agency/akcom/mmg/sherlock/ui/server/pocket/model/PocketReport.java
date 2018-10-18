package agency.akcom.mmg.sherlock.ui.server.pocket.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PocketReport {

	private ArrayList<ReportDatum> data;

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class ReportDatum {

		String id;
		String name;
		String total_budget;
		String start_at;
		String end_at;
		String created_at;
		String updated_at;

	}

	public class ReportCompaigns {
		String id;
		String name;
		String total_budget;
		String start_at;
		String end_at;
		String created_at;
		String updated_at;
		String terminated_at;
		String created_by;
		String clicks;
		String conversions;
		String cpm;
		String status;
		String impressions;
		String spend;

		List<Orders> orders;

		public class Orders {
			String id;
			String pocketmath_id;
		}
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class ReportOrders {
		String name;
		String total_budget;
		String daily_budget;
		String hourly_budget;
		String created_at;
		String updated_at;
		String country_code;
		String terminated_at;
		String start_at;
		String end_at;
		String campaign_id;
		// List<Os_versions> os_versions;
		String deals;
		String general;
		String impressions_cap;
		String app_targeting;
		String bidding_strategy;
		// List<blacklisted_isp_ids> blacklisted_isp_ids
		// List<blacklisted_carrier_codes> blacklisted_carrier_codes
		// List<whitelisted_carrier_codes> whitelisted_carrier_codes
		// List<carrier_codes> carrier_codes
		// List<categories> categories
		// List<blacklisted_city_ids> blacklisted_city_ids
		// List<whitelisted_city_ids> whitelisted_city_ids
		// List<city_ids> city_ids
		String clicks;
		String conversions;
		String cpm;
		String created_by;
		String ecpm;
		String impressions;
		String pricing_targeting; // conversions
		String spend;
		String id;
		String pocketmath_id;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class ReportOrdersStats {
		List<Order> orders;

		@Data
		@ToString
		@NoArgsConstructor
		@AllArgsConstructor
		public class Order {
			String id;
			String pocketmath_id;
			String bid_count;
			String spend;
			String win_rate;
			String impressions;
			String clicks;
			String conversions;
		}
	}
}
