package parserx_backend.dto;

import java.util.List;

public class AIAnalysisResponse {


    private int overallScore;
    private String overallStatus;
    private String overallSummary;

    private int atsScore;
    private int skillMatch;
    private Integer jobMatch;
    private int contentQuality;

    private ATSBreakdown atsBreakdown;
    private String atsExplanation;

    private List<AnalysisResult.SkillItem> skills;

    private AnalysisResult.Keywords keywords;

    private List<AnalysisResult.Recommendation> recommendations;

    private List<String> strengths;
    private List<String> weaknesses;

    private List<AnalysisResult.ResumeSectionScore> resumeSections;

    private boolean hasJobDescription;

    private AnalysisResult.JobMatchData jobMatchData;

    public AIAnalysisResponse() {
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public String getOverallSummary() {
        return overallSummary;
    }

    public void setOverallSummary(String overallSummary) {
        this.overallSummary = overallSummary;
    }

    public int getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(int atsScore) {
        this.atsScore = atsScore;
    }

    public int getSkillMatch() {
        return skillMatch;
    }

    public void setSkillMatch(int skillMatch) {
        this.skillMatch = skillMatch;
    }

    public Integer getJobMatch() {
        return jobMatch;
    }

    public void setJobMatch(Integer jobMatch) {
        this.jobMatch = jobMatch;
    }

    public int getContentQuality() {
        return contentQuality;
    }

    public void setContentQuality(int contentQuality) {
        this.contentQuality = contentQuality;
    }

    public ATSBreakdown getAtsBreakdown() {
        return atsBreakdown;
    }

    public void setAtsBreakdown(ATSBreakdown atsBreakdown) {
        this.atsBreakdown = atsBreakdown;
    }

    public String getAtsExplanation() {
        return atsExplanation;
    }

    public void setAtsExplanation(String atsExplanation) {
        this.atsExplanation = atsExplanation;
    }

    public List<AnalysisResult.SkillItem> getSkills() {
        return skills;
    }

    public void setSkills(List<AnalysisResult.SkillItem> skills) {
        this.skills = skills;
    }

    public AnalysisResult.Keywords getKeywords() {
        return keywords;
    }

    public void setKeywords(AnalysisResult.Keywords keywords) {
        this.keywords = keywords;
    }

    public List<AnalysisResult.Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(
            List<AnalysisResult.Recommendation> recommendations
    ) {
        this.recommendations = recommendations;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<AnalysisResult.ResumeSectionScore> getResumeSections() {
        return resumeSections;
    }

    public void setResumeSections(
            List<AnalysisResult.ResumeSectionScore> resumeSections
    ) {
        this.resumeSections = resumeSections;
    }

    public boolean isHasJobDescription() {
        return hasJobDescription;
    }

    public void setHasJobDescription(boolean hasJobDescription) {
        this.hasJobDescription = hasJobDescription;
    }

    public AnalysisResult.JobMatchData getJobMatchData() {
        return jobMatchData;
    }

    public void setJobMatchData(
            AnalysisResult.JobMatchData jobMatchData
    ) {
        this.jobMatchData = jobMatchData;
    }

    public static class ATSBreakdown {

        private int keywordOptimization;
        private int formatting;
        private int sectionStructure;
        private int readability;

        public ATSBreakdown() {
        }

        public int getKeywordOptimization() {
            return keywordOptimization;
        }

        public void setKeywordOptimization(
                int keywordOptimization
        ) {
            this.keywordOptimization = keywordOptimization;
        }

        public int getFormatting() {
            return formatting;
        }

        public void setFormatting(int formatting) {
            this.formatting = formatting;
        }

        public int getSectionStructure() {
            return sectionStructure;
        }

        public void setSectionStructure(int sectionStructure) {
            this.sectionStructure = sectionStructure;
        }

        public int getReadability() {
            return readability;
        }

        public void setReadability(int readability) {
            this.readability = readability;
        }
    }


}
