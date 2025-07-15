package swyp.team5.greening.post.infrastructure;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT post
            FROM Post post
            WHERE post.id = :postId
            AND post.state = :state
            """)
    Optional<Post> findByIdAndStateWithLock(
            @Param("postId") Long postId,
            @Param("state") PostState state
    );

}
