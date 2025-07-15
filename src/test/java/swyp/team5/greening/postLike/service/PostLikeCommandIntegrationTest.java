package swyp.team5.greening.postLike.service;

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
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.postLike.domain.repository.LikeRepository;
import swyp.team5.greening.support.TestContainerSupport;

@SpringBootTest
@DisplayName("게시글 좋아요 동시성 테스트")
public class PostLikeCommandIntegrationTest extends TestContainerSupport {

    @Autowired
    private PostLikeCommandService postLikeCommandService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        likeRepository.deleteAll();
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
        @DisplayName("동시에 20명의 유저가 게시글을 좋아요 할 경우, 좋아요 수가 알맞게 증가한다.")
        void likeConcurrencyTest() throws InterruptedException {
            //given
            ExecutorService executorService = Executors.newCachedThreadPool();
            CountDownLatch countDownLatch = new CountDownLatch(20);

            //when
            for (long i = 0; i < 20; i++) {
                final long userId = i;
                executorService.submit(() -> {
                    postLikeCommandService.likeOrCancel(userId, post.getId());
                    countDownLatch.countDown();

                });
            }

            countDownLatch.await();

            Post result = postRepository.findByIdAndState(post.getId(), PostState.IN_PROGRESS)
                    .orElseThrow();

            //then
            assertThat(result.getLikeCount()).isEqualTo(20L);

            executorService.shutdown();
        }

        @Test
        @DisplayName("한 명의 유저가 해당 기능을 30번 호출할 경우, 좋아요 수는 0이다.")
        void likeOrCancelConcurrencyTest() throws InterruptedException {
            //given
            ExecutorService executorService = Executors.newCachedThreadPool();
            CountDownLatch countDownLatch = new CountDownLatch(30);
            
            //when
            for (int i = 0; i < 30; i++) {
                executorService.submit(() -> {
                    postLikeCommandService.likeOrCancel(1L, post.getId());
                    countDownLatch.countDown();
                });
            }

            countDownLatch.await();

            Post result = postRepository.findByIdAndState(post.getId(), PostState.IN_PROGRESS)
                    .orElseThrow();

            //then
            assertThat(result.getLikeCount()).isEqualTo(0L);

        }
    }
}