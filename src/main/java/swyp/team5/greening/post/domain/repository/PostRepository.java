package swyp.team5.greening.post.domain.repository;

import swyp.team5.greening.post.domain.entity.Post;

public interface PostRepository {

    Post save(Post post);

    boolean existsById(Long id);

    void deleteAll();

}
