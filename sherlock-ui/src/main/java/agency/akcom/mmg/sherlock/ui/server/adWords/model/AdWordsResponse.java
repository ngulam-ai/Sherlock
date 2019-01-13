package agency.akcom.mmg.sherlock.ui.server.adWords.model;

import java.util.Date;

public class AdWordsResponse {
    private Date start;
    private Date end;
    private long campaignID ;
    private long groupID;
    private long keywordID;
    private String keyword;
    private String criteriaType;
    private long impressions;
    private long clicks;
    private double cost;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(long campaignID) {
        this.campaignID = campaignID;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public long getKeywordID() {
        return keywordID;
    }

    public void setKeywordID(long keywordID) {
        this.keywordID = keywordID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCriteriaType() {
        return criteriaType;
    }

    public void setCriteriaType(String criteriaType) {
        this.criteriaType = criteriaType;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
