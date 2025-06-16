package com.study.apigateway.dto.Document.Folder.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFolderRequestDto {
    @NotEmpty(message = "Name is required.")
    private String name;
}
