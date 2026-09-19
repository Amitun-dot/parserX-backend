package parserx_backend.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import parserx_backend.dto.AnalysisHistoryItem;
import parserx_backend.dto.AnalysisRequest;
import parserx_backend.dto.AnalysisResult;
import parserx_backend.entity.Analysis;
import parserx_backend.service.AnalysisService;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final ObjectMapper objectMapper;

    public AnalysisController(
            AnalysisService analysisService,
            ObjectMapper objectMapper
    ) {
        this.analysisService = analysisService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResult> analyzeResume(
            @RequestBody AnalysisRequest request,
            Authentication authentication
    ) {

        System.out.println("===== ANALYZE CONTROLLER START =====");

        System.out.println("Resume ID: " + request.getResumeId());
        System.out.println("Job Description: " + request.getJobDescription());
        System.out.println("Authenticated Email: " + authentication.getName());

        String email = authentication.getName();

        Analysis analysis = analysisService.analyzeResume(
                request.getResumeId(),
                request.getJobDescription(),
                email
        );

        System.out.println("===== ANALYSIS SAVED =====");
        System.out.println("Analysis ID: " + analysis.getId());

        AnalysisResult result = convertToAnalysisResult(analysis);

        System.out.println("===== RESPONSE CREATED =====");

        return ResponseEntity.ok(result);

    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResult> getAnalysis(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email = authentication.getName();

        Analysis analysis = analysisService.getAnalysis(
                id,
                email
        );

        return ResponseEntity.ok(
                convertToAnalysisResult(analysis)
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<AnalysisHistoryItem>> getAnalysisHistory(
            Authentication authentication
    ) {

        String email = authentication.getName();

        List<Analysis> analyses =
                analysisService.getAnalysisHistory(email);

        List<AnalysisHistoryItem> history = new ArrayList<>();

        for (Analysis analysis : analyses) {

            history.add(
                    new AnalysisHistoryItem(
                            String.valueOf(analysis.getId()),
                            analysis.getResumeFileName(),
                            analysis.getOverallScore(),
                            analysis.getAnalyzedAt()
                                    .format(
                                            DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                    )
                    )
            );
        }

        return ResponseEntity.ok(history);
    }

    private AnalysisResult convertToAnalysisResult(
            Analysis analysis
    ) {

        AnalysisResult result = new AnalysisResult();

        /*
         * Basic analysis information
         */

        result.setId(
                String.valueOf(analysis.getId())
        );

        result.setResumeId(
                analysis.getResumeId()
        );

        result.setResumeFileName(
                analysis.getResumeFileName()
        );

        result.setAnalyzedAt(
                analysis.getAnalyzedAt()
                        .format(
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        )
        );

        /*
         * Overall scores
         */

        result.setOverallScore(
                analysis.getOverallScore()
        );

        result.setOverallStatus(
                analysis.getOverallStatus()
        );

        result.setOverallSummary(
                analysis.getOverallSummary()
        );

        /*
         * Main analysis scores
         */

        result.setAtsScore(
                analysis.getAtsScore()
        );

        result.setSkillMatch(
                analysis.getSkillMatch()
        );

        result.setJobMatch(
                analysis.getJobMatch()
        );

        result.setContentQuality(
                analysis.getContentQuality()
        );

        /*
         * ATS explanation
         */

        result.setAtsExplanation(
                analysis.getAtsExplanation()
        );

        /*
         * Skills
         */

        try {

            List<AnalysisResult.SkillItem> skills =
                    objectMapper.readValue(
                            analysis.getSkills(),
                            new TypeReference<
                                    List<AnalysisResult.SkillItem>
                                    >() {}
                    );

            result.setSkills(skills);

        } catch (Exception e) {

            result.setSkills(
                    new ArrayList<>()
            );
        }

        /*
         * Keywords
         */

        try {

            AnalysisResult.Keywords keywords =
                    objectMapper.readValue(
                            analysis.getKeywords(),
                            new TypeReference<
                                    AnalysisResult.Keywords
                                    >() {}
                    );

            result.setKeywords(keywords);

        } catch (Exception e) {

            AnalysisResult.Keywords keywords =
                    new AnalysisResult.Keywords();

            keywords.setMatched(
                    new ArrayList<>()
            );

            keywords.setMissing(
                    new ArrayList<>()
            );

            keywords.setCoverage(0);

            result.setKeywords(keywords);
        }

        /*
         * Recommendations
         */

        try {

            List<AnalysisResult.Recommendation> recommendations =
                    objectMapper.readValue(
                            analysis.getRecommendations(),
                            new TypeReference<
                                    List<AnalysisResult.Recommendation>
                                    >() {}
                    );

            result.setRecommendations(
                    recommendations
            );

        } catch (Exception e) {

            result.setRecommendations(
                    new ArrayList<>()
            );
        }

        /*
         * Strengths
         */

        try {

            List<String> strengths =
                    objectMapper.readValue(
                            analysis.getStrengths(),
                            new TypeReference<List<String>>() {}
                    );

            result.setStrengths(
                    strengths
            );

        } catch (Exception e) {

            result.setStrengths(
                    new ArrayList<>()
            );
        }

        /*
         * Weaknesses
         */

        try {

            List<String> weaknesses =
                    objectMapper.readValue(
                            analysis.getWeaknesses(),
                            new TypeReference<List<String>>() {}
                    );

            result.setWeaknesses(
                    weaknesses
            );

        } catch (Exception e) {

            result.setWeaknesses(
                    new ArrayList<>()
            );
        }

        /*
         * Resume sections
         */

        try {

            List<AnalysisResult.ResumeSectionScore> resumeSections =
                    objectMapper.readValue(
                            analysis.getResumeSections(),
                            new TypeReference<
                                    List<AnalysisResult.ResumeSectionScore>
                                    >() {}
                    );

            result.setResumeSections(
                    resumeSections
            );

        } catch (Exception e) {

            result.setResumeSections(
                    new ArrayList<>()
            );
        }

        /*
         * Job description
         */

        String jobDescription =
                analysis.getJobDescription();

        boolean hasJobDescription =
                jobDescription != null
                        && !jobDescription.trim().isEmpty();

        result.setHasJobDescription(
                hasJobDescription
        );

        /*
         * Job match data
         */

        try {

            if (analysis.getJobMatchData() != null
                    && !analysis.getJobMatchData().equals("null")) {

                AnalysisResult.JobMatchData jobMatchData =
                        objectMapper.readValue(
                                analysis.getJobMatchData(),
                                AnalysisResult.JobMatchData.class
                        );

                result.setJobMatchData(
                        jobMatchData
                );

            } else {

                result.setJobMatchData(null);
            }

        } catch (Exception e) {

            result.setJobMatchData(null);
        }

        /*
         * ATS breakdown
         */

        AnalysisResult.ATSBreakdown atsBreakdown =
                new AnalysisResult.ATSBreakdown();

        atsBreakdown.setKeywordOptimization(
                analysis.getKeywordOptimization()
        );

        atsBreakdown.setFormatting(
                analysis.getFormatting()
        );

        atsBreakdown.setSectionStructure(
                analysis.getSectionStructure()
        );

        atsBreakdown.setReadability(
                analysis.getReadability()
        );

        result.setAtsBreakdown(
                atsBreakdown
        );

        return result;
    }


}
