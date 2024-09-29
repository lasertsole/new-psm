package com.psm.infrastructure.utils.Tus;

import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class TargetFolderPathContextHolder {
    private static final ThreadLocal<Path> targetFolderPathHolder = new ThreadLocal<>();
    public static void setTargetFolderPath(Path path) {
        targetFolderPathHolder.set(path);
    }

    public static Path getTargetFolderPath() {
        return targetFolderPathHolder.get();
    }

    public static void removeTargetFolderPath() {
        targetFolderPathHolder.remove();
    }
}
