package swyp.team5.greening.postCategory.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    QnA("QnA"),
    FREE_BULLETIN_BOARD("자유게시판"),
    SHARING("나눔");


    private final String description;
}
