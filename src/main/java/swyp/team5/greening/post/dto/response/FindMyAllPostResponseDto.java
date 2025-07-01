package swyp.team5.greening.post.dto.response;

import java.time.LocalDateTime;

public record FindMyAllPostResponseDto(
        Long postId,
        Long categoryId,
        String title,
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt,
        boolean isLike

        ) {

}
