package swyp.team5.greening.postLike.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.fixture.PostFixture;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.postLike.domain.repository.LikeRepository;
import swyp.team5.greening.postLike.dto.PostLikeResponseDto;
import swyp.team5.greening.postLike.fixture.LikeFixture;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 좋아요 단위 테스트")
class PostLikeCommandServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private PostLikeCommandService postLikeCommandService;

    @DisplayName("게시글이 하나 존재하며, 유저는 해당 게시글을 좋아요 한 상태이다")
    @Nested
    class TestCase1 {

        Long userId = 100L;
        Long postId = 2L;
        Long likeId = 50L;

        Long likeCount = 20L;

        Post post = PostFixture.getPost(likeCount, userId);

        Like like = LikeFixture.getLike(postId, userId);

        @Test
        @DisplayName("게시글을 좋아요 한 상태라면, 좋아요가 취소되며 좋아요 수가 감소한다.")
        void likeCancelTest() {
            //given
            given(postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)).willReturn(
                    Optional.of(post));
            ReflectionTestUtils.setField(post, "id", postId);
            given(likeRepository.findByUserIdAndPostId(userId, postId)).willReturn(
                    Optional.of(like));
            ReflectionTestUtils.setField(like, "id", likeId);

            //when
            PostLikeResponseDto responseDto = postLikeCommandService.likeOrCancel(userId, postId);

            //then
            assertThat(responseDto.isLike()).isFalse();
            assertThat(post.getLikeCount()).isEqualTo(likeCount - 1);
        }

        @Test
        @DisplayName("게시글을 좋아요 하지 않은 상태라면, 좋아요 처리되며 좋아요 수가 증가한ㄷ.")
        void likeTest() {
            //given
            given(postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)).willReturn(
                    Optional.of(post));
            ReflectionTestUtils.setField(post, "id", postId);

            //when
            PostLikeResponseDto responseDto = postLikeCommandService.likeOrCancel(userId, postId);

            //then
            assertThat(responseDto.isLike()).isTrue();
            assertThat(post.getLikeCount()).isEqualTo(likeCount + 1);
        }
    }

}