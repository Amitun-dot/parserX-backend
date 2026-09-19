package parserx_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import parserx_backend.dto.AIJobMatchResponse;
import parserx_backend.dto.JobMatchRequest;
import parserx_backend.entity.Analysis;
import parserx_backend.entity.JobMatch;
import parserx_backend.entity.Resume;
import parserx_backend.entity.User;
import parserx_backend.repository.AnalysisRepository;
import parserx_backend.repository.JobMatchRepository;
import parserx_backend.repository.ResumeRepository;
import parserx_backend.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;

@Service
public class JobMatchService {


    private final JobMatchRepository jobMatchRepository;
    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final PdfTextExtractorService pdfTextExtractorService;
    private final AIAnalysisRouter aiAnalysisRouter;
    private final ObjectMapper objectMapper;

    public JobMatchService(
            JobMatchRepository jobMatchRepository,
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            PdfTextExtractorService pdfTextExtractorService,
            AIAnalysisRouter aiAnalysisRouter,
            ObjectMapper objectMapper
    ) {
        this.jobMatchRepository = jobMatchRepository;
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.pdfTextExtractorService = pdfTextExtractorService;
        this.aiAnalysisRouter = aiAnalysisRouter;
        this.objectMapper = objectMapper;
    }

    public JobMatch matchJob(
            JobMatchRequest request,
            String email
    ) {

        if (request == null) {
            throw new RuntimeException("Job match request is required");
        }

        if (request.getResumeId() == null
                || request.getResumeId().isBlank()) {
            throw new RuntimeException("Resume ID is required");
        }

        if (request.getAnalysisId() == null
                || request.getAnalysisId().isBlank()) {
            throw new RuntimeException("Analysis ID is required");
        }

        if (request.getJobDescription() == null
                || request.getJobDescription().isBlank()) {
            throw new RuntimeException("Job description is required");
        }

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        Long resumeId;

        try {
            resumeId = Long.parseLong(request.getResumeId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid resume ID");
        }

        Resume resume =
                resumeRepository.findById(resumeId)
                        .orElseThrow(() ->
                                new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        Long analysisId;

        try {
            analysisId = Long.parseLong(request.getAnalysisId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid analysis ID");
        }

        Analysis analysis =
                analysisRepository.findById(analysisId)
                        .orElseThrow(() ->
                                new RuntimeException("Analysis not found"));

        if (!analysis.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (!analysis.getResumeId()
                .equals(request.getResumeId())) {

            throw new RuntimeException(
                    "Analysis does not belong to the selected resume"
            );
        }

        String resumeText;

        try {
            resumeText =
                    pdfTextExtractorService.extractText(
                            resume.getFilePath()
                    );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to extract resume text",
                    e
            );
        }

        if (resumeText == null
                || resumeText.isBlank()) {

            throw new RuntimeException(
                    "Could not extract text from resume"
            );
        }


        String aiResponse =
                aiAnalysisRouter.matchResumeToJob(
                        resumeText,
                        request.getJobDescription()
                );



        if (aiResponse == null
                || aiResponse.isBlank()) {

            throw new RuntimeException(
                    "AI provider returned an empty job match response"
            );
        }

        AIJobMatchResponse aiMatch;

        try {

            aiMatch =
                    objectMapper.readValue(
                            aiResponse,
                            AIJobMatchResponse.class
                    );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse AI job match response",
                    e
            );
        }

        validateAIJobMatch(aiMatch);

        JobMatch jobMatch =
                new JobMatch(
                        request.getResumeId(),
                        request.getAnalysisId(),
                        aiMatch.getJobTitle(),
                        request.getJobDescription(),
                        aiMatch.getOverallMatch(),
                        aiMatch.getTechnicalSkills(),
                        aiMatch.getExperience(),
                        aiMatch.getKeywords(),
                        aiMatch.getEducation(),
                        LocalDateTime.now(),
                        user
                );

        try {

            jobMatch.setStrongMatch(
                    objectMapper.writeValueAsString(
                            safeList(aiMatch.getStrongMatch())
                    )
            );

            jobMatch.setMissing(
                    objectMapper.writeValueAsString(
                            safeList(aiMatch.getMissing())
                    )
            );

            jobMatch.setPotentialGaps(
                    objectMapper.writeValueAsString(
                            safeList(aiMatch.getPotentialGaps())
                    )
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to store AI job match data",
                    e
            );
        }

        return jobMatchRepository.save(jobMatch);
    }

    private void validateAIJobMatch(
            AIJobMatchResponse aiMatch
    ) {

        if (aiMatch == null) {

            throw new RuntimeException(
                    "AI provider returned an invalid job match response"
            );
        }

        if (aiMatch.getJobTitle() == null
                || aiMatch.getJobTitle().isBlank()) {

            throw new RuntimeException(
                    "AI response is missing job title"
            );
        }

        validateScore(
                aiMatch.getOverallMatch(),
                "overallMatch"
        );

        validateScore(
                aiMatch.getTechnicalSkills(),
                "technicalSkills"
        );

        validateScore(
                aiMatch.getExperience(),
                "experience"
        );

        validateScore(
                aiMatch.getKeywords(),
                "keywords"
        );

        validateScore(
                aiMatch.getEducation(),
                "education"
        );
    }

    private void validateScore(
            int score,
            String fieldName
    ) {

        if (score < 0 || score > 100) {

            throw new RuntimeException(
                    "Invalid AI score for "
                            + fieldName
                            + ": "
                            + score
            );
        }
    }

    private <T> List<T> safeList(List<T> list) {

        return list == null
                ? new ArrayList<>()
                : list;
    }

    public List<JobMatch> getJobMatches(
            String email
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        return jobMatchRepository.findByUser(user);
    }


}
