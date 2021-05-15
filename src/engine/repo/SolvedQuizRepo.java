package engine.repo;

import engine.domain.SolvedQuizInfo;
import engine.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SolvedQuizRepo extends PagingAndSortingRepository<SolvedQuizInfo, Long> {
    Page<SolvedQuizInfo> findAllByUser(User user, Pageable pageable);
}

