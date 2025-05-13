package swyp.team5.greening.post.dto.response;

import java.time.LocalDateTime;
import java.util.Comparator;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostContent;

public record PostPreviewResponseDto(
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
    public static PostPreviewResponseDto from(Post post, boolean isLike, String userName) {
        PostContent firstContent = post.getPostImages().stream()
            .sorted(Comparator.comparing(PostContent::getSequence))
            .findFirst()
            .orElse(null);

        return new PostPreviewResponseDto(
            post.getId(),
            post.getCategoryId(),
            post.getUserId(),
            userName,
            post.getTitle(),
            firstContent != null ? firstContent.getContent() : "",
            post.getLikeCount(),
            post.getCommentCount(),
            post.getCreatedAt(),
            post.getUpdatedAt(),
            isLike
        );
    }
}
