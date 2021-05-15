package engine.repo;

import engine.domain.Quiz;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepo extends PagingAndSortingRepository<Quiz, Long> {
}
