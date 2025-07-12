package swyp.team5.greening.post.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swyp.team5.greening.post.dto.data.FindPostDto;
import swyp.team5.greening.post.dto.response.FindAllPostResponseDto;
import swyp.team5.greening.post.dto.response.FindMyAllPostResponseDto;
import swyp.team5.greening.post.dto.response.FindPostPreviewResponseDto;
import swyp.team5.greening.postCategory.domain.entity.CategoryType;

public interface PostQueryRepository {

    Optional<FindPostDto> findPost(
            Long postId,
            Long userId
    );

    List<FindPostPreviewResponseDto> findTop6TodayByCategoryWithUserName(Long categoryId);

    Page<FindAllPostResponseDto> findAllByCategoryWithUserName(
            Long loginUserId,
            CategoryType categoryType,
            Pageable pageable
    );

    Page<FindMyAllPostResponseDto> findMyPosts(
            Long userId,
            Pageable pageable
    );

}
