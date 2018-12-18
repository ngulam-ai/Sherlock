package agency.akcom.mmg.sherlock.ui.server.facebook.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class FacebookReport {
    @lombok.Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class AccountId{
        List<Data> data;
    }

    @lombok.Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class Data{
        String id;
        String account_id;
    }

    @lombok.Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReportStat{
        String impressions;
    }
}
