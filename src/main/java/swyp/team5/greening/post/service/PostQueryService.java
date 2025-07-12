package swyp.team5.greening.post.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.repository.PostQueryRepository;
import swyp.team5.greening.post.dto.data.FindPostDto;
import swyp.team5.greening.post.dto.response.FindAllPostResponseDto;
import swyp.team5.greening.post.dto.response.FindMyAllPostResponseDto;
import swyp.team5.greening.post.dto.response.FindPostPreviewResponseDto;
import swyp.team5.greening.post.dto.response.FindPostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;
import swyp.team5.greening.postCategory.domain.entity.CategoryType;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.user.domain.entity.User;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostQueryRepository postQueryRepository;

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

    // 홈 화면 게시글
    @Transactional(readOnly = true)
    public List<FindPostPreviewResponseDto> findLatestPostByCategory() {
        return Stream.of(1L, 2L, 3L)
                .flatMap(categoryId ->
                        postQueryRepository.findTop6TodayByCategoryWithUserName(categoryId)
                                .stream())
                .toList();
    }

    // 카테고리별 게시글
    @Transactional(readOnly = true)
    public Page<FindAllPostResponseDto> findPostsByCategory(
            Long loginUserId,
            String categoryName,
            Integer pageNumber,
            Integer pageSize
    ) {
        CategoryType categoryType = CategoryType.of(categoryName.toUpperCase());

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return postQueryRepository.findAllByCategoryWithUserName(
                loginUserId, categoryType, pageRequest
        );
    }

    @Transactional(readOnly = true)
    public Page<FindMyAllPostResponseDto> findMyPosts(
            Long loginUserId,
            Integer pageNumber,
            Integer pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return postQueryRepository.findMyPosts(loginUserId, pageRequest);
    }
}
