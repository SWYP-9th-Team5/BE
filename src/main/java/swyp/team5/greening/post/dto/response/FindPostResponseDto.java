package swyp.team5.greening.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostContent;
import swyp.team5.greening.post.domain.entity.PostType;
import swyp.team5.greening.user.domain.entity.User;

public record FindPostResponseDto(
        Long postId,
        String title,
        Long userId,
        String userName,
        LocalDateTime createdAt,
        Long likeCount,
        Long commentCount,
        boolean isLike,
        boolean isAuthor,
        List<ContentDto> content
) {

    public static FindPostResponseDto of(
            Post post,
            User user,
            boolean isLike,
            boolean isAuthor
    ) {
        List<PostContent> postContents = post.getPostContents();

        return new FindPostResponseDto(
                post.getId(),
                post.getTitle(),
                user.getId(),
                user.getUserName(),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount(),
                isLike,
                isAuthor,
                postContents.stream()
                        .map(content -> new ContentDto(content.getType(), content.getContent()))
                        .toList()
        );
    }

    public record ContentDto(PostType type, String value) {

    }
}
