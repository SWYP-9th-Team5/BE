package swyp.team5.greening.post.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.repository.PostRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostRepository {

}
