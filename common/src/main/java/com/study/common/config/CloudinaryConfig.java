package com.study.common.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Path dotenvDir = Paths.get(System.getProperty("user.dir"));
        if (!Files.exists(dotenvDir.resolve(".env"))) {
            dotenvDir = dotenvDir.getParent();
        }

        Dotenv dotenv = Dotenv.configure()
                .directory(dotenvDir.toString())
                .filename(".env")
                .load();

        String url = dotenv.get("CLOUDINARY_URL");
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Missing CLOUDINARY_URL in .env");
        }

        return new Cloudinary(url);
    }
}

