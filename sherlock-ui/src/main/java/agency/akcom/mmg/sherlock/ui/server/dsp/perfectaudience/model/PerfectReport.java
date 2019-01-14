package agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

public class PerfectReport {
	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class Auth {
		String status;
		String token;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class CampaignReport {
		List<CampaignStats> campaignsReportList;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class CampaignStats {
		String campaign_id;	//The ID of the campaign
		String campaign_nid;//The numeric ID of the campaign
		String campaign_name;
		String campaign_type;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class AdReport {
		List<AdStats> adStats;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class AdStats {
		String ad_id;	// The ID of the ad
		String ad_nid; //	The numeric ID of the ad
		String ad_name;
		String ad_type;
		String impressions;
		String clicks;
		String cost;
		String click_conversions;
		String view_conversions;
		String conversions;
		String cpm;
		String cpc;
	}
}