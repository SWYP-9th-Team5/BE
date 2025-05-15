package swyp.team5.greening.post.dto.response;

import java.time.LocalDateTime;

public record FindAllPostResponseDto(
        Long postId,
        Long categoryId,
        Long userId,
        String userName,
        String title,
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt,
        boolean isLike
) {

}
