package parserx_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import parserx_backend.dto.AIAnalysisResponse;
import parserx_backend.entity.Analysis;
import parserx_backend.entity.Resume;
import parserx_backend.entity.User;
import parserx_backend.repository.AnalysisRepository;
import parserx_backend.repository.ResumeRepository;
import parserx_backend.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;

@Service
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final PdfTextExtractorService pdfTextExtractorService;
    private final AIAnalysisRouter aiAnalysisRouter;
    private final ObjectMapper objectMapper;

    public AnalysisService(
            AnalysisRepository analysisRepository,
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            PdfTextExtractorService pdfTextExtractorService,
            AIAnalysisRouter aiAnalysisRouter,
            ObjectMapper objectMapper
    ) {
        this.analysisRepository = analysisRepository;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.pdfTextExtractorService = pdfTextExtractorService;
        this.aiAnalysisRouter = aiAnalysisRouter;
        this.objectMapper = objectMapper;
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    private Resume getUserResume(
            String resumeId,
            User user
    ) {

        Long id;

        try {

            id = Long.parseLong(resumeId);

        } catch (NumberFormatException e) {

            throw new RuntimeException(
                    "Invalid resume ID"
            );
        }

        Resume resume =
                resumeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found"
                                )
                        );

        if (!resume.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Access denied"
            );
        }

        return resume;
    }

    public Analysis analyzeResume(
            String resumeId,
            String jobDescription,
            String email
    ) {

        User user = getUserByEmail(email);

        Resume resume = getUserResume(
                resumeId,
                user
        );

        /*
         * STEP 1
         * Extract text from uploaded PDF
         */

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

        /*
         * STEP 2
         * Send resume + job description to AI Router
         *
         * Router behavior:
         *
         * Gemini succeeds
         *       ↓
         * return Gemini response
         *
         * Gemini fails / quota / rate limit
         *       ↓
         * NVIDIA Nemotron
         */


        String aiResponse =
                aiAnalysisRouter.analyzeResume(
                        resumeText,
                        jobDescription
                );



        if (aiResponse == null
                || aiResponse.isBlank()) {

            throw new RuntimeException(
                    "AI provider returned an empty response"
            );
        }

        /*
         * STEP 3
         * Convert AI JSON into Java object
         */

        AIAnalysisResponse aiAnalysis;

        try {

            aiAnalysis =
                    objectMapper.readValue(
                            aiResponse,
                            AIAnalysisResponse.class
                    );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse AI analysis response",
                    e
            );
        }

        /*
         * STEP 3.1
         * Validate AI response
         */

        validateAIAnalysis(aiAnalysis);

        /*
         * STEP 4
         * Create database Analysis entity
         */

        Analysis analysis =
                new Analysis(
                        String.valueOf(resume.getId()),
                        resume.getFileName(),
                        LocalDateTime.now(),

                        aiAnalysis.getOverallScore(),
                        aiAnalysis.getAtsScore(),
                        aiAnalysis.getSkillMatch(),
                        aiAnalysis.getContentQuality(),

                        aiAnalysis.getAtsBreakdown()
                                .getKeywordOptimization(),

                        aiAnalysis.getAtsBreakdown()
                                .getFormatting(),

                        aiAnalysis.getAtsBreakdown()
                                .getSectionStructure(),

                        aiAnalysis.getAtsBreakdown()
                                .getReadability(),

                        aiAnalysis.getOverallStatus(),
                        aiAnalysis.getOverallSummary(),
                        jobDescription,
                        user
                );

        /*
         * Main job match score
         */

        analysis.setJobMatch(
                aiAnalysis.getJobMatch()
        );

        /*
         * ATS explanation
         */

        analysis.setAtsExplanation(
                aiAnalysis.getAtsExplanation()
        );

        /*
         * Convert structured AI data into JSON
         * for PostgreSQL JSONB columns.
         */

        try {

            analysis.setSkills(
                    objectMapper.writeValueAsString(
                            safeList(
                                    aiAnalysis.getSkills()
                            )
                    )
            );

            analysis.setKeywords(
                    objectMapper.writeValueAsString(
                            aiAnalysis.getKeywords()
                    )
            );

            analysis.setRecommendations(
                    objectMapper.writeValueAsString(
                            safeList(
                                    aiAnalysis.getRecommendations()
                            )
                    )
            );

            analysis.setStrengths(
                    objectMapper.writeValueAsString(
                            safeList(
                                    aiAnalysis.getStrengths()
                            )
                    )
            );

            analysis.setWeaknesses(
                    objectMapper.writeValueAsString(
                            safeList(
                                    aiAnalysis.getWeaknesses()
                            )
                    )
            );

            analysis.setResumeSections(
                    objectMapper.writeValueAsString(
                            safeList(
                                    aiAnalysis.getResumeSections()
                            )
                    )
            );

            analysis.setJobMatchData(
                    aiAnalysis.getJobMatchData() == null
                            ? null
                            : objectMapper.writeValueAsString(
                            aiAnalysis.getJobMatchData()
                    )
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to store AI analysis data",
                    e
            );
        }

        /*
         * STEP 5
         * Save complete analysis to PostgreSQL
         */

        return analysisRepository.save(
                analysis
        );
    }

    private void validateAIAnalysis(
            AIAnalysisResponse aiAnalysis
    ) {

        if (aiAnalysis == null) {

            throw new RuntimeException(
                    "AI provider returned an invalid analysis response"
            );
        }

        if (aiAnalysis.getAtsBreakdown() == null) {

            throw new RuntimeException(
                    "AI response is missing ATS breakdown"
            );
        }

        validateScore(
                aiAnalysis.getOverallScore(),
                "overallScore"
        );

        validateScore(
                aiAnalysis.getAtsScore(),
                "atsScore"
        );

        validateScore(
                aiAnalysis.getSkillMatch(),
                "skillMatch"
        );

        validateScore(
                aiAnalysis.getContentQuality(),
                "contentQuality"
        );

        validateScore(
                aiAnalysis.getAtsBreakdown()
                        .getKeywordOptimization(),
                "keywordOptimization"
        );

        validateScore(
                aiAnalysis.getAtsBreakdown()
                        .getFormatting(),
                "formatting"
        );

        validateScore(
                aiAnalysis.getAtsBreakdown()
                        .getSectionStructure(),
                "sectionStructure"
        );

        validateScore(
                aiAnalysis.getAtsBreakdown()
                        .getReadability(),
                "readability"
        );

        if (aiAnalysis.getOverallStatus() == null
                || aiAnalysis.getOverallStatus().isBlank()) {

            throw new RuntimeException(
                    "AI response is missing overall status"
            );
        }

        if (aiAnalysis.getOverallSummary() == null
                || aiAnalysis.getOverallSummary().isBlank()) {

            throw new RuntimeException(
                    "AI response is missing overall summary"
            );
        }
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

    private <T> List<T> safeList(
            List<T> list
    ) {

        return list == null
                ? new ArrayList<>()
                : list;
    }

    public Analysis getAnalysis(
            Long analysisId,
            String email
    ) {

        User user = getUserByEmail(email);

        Analysis analysis =
                analysisRepository.findById(
                        analysisId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Analysis not found"
                        )
                );

        if (!analysis.getUser().getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Access denied"
            );
        }

        return analysis;
    }

    public List<Analysis> getAnalysisHistory(
            String email
    ) {

        User user =
                getUserByEmail(email);

        return analysisRepository.findByUser(
                user
        );
    }
}