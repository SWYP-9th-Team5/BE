package swyp.team5.greening.post.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swyp.team5.greening.post.dto.PostUserNameProjection;
import swyp.team5.greening.post.dto.data.FindPostDto;

public interface PostQueryRepository {

    Optional<FindPostDto> findPost(Long postId, Long userId);

    List<PostUserNameProjection> findTop6TodayByCategoryWithUserName(
            Long categoryId
    );

    Page<PostUserNameProjection> findAllByCategoryWithUserName(
            Long categoryId,
            String state,
            Pageable pageable
    );

}
