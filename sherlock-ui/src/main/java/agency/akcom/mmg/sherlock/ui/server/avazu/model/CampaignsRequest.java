package agency.akcom.mmg.sherlock.ui.server.avazu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CampaignsRequest {
	private String access_token; // The access token issued by Avazu authorization server.
	private String command; // Use the following value to get all campaigns status: "get"

	private int page; // Each page contains a maximum 100 items. Default value is 1. (Note that "page"
				// field only for campaign and creative dimension reports)
	
	private int pagecount; // Maximum items of each page
	//private int status; // Campaign status
	//Value -- Status
	//1 -- Active
	//0 -- Inactive

}
