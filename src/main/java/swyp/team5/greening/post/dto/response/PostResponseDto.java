package swyp.team5.greening.post.dto.response;

import java.util.List;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostType;

public record PostResponseDto(
    Long postId,
    String title,
    Long categoryId,
    Long likeCount,
    Long commentCount,
    List<ContentDto> content

) {
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
            post.getId(),
            post.getTitle(),
            post.getCategoryId(),
            post.getLikeCount(),
            post.getCommentCount(),
            post.getPostContents().stream()
                .map(content -> new ContentDto(content.getType(), content.getContent()))
                .toList()
        );
    }

    public record ContentDto(PostType type, String value) {}
}
