package parserx_backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import parserx_backend.entity.Resume;
import parserx_backend.entity.User;
import parserx_backend.repository.ResumeRepository;
import parserx_backend.repository.UserRepository;

@Service
public class ResumeService {


    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    private final Path uploadDirectory =
            Paths.get("uploads/resumes");

    public ResumeService(
            ResumeRepository resumeRepository,
            UserRepository userRepository
    ) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public Resume uploadResume(
            MultipartFile file,
            String email
    ) throws IOException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        Files.createDirectories(uploadDirectory);

        String originalFileName = file.getOriginalFilename();

        String storedFileName =
                UUID.randomUUID() + "_" + originalFileName;

        Path filePath =
                uploadDirectory.resolve(storedFileName);

        Files.copy(file.getInputStream(), filePath);

        Resume resume = new Resume(
                originalFileName,
                filePath.toString(),
                file.getContentType(),
                user
        );

        return resumeRepository.save(resume);
    }

    public List<Resume> getUserResumes(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return resumeRepository.findByUser(user);
    }

    public Resume getUserResume(
            Long id,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Resume resume = resumeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found")
                );

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return resume;
    }

    public void deleteUserResume(
            Long id,
            String email
    ) throws IOException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Resume resume = resumeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found")
                );

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        Path filePath = Paths.get(resume.getFilePath());

        Files.deleteIfExists(filePath);

        resumeRepository.delete(resume);
    }


}
