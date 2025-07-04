package swyp.team5.greening.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

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
import swyp.team5.greening.post.dto.PostContentDto;
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
    private final List<PostContentDto> contentList = List.of(
            new PostContentDto("TEXT", "본문1"),
            new PostContentDto("IMAGE", "http://image.com/test.jpg")
    );

    @Nested
    @DisplayName("게시글 생성")
    class CreatePost {

        @Test
        void 성공적으로_생성한다() {
            //given
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

            //when
            CreatePostResponseDto result = postCommandService.createPost(
                    userId, new CreatePostRequestDto(title, categoryId, contentList)
            );

            //then
            assertThat(result.postId()).isEqualTo(100L);
            verify(postRepository).save(any(Post.class));
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

            given(postRepository.findByIdAndState(200L, PostState.IN_PROGRESS)).willReturn(
                    java.util.Optional.of(post));

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

            given(postRepository.findByIdAndState(201L, PostState.IN_PROGRESS)).willReturn(
                    java.util.Optional.of(post));

            assertThatThrownBy(() -> postCommandService.deletePost(userId, 201L))
                    .isInstanceOf(GreeningGlobalException.class)
                    .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
        }
    }
}
