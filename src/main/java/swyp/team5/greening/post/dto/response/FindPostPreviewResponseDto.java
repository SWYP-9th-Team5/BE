package swyp.team5.greening.post.dto.response;

import java.time.LocalDateTime;

public record FindPostPreviewResponseDto(
        Long postId,
        Long categoryId,
        Long userId,
        String userName,
        String title,
        String content,
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt,
        boolean isLike
) {

}
