package parserx_backend.dto;

public class AnalysisRequest {


    private String resumeId;
    private String jobDescription;

    public AnalysisRequest() {
    }

    public String getResumeId() {
        return resumeId;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }


}
