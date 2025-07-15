package swyp.team5.greening.comment.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swyp.team5.greening.comment.domain.repository.CommentRepository;
import swyp.team5.greening.comment.dto.request.SaveCommentRequestDto;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.support.TestContainerSupport;

@SpringBootTest
public class PostCommentCountConcurrencyTest extends TestContainerSupport {

    @Autowired
    private CommentCommandService commentCommandService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void init() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Nested
    @DisplayName("게시글 1개가 존재한다.")
    class TestCase1 {

        Post post;

        @BeforeEach
        void init() {
            post = Post.builder()
                    .title("제목")
                    .commentCount(0L)
                    .likeCount(0L)
                    .state(PostState.IN_PROGRESS)
                    .categoryId(1L)
                    .userId(1L)
                    .build();
            postRepository.save(post);
        }

        @Test
        @DisplayName("동시에 20명의 유저가 게시글의 댓글을 작성한다면, 댓글 수는 20이다.")
        void saveCommentConcurrencyTest() throws InterruptedException {
            //given
            ExecutorService executorService = Executors.newCachedThreadPool();
            CountDownLatch countDownLatch = new CountDownLatch(20);

            //when
            for (long i = 0; i < 20; i++) {
                final long userId = i;
                executorService.submit(() -> {
                    commentCommandService.saveComment(userId,
                            new SaveCommentRequestDto(
                                    post.getId(), "댓글" + userId
                            ));

                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();

            Post result = postRepository.findByIdAndState(post.getId(), PostState.IN_PROGRESS)
                    .orElseThrow();

            assertThat(result.getCommentCount()).isEqualTo(20L);
        }
    }
}
