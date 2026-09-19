package parserx_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "analyses")
public class Analysis {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String resumeId;

    @Column(nullable = false)
    private String resumeFileName;

    @Column(nullable = false)
    private LocalDateTime analyzedAt;

    @Column(nullable = false)
    private int overallScore;

    @Column(nullable = false)
    private int atsScore;

    @Column(nullable = false)
    private int skillMatch;

    @Column(nullable = false)
    private int contentQuality;

    @Column(nullable = false)
    private int keywordOptimization;

    @Column(nullable = false)
    private int formatting;

    @Column(nullable = false)
    private int sectionStructure;

    @Column(nullable = false)
    private int readability;

    @Column
    private Integer jobMatch;

    @Column(nullable = false)
    private String overallStatus;

    @Column(columnDefinition = "TEXT")
    private String overallSummary;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Column(columnDefinition = "TEXT")
    private String atsExplanation;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String skills;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String keywords;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String recommendations;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String strengths;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String weaknesses;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String resumeSections;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String jobMatchData;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Analysis() {
    }

    public Analysis(
            String resumeId,
            String resumeFileName,
            LocalDateTime analyzedAt,
            int overallScore,
            int atsScore,
            int skillMatch,
            int contentQuality,
            int keywordOptimization,
            int formatting,
            int sectionStructure,
            int readability,
            String overallStatus,
            String overallSummary,
            String jobDescription,
            User user
    ) {
        this.resumeId = resumeId;
        this.resumeFileName = resumeFileName;
        this.analyzedAt = analyzedAt;
        this.overallScore = overallScore;
        this.atsScore = atsScore;
        this.skillMatch = skillMatch;
        this.contentQuality = contentQuality;
        this.keywordOptimization = keywordOptimization;
        this.formatting = formatting;
        this.sectionStructure = sectionStructure;
        this.readability = readability;
        this.overallStatus = overallStatus;
        this.overallSummary = overallSummary;
        this.jobDescription = jobDescription;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getResumeId() {
        return resumeId;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public int getAtsScore() {
        return atsScore;
    }

    public int getSkillMatch() {
        return skillMatch;
    }

    public int getContentQuality() {
        return contentQuality;
    }

    public int getKeywordOptimization() {
        return keywordOptimization;
    }

    public int getFormatting() {
        return formatting;
    }

    public int getSectionStructure() {
        return sectionStructure;
    }

    public int getReadability() {
        return readability;
    }

    public Integer getJobMatch() {
        return jobMatch;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public String getOverallSummary() {
        return overallSummary;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getAtsExplanation() {
        return atsExplanation;
    }

    public String getSkills() {
        return skills;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public String getStrengths() {
        return strengths;
    }

    public String getWeaknesses() {
        return weaknesses;
    }

    public String getResumeSections() {
        return resumeSections;
    }

    public String getJobMatchData() {
        return jobMatchData;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public void setAtsScore(int atsScore) {
        this.atsScore = atsScore;
    }

    public void setSkillMatch(int skillMatch) {
        this.skillMatch = skillMatch;
    }

    public void setContentQuality(int contentQuality) {
        this.contentQuality = contentQuality;
    }

    public void setKeywordOptimization(int keywordOptimization) {
        this.keywordOptimization = keywordOptimization;
    }

    public void setFormatting(int formatting) {
        this.formatting = formatting;
    }

    public void setSectionStructure(int sectionStructure) {
        this.sectionStructure = sectionStructure;
    }

    public void setReadability(int readability) {
        this.readability = readability;
    }

    public void setJobMatch(Integer jobMatch) {
        this.jobMatch = jobMatch;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public void setOverallSummary(String overallSummary) {
        this.overallSummary = overallSummary;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setAtsExplanation(String atsExplanation) {
        this.atsExplanation = atsExplanation;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }

    public void setResumeSections(String resumeSections) {
        this.resumeSections = resumeSections;
    }

    public void setJobMatchData(String jobMatchData) {
        this.jobMatchData = jobMatchData;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
