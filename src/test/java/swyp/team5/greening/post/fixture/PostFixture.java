package swyp.team5.greening.post.fixture;

import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostContent;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.entity.PostType;

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

    public static Post getPost(Long userId) {
        Post post = Post.builder()
                .title("임시 제목")
                .likeCount(0L)
                .commentCount(0L)
                .state(PostState.IN_PROGRESS)
                .categoryId(1L)
                .userId(userId)
                .build();

        getPostContents(post);

        return post;
    }

    private static void getPostContents(Post post) {
        for (int i = 1; i <= 5; i++) {
            PostContent.builder()
                    .content(i%2==0? "하용": "image")
                    .type(i%2==0? PostType.TEXT:PostType.IMAGE)
                    .sequence(i)
                    .post(post)
                    .build();
        }
    }

}
