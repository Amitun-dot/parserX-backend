package parserx_backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import parserx_backend.dto.JobMatchHistoryItem;
import parserx_backend.dto.JobMatchRequest;
import parserx_backend.dto.JobMatchResult;
import parserx_backend.entity.JobMatch;
import parserx_backend.service.JobMatchService;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/job")
public class JobMatchController {


    private final JobMatchService jobMatchService;
    private final ObjectMapper objectMapper;

    public JobMatchController(
            JobMatchService jobMatchService,
            ObjectMapper objectMapper
    ) {
        this.jobMatchService = jobMatchService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/match")
    public ResponseEntity<JobMatchResult> matchJob(
            @RequestBody JobMatchRequest request,
            Authentication authentication
    ) {

        JobMatch jobMatch =
                jobMatchService.matchJob(
                        request,
                        authentication.getName()
                );

        JobMatchResult result = new JobMatchResult();

        result.setAnalysisId(jobMatch.getAnalysisId());
        result.setJobTitle(jobMatch.getJobTitle());
        result.setOverallMatch(jobMatch.getOverallMatch());
        result.setTechnicalSkills(jobMatch.getTechnicalSkills());
        result.setExperience(jobMatch.getExperience());
        result.setKeywords(jobMatch.getKeywords());
        result.setEducation(jobMatch.getEducation());

        try {
            result.setStrongMatch(
                    objectMapper.readValue(
                            jobMatch.getStrongMatch(),
                            ArrayList.class
                    )
            );

            result.setMissing(
                    objectMapper.readValue(
                            jobMatch.getMissing(),
                            ArrayList.class
                    )
            );

            result.setPotentialGaps(
                    objectMapper.readValue(
                            jobMatch.getPotentialGaps(),
                            ArrayList.class
                    )
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read job match data",
                    e
            );
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/matches")
    public ResponseEntity<List<JobMatchHistoryItem>> getJobMatches(
            Authentication authentication
    ) {

        List<JobMatch> jobMatches =
                jobMatchService.getJobMatches(
                        authentication.getName()
                );

        List<JobMatchHistoryItem> history =
                jobMatches.stream()
                        .map(jobMatch ->
                                new JobMatchHistoryItem(
                                        String.valueOf(jobMatch.getId()),
                                        jobMatch.getJobTitle(),
                                        jobMatch.getOverallMatch(),
                                        jobMatch.getAnalyzedAt()
                                )
                        )
                        .toList();

        return ResponseEntity.ok(history);
    }


}
