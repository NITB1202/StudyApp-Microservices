package com.study.apigateway.dto.Auth.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthUserInfo {
    private String id;

    private String name;

    private String email;

    private String picture;

    private String gender;

    private String birthday;
}
