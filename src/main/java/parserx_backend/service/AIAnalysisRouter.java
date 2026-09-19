package parserx_backend.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import parserx_backend.exception.AIServiceException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AIAnalysisRouter {

    private final AIAnalysisProvider geminiService;
    private final AIAnalysisProvider nvidiaService;
    private final ObjectMapper objectMapper;

    public AIAnalysisRouter(
            @Qualifier("geminiService") AIAnalysisProvider geminiService,
            @Qualifier("nvidiaService") AIAnalysisProvider nvidiaService,
            ObjectMapper objectMapper
    ) {
        this.geminiService = geminiService;
        this.nvidiaService = nvidiaService;
        this.objectMapper = objectMapper;
    }

    public String analyzeResume(
            String resumeText,
            String jobDescription
    ) {

        System.out.println(
                "AI Router: Trying Gemini..."
        );

        try {

            String result =
                    geminiService.analyzeResume(
                            resumeText,
                            jobDescription
                    );

            if (!isValidJson(result)) {

                throw new AIServiceException(
                        "Gemini returned invalid JSON."
                );
            }

            System.out.println(
                    "AI Router: Gemini succeeded."
            );

            return result;

        } catch (Exception geminiException) {

            System.out.println(
                    "AI Router: Gemini failed."
            );

            System.out.println(
                    "Gemini error: "
                            + geminiException.getMessage()
            );

            System.out.println(
                    "AI Router: Falling back to NVIDIA Nemotron..."
            );

            try {

                String result =
                        nvidiaService.analyzeResume(
                                resumeText,
                                jobDescription
                        );

                if (!isValidJson(result)) {

                    throw new AIServiceException(
                            "NVIDIA Nemotron returned invalid JSON."
                    );
                }

                System.out.println(
                        "AI Router: NVIDIA Nemotron succeeded."
                );

                return result;

            } catch (Exception nvidiaException) {

                System.out.println(
                        "AI Router: NVIDIA Nemotron failed."
                );

                System.out.println(
                        "NVIDIA error: "
                                + nvidiaException.getMessage()
                );

                throw new AIServiceException(
                        "Both Gemini and NVIDIA AI providers failed.",
                        nvidiaException
                );
            }
        }
    }

    public String matchResumeToJob(
            String resumeText,
            String jobDescription
    ) {

        System.out.println(
                "AI Router: Trying Gemini for job match..."
        );

        try {

            String result =
                    geminiService.matchResumeToJob(
                            resumeText,
                            jobDescription
                    );

            if (!isValidJson(result)) {

                throw new AIServiceException(
                        "Gemini returned invalid JSON."
                );
            }

            System.out.println(
                    "AI Router: Gemini job match succeeded."
            );

            return result;

        } catch (Exception geminiException) {

            System.out.println(
                    "AI Router: Gemini job match failed."
            );

            System.out.println(
                    "Gemini error: "
                            + geminiException.getMessage()
            );

            System.out.println(
                    "AI Router: Falling back to NVIDIA Nemotron for job match..."
            );

            try {

                String result =
                        nvidiaService.matchResumeToJob(
                                resumeText,
                                jobDescription
                        );

                if (!isValidJson(result)) {

                    throw new AIServiceException(
                            "NVIDIA Nemotron returned invalid JSON."
                    );
                }

                System.out.println(
                        "AI Router: NVIDIA Nemotron job match succeeded."
                );

                return result;

            } catch (Exception nvidiaException) {

                System.out.println(
                        "AI Router: NVIDIA Nemotron job match failed."
                );

                System.out.println(
                        "NVIDIA error: "
                                + nvidiaException.getMessage()
                );

                throw new AIServiceException(
                        "Both Gemini and NVIDIA AI providers failed.",
                        nvidiaException
                );
            }
        }
    }

    private boolean isValidJson(String response) {

        if (response == null
                || response.isBlank()) {

            return false;
        }

        try {

            JsonNode jsonNode =
                    objectMapper.readTree(response);

            return jsonNode != null
                    && jsonNode.isObject();

        } catch (Exception e) {

            return false;
        }
    }


}
