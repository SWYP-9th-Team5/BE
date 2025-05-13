package swyp.team5.greening.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.response.PostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public PostResponseDto findPostDto(Long postId) {
        Post post = postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)
            .orElseThrow(() -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        return PostResponseDto.from(post);
    }
}
