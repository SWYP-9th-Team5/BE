package swyp.team5.greening.post.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UpdatePostRequestDto(

        @NotEmpty
        String title,

        @NotEmpty
        List<ContentDto> content

) {

}
