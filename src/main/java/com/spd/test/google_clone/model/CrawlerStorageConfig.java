package com.spd.test.google_clone.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Configuration
public class CrawlerStorageConfig {

    private Path path;

    @PreDestroy
    private void deleteDirectory() throws IOException {
        FileSystemUtils.deleteRecursively(path);
    }

    @Bean
    public Path getPath() throws IOException {
        path = Files.createTempDirectory("CrawlerBase");
        return path;
    }
}

