package parserx_backend.dto;

import java.util.List;

public class AnalysisResult {

    private String id;
    private String resumeId;
    private String resumeFileName;
    private String analyzedAt;

    private int overallScore;
    private String overallStatus;
    private String overallSummary;

    private int atsScore;
    private int skillMatch;
    private Integer jobMatch;
    private int contentQuality;

    private ATSBreakdown atsBreakdown;
    private String atsExplanation;

    private List<SkillItem> skills;

    private Keywords keywords;

    private List<Recommendation> recommendations;

    private List<String> strengths;
    private List<String> weaknesses;

    private List<ResumeSectionScore> resumeSections;

    private boolean hasJobDescription;

    private JobMatchData jobMatchData;

    public AnalysisResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public String getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(String analyzedAt) {
        this.analyzedAt = analyzedAt;
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

    public List<SkillItem> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillItem> skills) {
        this.skills = skills;
    }

    public Keywords getKeywords() {
        return keywords;
    }

    public void setKeywords(Keywords keywords) {
        this.keywords = keywords;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
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

    public List<ResumeSectionScore> getResumeSections() {
        return resumeSections;
    }

    public void setResumeSections(List<ResumeSectionScore> resumeSections) {
        this.resumeSections = resumeSections;
    }

    public boolean isHasJobDescription() {
        return hasJobDescription;
    }

    public void setHasJobDescription(boolean hasJobDescription) {
        this.hasJobDescription = hasJobDescription;
    }

    public JobMatchData getJobMatchData() {
        return jobMatchData;
    }

    public void setJobMatchData(JobMatchData jobMatchData) {
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

        public void setKeywordOptimization(int keywordOptimization) {
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

    public static class SkillItem {

        private String name;
        private String status;

        public SkillItem() {
        }

        public SkillItem(String name, String status) {
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class Keywords {

        private List<String> matched;
        private List<String> missing;
        private int coverage;

        public Keywords() {
        }

        public List<String> getMatched() {
            return matched;
        }

        public void setMatched(List<String> matched) {
            this.matched = matched;
        }

        public List<String> getMissing() {
            return missing;
        }

        public void setMissing(List<String> missing) {
            this.missing = missing;
        }

        public int getCoverage() {
            return coverage;
        }

        public void setCoverage(int coverage) {
            this.coverage = coverage;
        }
    }

    public static class Recommendation {

        private String id;
        private String title;
        private String description;
        private String suggestion;
        private String priority;

        public Recommendation() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(String suggestion) {
            this.suggestion = suggestion;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }
    }

    public static class ResumeSectionScore {

        private String section;
        private int score;
        private String status;
        private String explanation;

        public ResumeSectionScore() {
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }

    public static class JobMatchData {

        private String jobTitle;
        private int overallMatch;
        private int technicalSkills;
        private int experience;
        private int keywords;
        private int education;

        private List<String> strongMatch;
        private List<String> missing;
        private List<String> potentialGaps;

        public JobMatchData() {
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

        public int getTechnicalSkills() {
            return technicalSkills;
        }

        public void setTechnicalSkills(int technicalSkills) {
            this.technicalSkills = technicalSkills;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public int getKeywords() {
            return keywords;
        }

        public void setKeywords(int keywords) {
            this.keywords = keywords;
        }

        public int getEducation() {
            return education;
        }

        public void setEducation(int education) {
            this.education = education;
        }

        public List<String> getStrongMatch() {
            return strongMatch;
        }

        public void setStrongMatch(List<String> strongMatch) {
            this.strongMatch = strongMatch;
        }

        public List<String> getMissing() {
            return missing;
        }

        public void setMissing(List<String> missing) {
            this.missing = missing;
        }

        public List<String> getPotentialGaps() {
            return potentialGaps;
        }

        public void setPotentialGaps(List<String> potentialGaps) {
            this.potentialGaps = potentialGaps;
        }
    }
}