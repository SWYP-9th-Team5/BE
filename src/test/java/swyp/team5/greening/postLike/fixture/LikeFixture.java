package swyp.team5.greening.postLike.fixture;

import swyp.team5.greening.postLike.domain.entity.Like;

public final class LikeFixture {

    private LikeFixture() {
    }

    public static Like getLike(Long postId, Long userId) {
        return Like.builder()
                .postId(postId)
                .userId(userId)
                .build();
    }

}
