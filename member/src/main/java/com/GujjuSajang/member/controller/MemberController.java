package com.GujjuSajang.member.controller;

import com.GujjuSajang.core.dto.TokenInfo;
import com.GujjuSajang.core.dto.TokenMemberInfo;
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

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpDto> signUp(@RequestBody @Valid MemberSignUpDto memberSignUpDto) {
        return ResponseEntity.ok().body(memberService.signUp(memberSignUpDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenMemberInfo> login(@RequestBody @Valid MemberLoginDto memberLoginDto, HttpServletResponse response) {
        return ResponseEntity.ok().body(memberService.login(memberLoginDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        TokenMemberInfo tokenMemberInfo = getTokenMemberInfo(request);
//        memberService.logout(tokenMemberInfo.getId());
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    // 토큰 재발급
//    @PostMapping("/token/refresh")
//    public ResponseEntity<TokenInfo> refreshToken(@RequestBody String refreshToken, HttpServletResponse response) {
//        TokenInfo tokenInfo = jwtService.refreshToken(refreshToken);
//        addCookie(response, tokenInfo.getAccessToken(), 30);
//        return ResponseEntity.ok(tokenInfo);
//    }

    // 메일 검증
//    @GetMapping("/mailVerified")
//    public void mailVerified(@RequestParam Long id, @RequestParam String code) {
//        memberService.verifiedMail(id, code);
//    }

    // 상세 정보 조회
    @GetMapping("/{member-id}/detail")
    public ResponseEntity<MemberUpdateDetailDto> getDetail(@PathVariable("member-id") long id) {
        return ResponseEntity.ok().body(memberService.getDetail(id));
    }

    // 정보 수정
//    @PatchMapping("/{member-id}/detail")
//    public ResponseEntity<MemberUpdateDetailDto> updateDetail(@PathVariable("member-id") Long id, @RequestBody @Valid MemberUpdateDetailDto memberUpdateDetailDto, HttpServletRequest request) {
//        TokenMemberInfo tokenMemberInfo = getTokenMemberInfo(request);
//        return ResponseEntity.ok().body(memberService.updateConsumer(id, tokenMemberInfo.getId(), memberUpdateDetailDto));
//    }

    // 비밀번호 수정
//    @PatchMapping("/{member-id}/detail/password")
//    public ResponseEntity<MemberUpdatePasswordDto.Response> updatePassword(@PathVariable("member-id") Long id, @RequestBody @Valid MemberUpdatePasswordDto memberUpdatePasswordDto, HttpServletRequest request) {
//        TokenMemberInfo tokenMemberInfo = getTokenMemberInfo(request);
//        return ResponseEntity.ok().body(memberService.updatePassword(id, tokenMemberInfo.getId(), memberUpdatePasswordDto));
//    }



}
