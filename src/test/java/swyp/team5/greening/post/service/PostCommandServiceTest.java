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

@DisplayName("게시글 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PostCommandServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostCreateService postCreateService;

    @DisplayName("게시글 생성 테스트")
    @Nested
    class createPostTest {

        Long userId = 1L;
        String title = "테스트 게시글 제목";
        Long categoryId = 5L;

        CreatePostRequestDto.ContentDto content1 = new CreatePostRequestDto.ContentDto("TEXT", "본문1");
        CreatePostRequestDto.ContentDto content2 = new CreatePostRequestDto.ContentDto("IMAGE", "http://image.com/test.jpg");

        List<CreatePostRequestDto.ContentDto> contentList = List.of(content1, content2);

        @Test
        @DisplayName("게시글을 성공적으로 작성할 수 있다")
        void testCase1() {
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
            verify(postRepository, times(1)).save(any(Post.class));
            assertThat(result.postId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("제목이 null이면 예외가 발생한다")
        void testCase2() {
            // when
            CreatePostRequestDto request = new CreatePostRequestDto(null, categoryId, contentList);

            // then
            assertThatThrownBy(() -> postCreateService.createPost(userId, request))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }

        @Test
        @DisplayName("본문이 비어있으면 예외가 발생한다")
        void testCase3() {
            // when
            CreatePostRequestDto request = new CreatePostRequestDto(title, categoryId, List.of());

            // then
            assertThatThrownBy(() -> postCreateService.createPost(userId, request))
                .isInstanceOf(GreeningGlobalException.class)
                .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }
}