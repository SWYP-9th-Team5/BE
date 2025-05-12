package swyp.team5.greening.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.post.dto.response.CreatePostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;

@DisplayName("게시글 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PostCommandServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostCreateService postCreateService;

    @InjectMocks
    private PostReadService postReadService;

    @InjectMocks
    private PostDeleteService postDeleteService;

    private final Long userId = 1L;
    private final String title = "테스트 게시글 제목";
    private final Long categoryId = 5L;

    private final List<CreatePostRequestDto.ContentDto> contentList = List.of(
        new CreatePostRequestDto.ContentDto("TEXT", "본문1"),
        new CreatePostRequestDto.ContentDto("IMAGE", "http://image.com/test.jpg")
    );

    @Nested
    @DisplayName("게시글 생성 테스트")
    class CreatePostTest {

        @Test
        @DisplayName("게시글을 성공적으로 작성할 수 있다")
        void create_success() {
            // given
            Post post = Post.builder()
                .title(title)
                .userId(userId)
                .categoryId(categoryId)
                .state(PostState.IN_PROGRESS)
                .likeCount(0L)
                .commentCount(0L)
                .build();
            ReflectionTestUtils.setField(post, "id", 100L);
            given(postRepository.save(any(Post.class))).willReturn(post);

            // when
            CreatePostResponseDto result = postCreateService.createPost(
                userId, new CreatePostRequestDto(title, categoryId, contentList)
            );

            // then
            assertThat(result.postId()).isEqualTo(100L);
            verify(postRepository).save(any(Post.class));
        }

        @Test
        @DisplayName("제목이 null이면 예외가 발생한다")
        void create_fail_titleNull() {
            CreatePostRequestDto request = new CreatePostRequestDto(null, categoryId, contentList);

            assertThatThrownBy(() -> postCreateService.createPost(userId, request))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }

        @Test
        @DisplayName("본문이 비어있으면 예외가 발생한다")
        void create_fail_emptyContent() {
            CreatePostRequestDto request = new CreatePostRequestDto(title, categoryId, List.of());

            assertThatThrownBy(() -> postCreateService.createPost(userId, request))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }

    @Nested
    @DisplayName("게시글 단건 조회 테스트")
    class ReadPostTest {

        @Test
        @DisplayName("게시글을 정상적으로 조회할 수 있다")
        void read_success() {
            // given
            Post post = Post.builder()
                .title(title)
                .userId(userId)
                .categoryId(categoryId)
                .state(PostState.IN_PROGRESS)
                .likeCount(0L)
                .commentCount(0L)
                .build();
            ReflectionTestUtils.setField(post, "id", 200L);

            given(postRepository.findByIdAndState(200L, PostState.IN_PROGRESS)).willReturn(java.util.Optional.of(post));

            // when
            Post result = postReadService.findPost(200L);

            // then
            assertThat(result.getId()).isEqualTo(200L);
            assertThat(result.getTitle()).isEqualTo(title);
        }

        @Test
        @DisplayName("게시글이 없으면 예외가 발생한다")
        void read_fail_notFound() {
            given(postRepository.findByIdAndState(999L, PostState.IN_PROGRESS)).willReturn(java.util.Optional.empty());

            assertThatThrownBy(() -> postReadService.findPost(999L))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class DeletePostTest {

        @Test
        @DisplayName("게시글을 성공적으로 삭제할 수 있다 (상태 변경)")
        void delete_success() {
            // given
            Post post = Post.builder()
                .title(title)
                .userId(userId)
                .categoryId(categoryId)
                .state(PostState.IN_PROGRESS)
                .likeCount(0L)
                .commentCount(0L)
                .build();
            ReflectionTestUtils.setField(post, "id", 300L);

            given(postRepository.findByIdAndState(300L, PostState.IN_PROGRESS)).willReturn(java.util.Optional.of(post));

            // when
            postDeleteService.deletePost(userId, 300L);

            // then
            assertThat(post.getState()).isEqualTo(PostState.DELETED);
        }

        @Test
        @DisplayName("작성자가 아니면 삭제할 수 없다")
        void delete_fail_wrongUser() {
            Post post = Post.builder()
                .title(title)
                .userId(999L) // 다른 사용자
                .categoryId(categoryId)
                .state(PostState.IN_PROGRESS)
                .likeCount(0L)
                .commentCount(0L)
                .build();
            ReflectionTestUtils.setField(post, "id", 301L);

            given(postRepository.findByIdAndState(301L, PostState.IN_PROGRESS)).willReturn(java.util.Optional.of(post));

            // then
            assertThatThrownBy(() -> postDeleteService.deletePost(userId, 301L))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }
}