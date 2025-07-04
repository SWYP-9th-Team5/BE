package swyp.team5.greening.post.dto;

import jakarta.validation.constraints.NotEmpty;

public record PostContentDto(
        @NotEmpty
        String type,

        @NotEmpty
        String value
) {

}
