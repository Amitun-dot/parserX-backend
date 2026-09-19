package parserx_backend.service;

public interface AIAnalysisProvider {

    String analyzeResume(
            String resumeText,
            String jobDescription
    );

    String matchResumeToJob(
            String resumeText,
            String jobDescription
    );
}