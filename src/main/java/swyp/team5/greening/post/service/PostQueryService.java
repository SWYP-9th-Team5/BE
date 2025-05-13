package swyp.team5.greening.post.service;

import java.util.List;
import swyp.team5.greening.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.team5.greening.common.exception.GreeningGlobalException;
import swyp.team5.greening.post.domain.entity.Post;
import swyp.team5.greening.post.domain.entity.PostState;
import swyp.team5.greening.post.domain.repository.PostRepository;
import swyp.team5.greening.post.dto.response.PaginationDto;
import swyp.team5.greening.post.dto.response.PostPaginationResponseDto;
import swyp.team5.greening.post.dto.response.PostPreviewResponseDto;
import swyp.team5.greening.post.dto.response.PostResponseDto;
import swyp.team5.greening.post.exception.PostExceptionMessage;
import swyp.team5.greening.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 단일 조회
    @Transactional(readOnly = true)
    public PostResponseDto findPostDto(Long postId) {
        Post post = postRepository.findByIdAndState(postId, PostState.IN_PROGRESS)
            .orElseThrow(() -> new GreeningGlobalException(PostExceptionMessage.NOT_FOUND_POST));

        return PostResponseDto.from(post);
    }

    // 최신 게시글 6개씩 카테고리별로 조회
    @Transactional(readOnly = true)
    public List<PostPreviewResponseDto> getLatestPostByCategory(Long userId) {
        return List.of(1L, 2L, 3L).stream()
            .flatMap(categoryId ->
                postRepository.findTop6ByCategoryIdAndStateOrderByCreatedAtDesc(categoryId, PostState.IN_PROGRESS)
                    .stream()
                    .map(post -> {
                        String userName = userRepository.findById(post.getUserId())
                            .map(User::getUserName)
                            .orElse("탈퇴한 사용자");
                        return PostPreviewResponseDto.from(post, false, userName);
                    }))
            .toList();
    }

    // 카테고리 조회
    @Transactional(readOnly = true)
    public PostPaginationResponseDto getPostsByCategory(Long categoryId, int page, int size, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCategoryIdAndStateOrderByCreatedAtDesc(categoryId, PostState.IN_PROGRESS, pageRequest);

        List<PostPreviewResponseDto> data = posts.getContent().stream()
            .map(post -> {
                String userName = userRepository.findById(post.getUserId())
                    .map(User::getUserName)
                    .orElse("탈퇴한 사용자");
                return PostPreviewResponseDto.from(post, false, userName);
            })
            .toList();

        PaginationDto pagination = new PaginationDto(
            posts.getNumber(),
            posts.getSize(),
            posts.getTotalPages(),
            posts.getTotalElements()
        );

        return new PostPaginationResponseDto(data, pagination);
    }
}
