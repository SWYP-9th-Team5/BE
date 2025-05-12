package swyp.team5.greening.post.service;


import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class PostCreateService {

    private final PostRepository postRepository;

    @Transactional
    public CreatePostResponseDto createPost(Long userId, CreatePostRequestDto requestDto) {

        // 예외 처리
        if (requestDto.title() == null || requestDto.content() == null || requestDto.content().isEmpty()) {
            throw new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST);
        }

        // 게시글 객체 생성
        Post post = Post.builder()
            .title(requestDto.title())
            .userId(userId)
            .categoryId(requestDto.categoryId())
            .state(PostState.IN_PROGRESS)
            .likeCount(0L)
            .commentCount(0L)
            .build();

        // 게시글 내용 리스트 생성
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

        post.getPostImages().addAll(contents);
        Post saved = postRepository.save(post);

        return new CreatePostResponseDto(saved.getId());
    }
}