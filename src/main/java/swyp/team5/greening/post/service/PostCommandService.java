package swyp.team5.greening.post.service;

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
import swyp.team5.greening.post.dto.response.CreatePostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PostCommandService {

    private final PostRepository postRepository;

    @Transactional
    public CreatePostResponseDto createPost(Long userId, CreatePostRequestDto requestDto) {
        if (requestDto.title() == null || requestDto.content() == null || requestDto.content().isEmpty()) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST);
        }

        Post post = Post.builder()
            .title(requestDto.title())
            .userId(userId)
            .categoryId(requestDto.categoryId())
            .state(PostState.IN_PROGRESS)
            .likeCount(0L)
            .commentCount(0L)
            .build();

        List<PostContent> contents = IntStream.range(0, requestDto.content().size())
            .mapToObj(i -> {
                var c = requestDto.content().get(i);
                return PostContent.of(
                    c.value(),
                    PostType.valueOf(c.type().toUpperCase()),
                    i + 1,
                    post
                );
            })
            .toList();

        post.getPostContents().addAll(contents);
        Post saved = postRepository.save(post);

        return new CreatePostResponseDto(saved.getId());
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)
            .orElseThrow(() -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        if (!post.getUserId().equals(userId)) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST); // 권한 예외
        }

        post.delete();
    }
}
