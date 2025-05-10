package swyp.team5.greening.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import swyp.team5.greening.common.dto.response.ApiResponseDto;
import swyp.team5.greening.user.dto.request.LogInRequestDto;
import swyp.team5.greening.user.dto.response.LoginResponseDto;
import swyp.team5.greening.user.service.GoogleLogInService;
import swyp.team5.greening.user.service.KakaoLogInService;

@Tag(name = "유저 로그인 관련 API")
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LogInController {

    private final KakaoLogInService kakaoLogInService;
    private final GoogleLogInService googleLogInService;

    @Operation(summary = "카카오 로그인 API")
    @PostMapping("/kakao")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<LoginResponseDto> kakaoLogin(@RequestBody LogInRequestDto requestDto) {
        return ApiResponseDto.of(kakaoLogInService.kakaoLogin(requestDto.code()));
    }

    @Operation(summary = "구글 로그인 API")
    @PostMapping("/google")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<LoginResponseDto> googleLogin(@RequestBody LogInRequestDto requestDto) {
        return ApiResponseDto.of(googleLogInService.googleLogin(requestDto.code()));
    }

}
