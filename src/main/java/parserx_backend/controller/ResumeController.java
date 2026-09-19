package parserx_backend.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import parserx_backend.dto.ResumeResponse;
import parserx_backend.entity.Resume;
import parserx_backend.service.ResumeService;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {


    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public ResponseEntity<?> getUserResumes(
            Authentication authentication
    ) {

        String email = authentication.getName();

        List<Resume> resumes =
                resumeService.getUserResumes(email);

        List<ResumeResponse> response = resumes.stream()
                .map(resume -> new ResumeResponse(
                        resume.getId(),
                        resume.getFileName(),
                        resume.getFileType()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String email = authentication.getName();

        Resume resume = resumeService.getUserResume(
                id,
                email
        );

        ResumeResponse response = new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getFileType()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(
            @PathVariable Long id,
            Authentication authentication
    ) throws IOException {

        String email = authentication.getName();

        resumeService.deleteUserResume(
                id,
                email
        );

        return ResponseEntity.ok(
                "Resume deleted successfully"
        );
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("resume") MultipartFile file,
            Authentication authentication
    ) throws IOException {


        String email = authentication.getName();

        Resume resume = resumeService.uploadResume(
                file,
                email
        );

        ResumeResponse response = new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getFileType()
        );

        return ResponseEntity.ok(response);


    }




}
