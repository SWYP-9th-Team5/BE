package swyp.team5.greening.comment.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.team5.greening.comment.domain.entity.Comment;
import swyp.team5.greening.comment.domain.repository.CommentRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long>, CommentRepository {

}
