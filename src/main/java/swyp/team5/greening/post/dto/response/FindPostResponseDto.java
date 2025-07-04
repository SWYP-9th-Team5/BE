package swyp.team5.greening.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostContent;
import swyp.team5.greening.post.dto.PostContentDto;
import swyp.team5.greening.user.domain.entity.User;

public record FindPostResponseDto(
        Long postId,
        String title,
        Long categoryId,
        Long userId,
        String userName,
        LocalDateTime createdAt,
        Long likeCount,
        Long commentCount,
        boolean isLike,
        boolean isAuthor,
        List<PostContentDto> content
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
                post.getCategoryId(),
                user.getId(),
                user.getUserName(),
                post.getCreatedAt(),
                post.getLikeCount(),
                post.getCommentCount(),
                isLike,
                isAuthor,
                postContents.stream()
                        .map(content -> new PostContentDto(content.getType().name(), content.getContent()))
                        .toList()
        );
    }
}
