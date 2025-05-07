package swyp.team5.greening.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveCommentRequestDto(
        @NotNull
        Long postId,

        @NotBlank
        String comment
) {

}
