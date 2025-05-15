package swyp.team5.greening.postLike.service;

import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.exception.PostExceptionMessage;
import swyp.team5.greening.postLike.domain.entity.Like;
import swyp.team5.greening.postLike.domain.repository.LikeRepository;
import swyp.team5.greening.postLike.dto.PostLikeResponseDto;

//todo: 게시글 좋아요 수 상태 변화 -> 이벤트 처리
//todo: 동시성 문제 처리
@Service
@RequiredArgsConstructor
public class PostLikeCommandService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    //좋아요 혹은 좋아요 취소
    @Transactional
    public PostLikeResponseDto likeOrCancel(Long userId, Long postId) {
        //게시글 조회
        Post post = postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)
                .orElseThrow(
                        () -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        //게시글 좋아요 여부 확인
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElse(Like.builder()
                        .userId(userId)
                        .postId(postId)
                        .build());

        boolean isLike = false;

        //해당 게시글의 좋아요 하지 않은 상태 -> 좋아요 처리 및 게시글 좋아요 수 증가
        if (Objects.isNull(like.getId())) {
            likeRepository.save(like);
            post.increaseLikeCount();
            isLike = true;
        }
        //해당 게시글의 좋아요 하지 않은 상태 -> 좋아요 취소 처리 및 게시글 좋아요 수 감소
        else {
            likeRepository.deleteById(like.getId());
            post.decreaseLikeCount();
        }

        return new PostLikeResponseDto(isLike);
    }

}
