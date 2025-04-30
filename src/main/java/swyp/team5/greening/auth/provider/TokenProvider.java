package swyp.team5.greening.auth.provider;

public interface TokenProvider {

    //토큰 생성
    String createToken(Long id);

    //토큰 페이로드 조회
    Long getPayLoad(String token);
}
