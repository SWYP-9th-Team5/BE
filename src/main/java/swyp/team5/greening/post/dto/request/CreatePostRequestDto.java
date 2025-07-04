package swyp.team5.greening.post.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import swyp.team5.greening.post.dto.PostContentDto;

public record CreatePostRequestDto(

        @NotEmpty
        String title,

        @NotNull
        Long categoryId,

        @NotEmpty
        List<PostContentDto> content

) {

}
