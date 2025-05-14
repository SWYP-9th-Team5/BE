package swyp.team5.greening.post.dto.response;

import java.time.LocalDateTime;
import java.util.Comparator;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostContent;
import swyp.team5.greening.post.dto.PostUserNameProjection;

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

    // 기본 isLike = false
    public static PostPreviewResponseDto from(PostUserNameProjection proj) {
        return from(proj, false);
    }

    // isLike 커스텀 지정
    public static PostPreviewResponseDto from(PostUserNameProjection proj, boolean isLike) {
        return new PostPreviewResponseDto(
            proj.getPostId(),
            proj.getCategoryId(),
            proj.getUserId(),
            proj.getUserName(),
            proj.getTitle(),
            proj.getContent(),
            proj.getLikeCount(),
            proj.getCommentCount(),
            proj.getCreatedAt(),
            proj.getLastModifiedAt(),
            isLike
        );
    }

    // Post 객체 + userName 기반 from 메서드 (N+1 방지용)
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
