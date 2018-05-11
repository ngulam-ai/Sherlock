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
	String access_token; // The access token issued by Avazu authorization server.

	String command; // Reports dimensions, can be one of the following value: "user", "campaign",
					// "creative"

	String startdate; // Timezone: UTC Format: YYYY-MM-DD. The date range from startdate to the
						// current date must be less than or equal to 180 days.

	String enddate; // Timezone: UTC Format: YYYY-MM-DD. Enddate must be larger than or equal to
					// startdate, and the date range from the startdate to the end date must be less
					// than or equal to 30 days.
}
