package swyp.team5.greening.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.comment.domain.repository.CommentQueryRepository;
import swyp.team5.greening.comment.dto.response.FindAllCommentResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentQueryRepository commentQueryRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<FindAllCommentResponseDto> findAllComment(
            Long userId, Long postId,
            Integer pageNumber, Integer pageSize
    ) {

        //게시물 존재 여부 확인
        if (!postRepository.existsById(postId)) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST);
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return commentQueryRepository.findAllComment(userId, postId, pageRequest);
    }

}
