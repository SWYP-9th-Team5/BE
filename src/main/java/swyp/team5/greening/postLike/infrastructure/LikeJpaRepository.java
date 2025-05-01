package swyp.team5.greening.postLike.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.postLike.domain.repository.LikeRepository;

public interface LikeJpaRepository extends JpaRepository<Like, Long>, LikeRepository {

}
