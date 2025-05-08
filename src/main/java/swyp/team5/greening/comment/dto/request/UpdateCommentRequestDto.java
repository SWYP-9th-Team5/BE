package swyp.team5.greening.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentRequestDto(
        @NotNull
        Long commentId,

        @NotBlank
        String comment
) {

}
