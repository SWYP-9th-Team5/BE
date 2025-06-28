package swyp.team5.greening.post.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostContent;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.entity.PostType;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.request.CreatePostRequestDto;
import swyp.team5.greening.post.dto.request.UpdatePostRequestDto;
import swyp.team5.greening.post.dto.response.CreatePostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;

@Service
@RequiredArgsConstructor
public class PostCommandService {

    private final PostRepository postRepository;

    @Transactional
    public CreatePostResponseDto createPost(
            Long userId,
            CreatePostRequestDto requestDto
    ) {
        Post post = Post.builder()
                .title(requestDto.title())
                .userId(userId)
                .categoryId(requestDto.categoryId())
                .state(PostState.IN_PROGRESS)
                .likeCount(0L)
                .commentCount(0L)
                .build();

        List<PostContent> contents = requestDto.content().stream()
                .map(dto -> PostContent.builder()
                        .content(dto.value())
                        .type(PostType.valueOf(dto.type().toUpperCase()))
                        .build())
                .toList();

        post.updateContent(contents);

        Post saved = postRepository.save(post);

        return new CreatePostResponseDto(saved.getId());
    }

    //게시글 수정
    //1. 게시글 조회
    //2. 권한 확인
    //3. 게시글 제목 수정
    //4. 본문 삭제
    //5. 새로운 본문 저장
    @Transactional
    public void updatePost(
            Long userId,
            UpdatePostRequestDto requestDto
    ) {
        Post post = postRepository.findByIdAndState(requestDto.postId(), PostState.IN_PROGRESS)
                .orElseThrow(
                        () -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        if (!Objects.equals(post.getUserId(), userId)) {
            throw new GreeningGlobalException(PostExceptionMessage.BAD_REQUEST_POST_WRITER);
        }

        post.updateTitle(requestDto.title());

        List<PostContent> contents = requestDto.content().stream()
                .map(dto -> PostContent.builder()
                        .content(dto.value())
                        .type(PostType.valueOf(dto.type().toUpperCase()))
                        .build())
                .toList();

        post.updateContent(contents);
    }

    @Transactional
    public void deletePost(
            Long userId,
            Long postId
    ) {
        Post post = postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)
                .orElseThrow(
                        () -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        if (!Objects.equals(post.getUserId(), userId)) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST); // 권한 예외
        }

        post.delete();
    }
}
