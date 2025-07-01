package swyp.team5.greening.post.infrastructure;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.team5.greening.post.dto.data.FindPostDto;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.repository.PostQueryRepository;
import swyp.team5.greening.post.dto.response.FindAllPostResponseDto;
import swyp.team5.greening.post.dto.response.FindMyAllPostResponseDto;
import swyp.team5.greening.post.dto.response.FindPostPreviewResponseDto;
import swyp.team5.greening.postCategory.domain.entity.CategoryType;

public interface PostJpaQueryRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    // 게시글 단일 조회
    @Override
    @Query("""
            SELECT new swyp.team5.greening.post.dto.data.FindPostDto(post, user, likes)
            FROM Post post
            LEFT JOIN User user
                ON user.id = post.userId
            LEFT JOIN Like likes
                ON likes.userId = :userId
                AND likes.postId = post.id
            WHERE post.id = :postId
            """)
    Optional<FindPostDto> findPost(
            @Param("postId") Long postId,
            @Param("userId") Long userId
    );

    // 홈 화면 오늘 작성된 게시글 중 카테고리별 좋아요 순 top 6
    @Override
    @Query("""
            SELECT new swyp.team5.greening.post.dto.response.FindPostPreviewResponseDto(
            post.id, post.categoryId, post.userId, user.userName, post.title, c.content,
            post.likeCount, post.commentCount, post.createdAt, post.updatedAt,
            case when likes.id is not null then true else false end)
            FROM Post post
            INNER JOIN User user
                ON user.id = post.userId
            LEFT JOIN PostContent c
                ON c.post = post
            AND c.sequence = 1
            LEFT JOIN Like likes
                ON likes.postId = post.id
                AND likes.userId = :loginUserId
            WHERE post.categoryId = :categoryId
                AND post.state = 'IN_PROGRESS'
            ORDER BY post.likeCount DESC
            LIMIT 6
            """)
    List<FindPostPreviewResponseDto> findTop6TodayByCategoryWithUserName(
            @Param("loginUserId") Long loginUserId,
            @Param("categoryId") Long categoryId
    );

    // 카테고리 별 조회
    @Override
    @Query("""
            SELECT new swyp.team5.greening.post.dto.response.FindAllPostResponseDto(
            post.id, post.categoryId, post.userId, user.userName, post.title,
            post.likeCount, post.commentCount, post.createdAt, post.updatedAt,
            case when likes.id is not null then true else false end)
            FROM Post post
            INNER JOIN User user
                ON user.id = post.userId
            LEFT JOIN Like likes
                ON likes.postId = post.id
                AND likes.userId = :loginUserId
            INNER JOIN Category category
                ON category.id = post.categoryId
                AND category.categoryType = :categoryType
            WHERE post.state = 'IN_PROGRESS'
            ORDER BY post.createdAt desc
            """)
    Page<FindAllPostResponseDto> findAllByCategoryWithUserName(
            @Param("loginUserId") Long loginUserId,
            @Param("categoryType") CategoryType categoryType,
            Pageable pageable
    );

    //특정 유저가 작성한 게시글 조회
    @Override
    @Query("""
            SELECT new swyp.team5.greening.post.dto.response.FindMyAllPostResponseDto(
            post.id, post.categoryId, post.title, post.likeCount,
            post.commentCount, post.createdAt, post.updatedAt,
            case when likes.id is not null then true else false end)
            FROM Post post
            LEFT JOIN Like likes
                ON likes.postId = post.id
                AND likes.userId = :loginUserId
            WHERE post.userId = :loginUserId
                AND post.state = 'IN_PROGRESS'
            ORDER BY post.createdAt desc
    """)
    Page<FindMyAllPostResponseDto> findMyPosts(
            @Param("loginUserId") Long loginUserId,
            Pageable pageable
    );

}
