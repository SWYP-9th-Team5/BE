package swyp.team5.greening.post.fixture;

import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;

public final class PostFixture {

    private PostFixture() {
    }

    public static Post getPost(Long likeCount, Long userId) {
        return Post.builder()
                .title("임시 제목")
                .likeCount(likeCount)
                .commentCount(0L)
                .state(PostState.IN_PROGRESS)
                .categoryId(1L)
                .userId(userId)
                .build();
    }

}
