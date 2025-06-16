package com.study.apigateway.dto.Document.Usage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageResponseDto {
    private long used;

    private long total;
}
