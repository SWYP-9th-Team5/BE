package swyp.team5.greening.post.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "comment_count")
    private Long commentCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private PostState state;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private final List<PostContent> postImages = new ArrayList<>();

    @Builder
    public Post(
            String title,
            Long likeCount,
            Long commentCount,
            PostState state,
            Long categoryId,
            Long userId
    ) {
        this.title = title;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.state = state;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public void delete() {
        this.state = PostState.DELETED;
    }
}
