package swyp.team5.greening.user.domain.repository;

import java.util.Optional;
import swyp.team5.greening.user.domain.entity.User;

public interface UserRepository {

    boolean existsById(Long userId);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);

}
