package engine.repo;

import engine.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    UserDetails findByEmail(String email);
    boolean existsByEmail(String email);
}
