package swyp.team5.greening.comment.dto.response;

import java.time.LocalDateTime;

public record FindMyAllCommentResponseDto(
        Long commentId,
        String comment,
        Long postId,
        Long categoryId,
        String postTitle,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt
) {

}
