package swyp.team5.greening.post.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;

public interface PostRepository {

    Post save(Post post);

    boolean existsById(Long id);

    void deleteAll();

    Optional<Post> findByIdAndState(Long postId, PostState state);
//    List<Post> findAllByState(PostState state); // 전체 조회 기능
    List<Post> findTop6ByCategoryIdAndStateOrderByCreatedAtDesc(Long categoryId, PostState state);
    Page<Post> findAllByCategoryIdAndStateOrderByCreatedAtDesc(Long categoryId, PostState state, Pageable pageable);

}
