package parserx_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import parserx_backend.entity.Analysis;
import parserx_backend.entity.User;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findByUser(User user);

}