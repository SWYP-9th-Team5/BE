package swyp.team5.greening.post.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostQueryRepository;
import swyp.team5.greening.post.dto.PostUserNameProjection;
import swyp.team5.greening.post.dto.data.FindPostDto;
import swyp.team5.greening.post.dto.response.PostPreviewResponseDto;
import swyp.team5.greening.post.dto.response.FindPostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;
import swyp.team5.greening.postCategory.domain.entity.Category;
import swyp.team5.greening.postCategory.domain.entity.CategoryType;
import swyp.team5.greening.postCategory.domain.repository.CategoryRepository;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.user.domain.entity.User;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostQueryRepository postQueryRepository;
    private final CategoryRepository categoryRepository;

    // 단일 조회
    @Transactional(readOnly = true)
    public FindPostResponseDto findPost(Long postId, Long userId) {
        FindPostDto postDto = postQueryRepository.findPost(postId, userId)
                .orElseThrow(
                        () -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        Post post = postDto.post();
        User postWriteuser = postDto.user();
        Like logInUserlike = postDto.like();

        return FindPostResponseDto.of(post, postWriteuser, !Objects.isNull(logInUserlike),
                Objects.equals(userId, postWriteuser.getId()));
    }

    // 최신 게시글 6개씩 카테고리별로 조회
    @Transactional(readOnly = true)
    public List<PostPreviewResponseDto> getLatestPostByCategory() {
        return List.of(1L, 2L, 3L).stream()
                .flatMap(categoryId ->
                        postQueryRepository.findTop6TodayByCategoryWithUserName(categoryId).stream()
                                .map(proj -> PostPreviewResponseDto.from(proj, false))
                )
                .toList();
    }

    // 카테고리 조회
    @Transactional(readOnly = true)
    public Page<PostPreviewResponseDto> getPostsByCategory(
            String categoryName,
            Integer pageNumber, Integer pageSize) {
        CategoryType categoryType;
        try {
            categoryType = CategoryType.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_CATEGORY);
        }

        Long categoryId = categoryRepository.findByCategoryType(categoryType)
                .map(Category::getId)
                .orElseThrow(
                        () -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_CATEGORY));

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<PostUserNameProjection> postPage = postQueryRepository.findAllByCategoryWithUserName(
                categoryId,
                PostState.IN_PROGRESS.name(),
                pageRequest
        );

        return postPage.map(proj -> PostPreviewResponseDto.from(proj, false));
    }
}
