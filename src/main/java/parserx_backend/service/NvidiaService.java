package parserx_backend.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

@Service
public class NvidiaService implements AIAnalysisProvider {


    private static final String NVIDIA_URL =
            "https://integrate.api.nvidia.com/v1/chat/completions";

    private static final String MODEL =
            "nvidia/nemotron-3-ultra-550b-a55b";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public NvidiaService(
            ObjectMapper objectMapper,
            @Value("${spring.ai.openai.api-key}") String apiKey
    ) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String analyzeResume(
            String resumeText,
            String jobDescription
    ) {

        String prompt = """
        You are ParserX, an AI-powered resume analyzer.

        Analyze the resume carefully and return ONLY one valid JSON object.

        Do not return Markdown.
        Do not return ```json.
        Do not add text before or after the JSON.

        IMPORTANT:
        - Do not invent information.
        - Keep all text concise.
        - All scores must be integers from 0 to 100.
        - Return at most 10 items in arrays.
        - Use "matched", "missing", or "partial" for skill status.
        - Use "good", "average", or "poor" for overallStatus.
        - Use "good", "average", or "poor" for resume section status.
        - If no job description is provided, jobMatch should be based only
          on the resume quality and should not invent job requirements.
        - The JSON field names must match the structure below exactly.

        Return EXACTLY this structure:

        {
          "overallScore": 0,
          "overallStatus": "good",
          "overallSummary": "string",

          "atsScore": 0,
          "skillMatch": 0,
          "jobMatch": 0,
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
              "status": "matched"
            }
          ],

          "keywords": {
            "matched": [],
            "missing": [],
            "coverage": 0
          },

          "recommendations": [
            {
              "id": "string",
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
        }

        JOB DESCRIPTION:
        %s

        RESUME:
        %s
        """.formatted(
                jobDescription == null ? "" : jobDescription,
                resumeText
        );

        return callNvidia(prompt);
    }

    @Override
    public String matchResumeToJob(
            String resumeText,
            String jobDescription
    ) {

        String prompt = """
        You are ParserX, an AI job matching engine.

        Compare the resume against the job description using ONLY
        the information provided.

        Return ONLY one valid JSON object.
        Do not return Markdown.
        Do not return ```json.
        Do not add text before or after the JSON.

        IMPORTANT:
        - Keep all text concise.
        - Use short sentences.
        - Do not repeat information.
        - Do not invent skills, experience, education, or achievements.
        - All scores must be integers from 0 to 100.
        - Keep arrays concise.
        - Return at most 10 items in each array.
        - Distinguish keyword presence from practical experience.

        Return EXACTLY:

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

        JOB DESCRIPTION:
        %s

        RESUME:
        %s
        """.formatted(
                jobDescription == null ? "" : jobDescription,
                resumeText
        );

        return callNvidia(prompt);
    }

    private String callNvidia(String prompt) {

        try {

            ObjectNode requestBody =
                    objectMapper.createObjectNode();

            requestBody.put("model", MODEL);

            ArrayNode messages =
                    requestBody.putArray("messages");

            ObjectNode message =
                    messages.addObject();

            message.put("role", "user");
            message.put("content", prompt);

            requestBody.put("max_tokens", 16384);

            ObjectNode chatTemplateKwargs =
                    requestBody.putObject("chat_template_kwargs");

            chatTemplateKwargs.put(
                    "enable_thinking",
                    false
            );

            String jsonBody =
                    objectMapper.writeValueAsString(requestBody);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(NVIDIA_URL))
                            .header(
                                    "Authorization",
                                    "Bearer " + apiKey
                            )
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(jsonBody)
                            )
                            .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                throw new RuntimeException(
                        "NVIDIA API HTTP "
                                + response.statusCode()
                                + ": "
                                + response.body()
                );
            }

            JsonNode responseJson =
                    objectMapper.readTree(response.body());

            JsonNode content =
                    responseJson
                            .path("choices")
                            .path(0)
                            .path("message")
                            .path("content");

            if (content.isMissingNode()
                    || content.isNull()
                    || content.asText().isBlank()) {

                throw new RuntimeException(
                        "NVIDIA response did not contain message content."
                );
            }

            return content.asText();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "NVIDIA request was interrupted.",
                    e
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "NVIDIA request failed: "
                            + e.getMessage(),
                    e
            );
        }
    }


}
