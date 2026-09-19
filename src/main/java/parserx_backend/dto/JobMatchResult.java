package parserx_backend.dto;

import java.util.List;

public class JobMatchResult {


    private String analysisId;
    private String jobTitle;

    private int overallMatch;
    private int technicalSkills;
    private int experience;
    private int keywords;
    private int education;

    private List<String> strongMatch;
    private List<String> missing;
    private List<String> potentialGaps;

    public JobMatchResult() {
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
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
