package swyp.team5.greening.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.exception.PostExceptionMessage;

@Service
@RequiredArgsConstructor
public class PostDeleteService {

    private final PostRepository postRepository;

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)
            .orElseThrow(() -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        if (!post.getUserId().equals(userId)) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST); // 권한 예외 분리해도 됨
        }

        post.changeState(PostState.DELETED);
    }

}