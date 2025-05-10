package swyp.team5.greening.comment.domain.repository;

import java.util.Optional;
import swyp.team5.greening.comment.domain.entity.Comment;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    void deleteById(Long id);

    void deleteAll();

}
