package swyp.team5.greening.post.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreatePostRequestDto(

    @NotEmpty
    String title,

    @NotNull
    Long categoryId,

    @NotEmpty
    List<ContentDto> content

) {
    public record ContentDto(
        @NotEmpty
        String type,

        @NotEmpty
        String value
    ) {}
}