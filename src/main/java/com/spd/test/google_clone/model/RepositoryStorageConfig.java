package com.spd.test.google_clone.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileSystemUtils;


import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class RepositoryStorageConfig {

    private Path tempPath;

    @PreDestroy
    private void deleteDirectory() throws IOException {
        FileSystemUtils.deleteRecursively(tempPath);
    }

    @Bean
    public Path getRepositoryStoragePath() throws IOException {
        tempPath = Files.createTempDirectory("index_base");
        return tempPath;
    }
}
