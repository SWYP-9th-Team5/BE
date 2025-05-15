package swyp.team5.greening.postLike.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.team5.greening.common.base.BaseTimeEntity;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
public class Like extends BaseTimeEntity {

    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    @Builder
    public Like(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

}
