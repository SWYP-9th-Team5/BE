package swyp.team5.greening.post.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ContentDto(
        @NotEmpty
        String type,

        @NotEmpty
        String value
) {

}
