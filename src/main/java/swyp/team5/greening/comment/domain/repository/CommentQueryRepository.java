package swyp.team5.greening.comment.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swyp.team5.greening.comment.dto.response.FindAllCommentResponseDto;
import swyp.team5.greening.comment.dto.response.FindMyAllCommentResponseDto;

public interface CommentQueryRepository {

    Page<FindAllCommentResponseDto> findAllComment(
            Long userId,
            Long postId,
            Pageable pageable
    );

    Page<FindMyAllCommentResponseDto> findMyComments(
            Long userId,
            Pageable pageable
    );

}
