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

@ExtendWith(MockitoExtension.class)
@DisplayName("PostCommandService 테스트")
class PostCommandServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostCommandService postCommandService;

    private final Long userId = 1L;
    private final String title = "테스트 게시글";
    private final Long categoryId = 5L;
    private final List<CreatePostRequestDto.ContentDto> contentList = List.of(
        new CreatePostRequestDto.ContentDto("TEXT", "본문1"),
        new CreatePostRequestDto.ContentDto("IMAGE", "http://image.com/test.jpg")
    );

    @Nested
    @DisplayName("게시글 생성")
    class CreatePost {

        @Test
        void 성공적으로_생성한다() {
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

            CreatePostResponseDto result = postCommandService.createPost(
                userId, new CreatePostRequestDto(title, categoryId, contentList)
            );

            assertThat(result.postId()).isEqualTo(100L);
            verify(postRepository).save(any(Post.class));
        }

        @Test
        void 제목이_null이면_예외() {
            var request = new CreatePostRequestDto(null, categoryId, contentList);

            assertThatThrownBy(() -> postCommandService.createPost(userId, request))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }

        @Test
        void 본문이_없으면_예외() {
            var request = new CreatePostRequestDto(title, categoryId, List.of());

            assertThatThrownBy(() -> postCommandService.createPost(userId, request))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost {

        @Test
        void 성공적으로_삭제한다() {
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

            postCommandService.deletePost(userId, 200L);

            assertThat(post.getState()).isEqualTo(PostState.DELETED);
        }

        @Test
        void 다른_사용자가_삭제하면_예외() {
            Post post = Post.builder()
                .title(title)
                .userId(999L)
                .categoryId(categoryId)
                .state(PostState.IN_PROGRESS)
                .likeCount(0L)
                .commentCount(0L)
                .build();
            ReflectionTestUtils.setField(post, "id", 201L);

            given(postRepository.findByIdAndState(201L, PostState.IN_PROGRESS)).willReturn(java.util.Optional.of(post));

            assertThatThrownBy(() -> postCommandService.deletePost(userId, 201L))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }
}
