package swyp.team5.greening.post.dto.response;

import java.util.List;

public record PostPaginationResponseDto(
    List<PostPreviewResponseDto> posts,
    PaginationDto paginationDto
) {

}
