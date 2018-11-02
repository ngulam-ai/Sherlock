package agency.akcom.mmg.sherlock.ui.server.avazu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
	private String access_token; // The access token issued by Avazu authorization server.

	private String command; // Reports dimensions, can be one of the following value: "user", "campaign",
					// "creative"

	private String startdate; // Timezone: UTC Format: YYYY-MM-DD. The date range from startdate to the
						// current date must be less than or equal to 180 days.

	private String enddate; // Timezone: UTC Format: YYYY-MM-DD. Enddate must be larger than or equal to
					// startdate, and the date range from the startdate to the end date must be less
					// than or equal to 30 days.

	private String groupby; // Which variable will be grouped by, note that "groupby" filed only for
					// campiagn and creative dimension reports, can be one of the following value:
					// Value -- Description
					// creative -- creative
					// geo -- country
					// city -- city
					// gender -- gender
					// carrier -- carrier
					// isp -- isp
					// device -- device
					// devicetype -- device type
					// browser -- mobile browser
					// os -- operation system
					// osv -- operation system version
					// connection -- connection type
					// inventory -- inventory
					// publisher -- publisher(seller)
					// site -- site
					// inventorytype -- inventory type
	private int page; // Each page contains a maximum 100 items. Default value is 1. (Note that "page" field only for campaign and creative dimension reports)
}
