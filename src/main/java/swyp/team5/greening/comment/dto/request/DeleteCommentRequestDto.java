package swyp.team5.greening.comment.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeleteCommentRequestDto(
        @NotNull
        Long commentId
) {

}
