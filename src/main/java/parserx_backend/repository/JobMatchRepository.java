package parserx_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import parserx_backend.entity.JobMatch;
import parserx_backend.entity.User;

public interface JobMatchRepository
        extends JpaRepository<JobMatch, Long> {


    List<JobMatch> findByUser(User user);


}
