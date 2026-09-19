package parserx_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "job_matches")
public class JobMatch {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String resumeId;

    @Column(nullable = false)
    private String analysisId;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String jobDescription;

    @Column(nullable = false)
    private int overallMatch;

    @Column(nullable = false)
    private int technicalSkills;

    @Column(nullable = false)
    private int experience;

    @Column(nullable = false)
    private int keywords;

    @Column(nullable = false)
    private int education;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String strongMatch;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String missing;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String potentialGaps;

    @Column(nullable = false)
    private LocalDateTime analyzedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public JobMatch() {
    }

    public JobMatch(
            String resumeId,
            String analysisId,
            String jobTitle,
            String jobDescription,
            int overallMatch,
            int technicalSkills,
            int experience,
            int keywords,
            int education,
            LocalDateTime analyzedAt,
            User user
    ) {
        this.resumeId = resumeId;
        this.analysisId = analysisId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.overallMatch = overallMatch;
        this.technicalSkills = technicalSkills;
        this.experience = experience;
        this.keywords = keywords;
        this.education = education;
        this.analyzedAt = analyzedAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
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

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
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

    public String getStrongMatch() {
        return strongMatch;
    }

    public void setStrongMatch(String strongMatch) {
        this.strongMatch = strongMatch;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }

    public String getPotentialGaps() {
        return potentialGaps;
    }

    public void setPotentialGaps(String potentialGaps) {
        this.potentialGaps = potentialGaps;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public void setAnalyzedAt(LocalDateTime analyzedAt) {
        this.analyzedAt = analyzedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
