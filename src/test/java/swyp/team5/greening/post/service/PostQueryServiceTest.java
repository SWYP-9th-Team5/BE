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
import swyp.team5.greening.post.domain.repository.PostQueryRepository;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.response.FindPostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;
import swyp.team5.greening.postCategory.domain.entity.Category;
import swyp.team5.greening.postCategory.domain.repository.CategoryRepository;
import swyp.team5.greening.user.domain.entity.User;
import swyp.team5.greening.user.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostQueryService 테스트")
class PostQueryServiceTest {

    @Mock
    private PostQueryRepository postQueryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostQueryService postQueryService;

    private final Long userId = 1L;
    private final String title = "테스트 게시글";
    private final Long categoryId = 5L;

    private Post mockPost(Long id) {
        Post post = Post.builder()
            .title(title)
            .userId(userId)
            .categoryId(categoryId)
            .state(PostState.IN_PROGRESS)
            .likeCount(0L)
            .commentCount(0L)
            .build();
        ReflectionTestUtils.setField(post, "id", id);
        return post;
    }

    private User mockUser(Long id, String name) {
        User user = User.builder()
            .userName(name)
            .nickname(name)
            .email("test@email.com")
            .loginType(null)
            .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Category mockCategory(Long id) {
        Category category = new Category();
        ReflectionTestUtils.setField(category, "id", id);
        return category;
    }


//    @Test
//    @DisplayName("카테고리별 게시글 목록 조회 (페이징 포함)")
//    void getPostsByCategory_success() {
//        String categoryName = "SHARING";
//        Post post = mockPost(1L);
//        Page<Post> page = new PageImpl<>(List.of(post));
//
//        given(categoryRepository.findByCategoryType(CategoryType.SHARING))
//            .willReturn(Optional.of(mockCategory(categoryId)));
//
//        given(postRepository.findAllByCategoryWithUserName(eq(categoryId), eq(PostState.IN_PROGRESS.name()), any(PageRequest.class)))
//            .willReturn(page);
//
//        given(userRepository.findById(post.getUserId()))
//            .willReturn(Optional.of(mockUser(post.getUserId(), "테스트유저")));
//
//        PaginationApiResponseDto<PostPreviewResponseDto> result =
//            postQueryService.getPostsByCategory(categoryName, 0, 10);
//
//        assertThat(result.data()).hasSize(1);
//        assertThat(result.data().get(0).userName()).isEqualTo("테스트유저");
//    }
//
//    @Test
//    @DisplayName("홈: 카테고리별 최신 게시글 6개 조회 성공")
//    void getLatestPostByCategory_success() {
//        Post post = mockPost(1L);
//        given(postRepository.findTop6TodayByCategoryWithUserName(anyLong(), eq(PostState.IN_PROGRESS)))
//            .willReturn(List.of(post));
//
//        given(userRepository.findById(userId))
//            .willReturn(Optional.of(mockUser(userId, "홈유저")));
//
//        var result = postQueryService.getLatestPostByCategory(userId);
//
//        assertThat(result).isNotEmpty();
//        assertThat(result.get(0).userName()).isEqualTo("홈유저");
//    }
}
