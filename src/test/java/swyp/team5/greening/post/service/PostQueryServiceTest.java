package swyp.team5.greening.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
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
import swyp.team5.greening.post.dto.response.PostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostQueryService 테스트")
class PostQueryServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostQueryService postQueryService;

    private final Long userId = 1L;
    private final String title = "테스트 게시글";
    private final Long categoryId = 5L;

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void findPost_success() {
        Post post = Post.builder()
            .title(title)
            .userId(userId)
            .categoryId(categoryId)
            .state(PostState.IN_PROGRESS)
            .likeCount(0L)
            .commentCount(0L)
            .build();
        ReflectionTestUtils.setField(post, "id", 200L);

        given(postRepository.findByIdAndState(200L, PostState.IN_PROGRESS)).willReturn(Optional.of(post));

        PostResponseDto result = postQueryService.findPostDto(200L);

        assertThat(result.postId()).isEqualTo(200L);
        assertThat(result.title()).isEqualTo(title);
    }

    @Test
    @DisplayName("게시글이 없으면 예외 발생")
    void findPost_notFound() {
        given(postRepository.findByIdAndState(999L, PostState.IN_PROGRESS)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postQueryService.findPostDto(999L))
            .isInstanceOf(GreeningGlobalException.class)
            .hasMessage(PostExceptionMessage.NOT_FOUND_POST.getMessage());
    }
}