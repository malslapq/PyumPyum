package com.GujjuSajang.member.controller;

import com.GujjuSajang.Jwt.dto.TokenInfo;
import com.GujjuSajang.Jwt.dto.TokenMemberInfo;
import com.GujjuSajang.member.dto.MemberLoginDto;
import com.GujjuSajang.member.dto.MemberSignUpDto;
import com.GujjuSajang.member.dto.MemberUpdateDetailDto;
import com.GujjuSajang.member.dto.MemberUpdatePasswordDto;
import com.GujjuSajang.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.GujjuSajang.Jwt.util.JwtUtil.getTokenMemberInfo;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService consumerService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpDto> signUp(@RequestBody @Valid MemberSignUpDto memberSignUpDto) {
        return ResponseEntity.ok().body(consumerService.signUp(memberSignUpDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody @Valid MemberLoginDto memberLoginDto, HttpServletResponse response) {
        TokenInfo tokenInfo = consumerService.login(memberLoginDto);
        addCookie(response, tokenInfo.getAccessToken(), 60);
        return ResponseEntity.ok().body(tokenInfo);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        TokenMemberInfo tokenMemberInfo = getTokenMemberInfo(request);
        consumerService.logout(tokenMemberInfo.getId());
        addCookie(response, "", 0);
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    // 메일 검증
    @GetMapping("/mailVerified")
    public void mailVerified(@RequestParam Long id, @RequestParam String code) {
        consumerService.verifiedMail(id, code);
    }

    // 상세 정보 조회
    @GetMapping("/detail/{member-id}")
    public ResponseEntity<MemberUpdateDetailDto> getDetail(@PathVariable("member-id") long id) {
        return ResponseEntity.ok().body(consumerService.getDetail(id));
    }

    // 정보 수정
    @PatchMapping("/detail/{member-id}")
    public ResponseEntity<MemberUpdateDetailDto> updateDetail(@PathVariable("member-id") Long id, @RequestBody @Valid MemberUpdateDetailDto memberUpdateDetailDto, HttpServletRequest request) {
        TokenMemberInfo tokenMemberInfo = getTokenMemberInfo(request);
        return ResponseEntity.ok().body(consumerService.updateConsumer(id, tokenMemberInfo.getId(), memberUpdateDetailDto));
    }

    // 비밀번호 수정
    @PatchMapping("/detail/{member-id}/password")
    public ResponseEntity<MemberUpdatePasswordDto.Response> updatePassword(@PathVariable("member-id") Long id, @RequestBody @Valid MemberUpdatePasswordDto memberUpdatePasswordDto, HttpServletRequest request) {
        TokenMemberInfo tokenMemberInfo = getTokenMemberInfo(request);
        return ResponseEntity.ok().body(consumerService.updatePassword(id, tokenMemberInfo.getId(), memberUpdatePasswordDto));
    }

    private void addCookie(HttpServletResponse response, String accessToken, int expiryMinutes) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * expiryMinutes);
        response.addCookie(cookie);
    }

}
