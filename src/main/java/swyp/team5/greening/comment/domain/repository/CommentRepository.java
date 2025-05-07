package swyp.team5.greening.comment.domain.repository;

import swyp.team5.greening.comment.domain.entity.Comment;

public interface CommentRepository {

    Comment save(Comment comment);

    void deleteAll();

}
