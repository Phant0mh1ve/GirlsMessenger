package org.example.dto.authDto;

import lombok.Data;

@Data
public class JwtAuthDTO {
    private String accessToken;
    private String refreshToken;
}
