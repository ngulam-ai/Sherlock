package agency.akcom.mmg.sherlock.ui.server.avazu.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Campaigns {
	private int code; // Status code
	private String msg; // Description details

	private List<CampaignDatum> data;

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class CampaignDatum {

		private String id; // String Campaigns id
		private String name; // Campaigns name
		private int status; // Campaigns status
		// Value--Status
		// 1--Active
		// 0--Inactive
		private int bidtype; // Campaigns bid type
		// Value -- Bid Type
		// 0 -- CPM
		// 1 -- CPM optimized towards to an eCPC
		// 2 -- CPM optimized towards to a CTR
		// 3 -- CPM optimized towards to a CPA
		// 4 -- CPC
		// 11 -- CPA
		private int bidprice; // Campaigns bid price
		// e.g.: Bid Price = 1000000
		// Tip: $1 (USD) = 1000000
	}
}
