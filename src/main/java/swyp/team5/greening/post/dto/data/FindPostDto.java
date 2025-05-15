package swyp.team5.greening.post.dto.data;

import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.user.domain.entity.User;

public record FindPostDto(
        Post post,
        User user,
        Like like
) {

}
