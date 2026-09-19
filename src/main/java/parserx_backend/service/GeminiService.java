package parserx_backend.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GeminiService implements AIAnalysisProvider {


    private final ChatClient chatClient;

    public GeminiService(
            @Qualifier("googleGenAiChatModel") ChatModel chatModel
    ) {
        this.chatClient = ChatClient
                .builder(chatModel)
                .build();
    }

    @Override
    public String analyzeResume(
            String resumeText,
            String jobDescription
    ) {

        String prompt = """
    You are ParserX, an AI resume analysis engine.

    Analyze the supplied resume and compare it with the supplied job description.

    Your response will be parsed directly by a Java backend.
    Therefore, the JSON structure and field names must be followed exactly.

    EVALUATE:

    - Overall resume quality
    - ATS compatibility
    - Technical skill match
    - Job match
    - Content quality
    - Keywords
    - Resume sections
    - Strengths
    - Weaknesses
    - Actionable recommendations

    RULES:

    - Return ONLY valid JSON.
    - Never use Markdown.
    - Never use ```json.
    - Do not write anything before or after the JSON.
    - All scores must be integers from 0 to 100.
    - Use only information present in the resume and job description.
    - Never invent skills, experience, education, projects, achievements,
      technologies, certifications, or requirements.
    - Use empty arrays when no items are available.
    - Keep text concise and specific.
    - Return at most 10 skills, 10 matched keywords, 10 missing keywords,
      6 recommendations, 5 strengths, and 5 weaknesses.
    - Every JSON field shown in the required structure must be present.

    JOB DESCRIPTION RULE:

    If a job description is provided:

    - jobMatch must contain a score from 0 to 100.
    - hasJobDescription must be true.
    - jobMatchData must contain the complete job matching object.

    If no job description is provided:

    - jobMatch must be null.
    - hasJobDescription must be false.
    - jobMatchData must be null.

    SKILL AND KEYWORD EVIDENCE:

    A keyword is matched when the term or a clear equivalent appears
    anywhere in the resume.

    A skill is demonstrated only when the resume provides evidence of
    using it through projects, internships, work experience, education,
    certifications, or other concrete evidence.

    A technology listed only in the Skills section can be a matched
    keyword but must not automatically be considered strongly demonstrated.

    Skill status:

    - "strong" = clearly demonstrated through relevant experience or projects
    - "moderate" = some supporting evidence but limited depth
    - "weak" = mentioned with little or no supporting evidence

    Missing keywords are important job-description terms absent from the resume.

    Potential gaps are requirements mentioned in the resume but lacking
    sufficient evidence of practical experience.

    Distinguish between:

    1. Keyword presence
    2. Demonstrated practical experience
    3. Job requirement

    Example:

    If PostgreSQL appears only in the Skills section and there is no evidence
    of using PostgreSQL elsewhere, it may be a matched keyword but can be
    considered a practical experience gap.

    STATUS VALUES:

    overallStatus:
    "excellent", "good", "needs_improvement", "poor"

    skill status:
    "strong", "moderate", "weak"

    recommendation priority:
    "high", "medium", "low"

    resume section status:
    "excellent", "good", "needs_improvement", "missing"

    REQUIRED JSON STRUCTURE:

    {
      "overallScore": 0,
      "overallStatus": "good",
      "overallSummary": "string",

      "atsScore": 0,
      "skillMatch": 0,
      "jobMatch": null,
      "contentQuality": 0,

      "atsBreakdown": {
        "keywordOptimization": 0,
        "formatting": 0,
        "sectionStructure": 0,
        "readability": 0
      },

      "atsExplanation": "string",

      "skills": [
        {
          "name": "string",
          "status": "strong"
        }
      ],

      "keywords": {
        "matched": [],
        "missing": [],
        "coverage": 0
      },

      "recommendations": [
        {
          "id": "rec1",
          "title": "string",
          "description": "string",
          "suggestion": "string",
          "priority": "high"
        }
      ],

      "strengths": [],
      "weaknesses": [],

      "resumeSections": [
        {
          "section": "string",
          "score": 0,
          "status": "good",
          "explanation": "string"
        }
      ],

      "hasJobDescription": false,

      "jobMatchData": null
    }

    IMPORTANT:

    When a job description is provided, return:

    "hasJobDescription": true

    and:

    "jobMatchData": {
      "jobTitle": "string",
      "overallMatch": 0,
      "technicalSkills": 0,
      "experience": 0,
      "keywords": 0,
      "education": 0,
      "strongMatch": [],
      "missing": [],
      "potentialGaps": []
    }

    JOB MATCH SCORING:

    overallMatch:
    Overall compatibility between the resume and job requirements.

    technicalSkills:
    Match between demonstrated technical skills and job requirements.

    experience:
    Match between demonstrated experience and job requirements.

    keywords:
    Percentage of important job-description keywords appearing in the resume.

    education:
    Compatibility between candidate education and job requirements.

    Strong matches must be supported by concrete resume evidence.

    Missing items must actually be absent from the resume.

    Potential gaps should be used when a skill or technology appears in the
    resume but there is insufficient evidence of practical use.

    RESUME SECTION ANALYSIS:

    Analyze the major sections that actually exist in the resume.

    For each section provide:

    - section
    - score
    - status
    - explanation

    Do not invent a resume section that does not exist.

    FINAL CHECK BEFORE RESPONDING:

    - Valid JSON only
    - No Markdown
    - No comments
    - No trailing commas
    - All required fields present
    - Correct field names
    - Scores between 0 and 100
    - jobMatchData consistent with hasJobDescription
    - No invented information

    JOB DESCRIPTION:

    %s

    RESUME:

    %s
    """.formatted(
                jobDescription == null ? "" : jobDescription,
                resumeText
        );

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }

    @Override
    public String matchResumeToJob(
            String resumeText,
            String jobDescription
    ) {

        String prompt = """
    You are ParserX, an AI job matching engine.

    Compare the supplied resume against the supplied job description.

    Your response will be parsed directly by a Java backend.

    RULES:

    - Return ONLY valid JSON.
    - Never use Markdown.
    - Never use ```json.
    - Do not write anything outside the JSON.
    - All scores must be integers from 0 to 100.
    - Use only information present in the resume and job description.
    - Never invent skills, experience, education, achievements, or requirements.
    - Consider evidence from projects, internships, work experience,
      education, and certifications.
    - Do not treat a technology as strongly demonstrated merely because
      it appears in the Skills section.
    - Keep arrays concise and relevant.
    - Do not add fields that are not present in the required structure.

    SCORING:

    overallMatch:
    Overall compatibility between the resume and job requirements.

    technicalSkills:
    Match between demonstrated technical skills and job requirements.

    experience:
    Match between demonstrated experience and job requirements.

    keywords:
    Percentage of important job-description keywords appearing in the resume.

    education:
    Compatibility between candidate education and job requirements.

    STRONG MATCH:

    Include important requirements clearly supported by concrete evidence
    in the resume.

    MISSING:

    Include important requirements or keywords that do not appear
    anywhere in the resume.

    POTENTIAL GAPS:

    Include requirements where the resume mentions the technology,
    skill, or concept but does not provide enough practical evidence.

    Example:

    If Docker appears only in Technical Skills without project or work
    evidence, it can be listed as a potential gap.

    JOB TITLE:

    Extract the most appropriate job title from the job description.
    Use the actual job title when clearly provided.

    RETURN EXACTLY THIS JSON:

    {
      "jobTitle": "string",
      "overallMatch": 0,
      "technicalSkills": 0,
      "experience": 0,
      "keywords": 0,
      "education": 0,
      "strongMatch": [],
      "missing": [],
      "potentialGaps": []
    }

    FINAL CHECK:

    - Valid JSON only
    - No Markdown
    - No comments
    - No trailing commas
    - All scores between 0 and 100
    - No invented information

    JOB DESCRIPTION:

    %s

    RESUME:

    %s
    """.formatted(
                jobDescription == null ? "" : jobDescription,
                resumeText
        );

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }


}
