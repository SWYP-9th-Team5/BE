package swyp.team5.greening.post.domain.repository;

import java.util.List;
import java.util.Optional;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;

public interface PostRepository {

    Post save(Post post);

    boolean existsById(Long id);

    void deleteAll();

    Optional<Post> findByIdAndState(Long postId, PostState state);
//    List<Post> findAllByState(PostState state); // 전체 조회 기능

}
