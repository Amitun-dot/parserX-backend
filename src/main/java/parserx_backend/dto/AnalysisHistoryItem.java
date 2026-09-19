package parserx_backend.dto;

public class AnalysisHistoryItem {

    private String id;
    private String resumeFileName;
    private int overallScore;
    private String analyzedAt;

    public AnalysisHistoryItem() {
    }

    public AnalysisHistoryItem(String id, String resumeFileName, int overallScore, String analyzedAt) {
        this.id = id;
        this.resumeFileName = resumeFileName;
        this.overallScore = overallScore;
        this.analyzedAt = analyzedAt;
    }

    public String getId() {
        return id;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public String getAnalyzedAt() {
        return analyzedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public void setAnalyzedAt(String analyzedAt) {
        this.analyzedAt = analyzedAt;
    }
}