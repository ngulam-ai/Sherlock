package agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model.PerfectReport.*;

@Data
@ToString
public class ReportDatum {
    List<AdStats> adStatsList;
    CampaignStats campaignStats;
}