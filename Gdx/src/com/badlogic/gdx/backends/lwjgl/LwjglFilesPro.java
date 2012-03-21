package com.badlogic.gdx.backends.lwjgl;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;

/**
 * @author tomas
 */
public final class LwjglFilesPro implements Files {

    static public final String externalPath = System.getProperty("user.home") + "/";

    @Override
    public FileHandle getFileHandle(String fileName, FileType type) {
        return new LwjglFileHandlePro(fileName, type);
    }

    @Override
    public FileHandle classpath(String path) {
        return new LwjglFileHandlePro(path, FileType.Classpath);
    }

    @Override
    public FileHandle internal(String path) {
        return new LwjglFileHandlePro(path, FileType.Internal);
    }

    @Override
    public FileHandle external(String path) {
        return new LwjglFileHandlePro(path, FileType.External);
    }

    @Override
    public FileHandle absolute(String path) {
        return new LwjglFileHandlePro(path, FileType.Absolute);
    }

    @Override
    public String getExternalStoragePath() {
        return externalPath;
    }

    @Override
    public boolean isExternalStorageAvailable() {
        return true;
    }
}
