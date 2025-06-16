package com.study.documentservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {
    private String url;

    private long bytes;
}
