package com.GujjuSajang.core.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TokenInfo {

    private String accessToken;
    private String refreshToken;
    private String prefix;

}
