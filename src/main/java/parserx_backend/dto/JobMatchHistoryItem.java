package parserx_backend.dto;

import java.time.LocalDateTime;

public class JobMatchHistoryItem {


    private String id;
    private String jobTitle;
    private int overallMatch;
    private LocalDateTime analyzedAt;

    public JobMatchHistoryItem() {
    }

    public JobMatchHistoryItem(
            String id,
            String jobTitle,
            int overallMatch,
            LocalDateTime analyzedAt
    ) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.overallMatch = overallMatch;
        this.analyzedAt = analyzedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getOverallMatch() {
        return overallMatch;
    }

    public void setOverallMatch(int overallMatch) {
        this.overallMatch = overallMatch;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }


}
