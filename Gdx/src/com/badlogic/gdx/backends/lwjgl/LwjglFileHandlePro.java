package com.badlogic.gdx.backends.lwjgl;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author tomas
 */
public final class LwjglFileHandlePro extends FileHandle {

    public LwjglFileHandlePro(String fileName, FileType type) {
        super(fileName, type);
    }

    public LwjglFileHandlePro(File file, FileType type) {
        super(file, type);
    }

    public FileHandle child(String name) {
        if (file.getPath().length() == 0) {
            return new LwjglFileHandle(new File(name), type);
        }
        return new LwjglFileHandle(new File(file, name), type);
    }

    public FileHandle parent() {
        File parent = file.getParentFile();
        if (parent == null) {
            if (type == FileType.Absolute) {
                parent = new File("/");
            } else {
                parent = new File("");
            }
        }
        return new LwjglFileHandle(parent, type);
    }

    /**
     * Returns a stream for reading this file as bytes. @throw GdxRuntimeException if the file handle represents a directory, doesn't exist, or could not be
     * read.
     */
    @Override
    public InputStream read() {
        if (type == FileType.Classpath || (type == FileType.Internal && !file.exists())) {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + file.getPath().replace('\\', '/'));
            if (input == null) {
                throw new GdxRuntimeException("File not found: " + file + " (" + type + ")");
            }
            return input;
        }
        try {
            return new FileInputStream(file());
        } catch (Exception ex) {
            if (file().isDirectory()) {
                throw new GdxRuntimeException("Cannot open a stream to a directory: " + file + " (" + type + ")", ex);
            }
            throw new GdxRuntimeException("Error reading file: " + file + " (" + type + ")", ex);
        }
    }

    public File file() {
        if (type == FileType.External) {
            return new File(LwjglFiles.externalPath, file.getPath());
        }
        return file;
    }
}
