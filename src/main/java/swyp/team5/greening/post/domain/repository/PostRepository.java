package swyp.team5.greening.post.domain.repository;

import java.util.Optional;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;

public interface PostRepository {

    Post save(Post post);

    boolean existsById(Long id);

    void deleteAll();

    Optional<Post> findByIdAndState(Long postId, PostState state);

    Optional<Post> findByIdAndStateWithLock(Long postId, PostState state);

}
