package swyp.team5.greening.post.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdatePostRequestDto(

        @NotNull
        Long postId,

        @NotEmpty
        String title,

        @NotEmpty
        List<ContentDto> content

) {

}
