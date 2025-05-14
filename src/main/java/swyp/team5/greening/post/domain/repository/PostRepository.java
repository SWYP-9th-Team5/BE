package swyp.team5.greening.post.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;

public interface PostRepository {

    Post save(Post post);

    boolean existsById(Long id);

    void deleteAll();

    Optional<Post> findByIdAndState(Long postId, PostState state);
//    List<Post> findAllByState(PostState state); // 전체 조회 기능
//    List<Post> findTop6ByCategoryIdAndStateOrderByCreatedAtDesc(Long categoryId, PostState state);
    @Query("""
        SELECT a FROM Post a
        WHERE a.categoryId = :categoryId
          AND a.state = :state
          AND FUNCTION('DATE', a.createdAt) = CURRENT_DATE
        ORDER BY a.likeCount DESC
    """)
    List<Post> findTop6TodayByCategoryIdAndStateOrderByLikeCountDesc(
        @Param("categoryId") Long categoryId,
        @Param("state") PostState state
    );
    Page<Post> findAllByCategoryIdAndStateOrderByCreatedAtDesc(Long categoryId, PostState state, Pageable pageable);

}
