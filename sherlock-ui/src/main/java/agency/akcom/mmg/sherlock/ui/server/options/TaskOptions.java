package agency.akcom.mmg.sherlock.ui.server.options;

import lombok.Getter;

public interface TaskOptions {
	
	public class Settings {
		private static final String TOPIC_ID = "real-time-ga-hit-data";
		private static final String PROJECT_ID = "dmpmm-200620";
		
		public static String getTopicId() {
			return TOPIC_ID;
		}

		public static String getProjectId() {
			return PROJECT_ID;
		}
	}
	
	// based on
	// https://developers.google.com/analytics/devguides/collection/protocol/v1/parameters?hl=en#trafficsources
	public class Keys {
		@Getter
		private static final String DATE_KEY = "date";
		@Getter
		private static final String DSP_KEY = "cd12";
		@Getter
		private static final String MODEL_KEY = "cd98";
		@Getter
		private static final String SOURCE_KEY = "cs";
		@Getter
		private static final String MEDIUM_KEY = "cm";
		@Getter
		private static final String CONTENT_KEY = "cc";
		@Getter
		private static final String TERM_KEY = "ck";
		@Getter
		private static final String CAMPAIGN_NAME_KEY = "cn";
		@Getter
		private static final String CAMPAIGN_ID_KEY = "ci";
		@Getter
		private static final String TIME_KEY = "time";

		@Getter
		private static final String IMPRESSIONS_KEY = "_imp";
		@Getter
		private static final String CLICKS_KEY = "_clk";
		@Getter
		private static final String CONVERSION_KEY = "_cnv";
		@Getter
		private static final String SPEND_KEY = "cp.ap";

		@Getter
		private static final String SITE_NAME_KEY = "_sn";
	}
}
