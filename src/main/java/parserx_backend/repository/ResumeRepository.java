package parserx_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import parserx_backend.entity.Resume;
import parserx_backend.entity.User;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUser(User user);

}
