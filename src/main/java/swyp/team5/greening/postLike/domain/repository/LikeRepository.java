package swyp.team5.greening.postLike.domain.repository;

import java.util.Optional;
import swyp.team5.greening.postLike.domain.entity.Like;

public interface LikeRepository {

    Like save(Like like);

    void deleteById(Long id);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    void deleteAll();

}
