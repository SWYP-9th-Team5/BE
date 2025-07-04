package swyp.team5.greening.post.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import swyp.team5.greening.post.dto.PostContentDto;

public record UpdatePostRequestDto(

        @NotEmpty
        String title,

        @NotEmpty
        List<PostContentDto> content

) {

}
