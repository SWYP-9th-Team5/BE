package swyp.team5.greening.post.dto;

import java.time.LocalDateTime;

public interface PostUserNameProjection {

    Long getPostId();

    Long getCategoryId();

    Long getUserId();

    String getUserName();

    String getTitle();

    String getContent();  // 첫 번째 content

    Long getLikeCount();

    Long getCommentCount();

    LocalDateTime getCreatedAt();

    LocalDateTime getLastModifiedAt();
}
