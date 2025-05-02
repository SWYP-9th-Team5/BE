package swyp.team5.greening.user.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.user.domain.entity.User;
import swyp.team5.greening.user.domain.repository.UserRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {

    Optional<User> findByEmail(String email);

}
