package swyp.team5.greening.postLike.dto;

import jakarta.validation.constraints.NotNull;

public record PostLikeRequestDto(
        @NotNull
        Long postId
) {

}
