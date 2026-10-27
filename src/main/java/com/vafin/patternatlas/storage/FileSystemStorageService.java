package com.vafin.patternatlas.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path storePath;

    public FileSystemStorageService(@Value("${storage.location}") String location) {
        this.storePath = Path.of(location);
        try {
            Files.createDirectories(this.storePath);
        } catch (IOException e) {
            throw new StorageException("Could not create Storage Directory", e);
        }
    }

    public String store(MultipartFile file) {
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + fileExtension;
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.storePath.resolve(fileName));
        } catch (IOException e) {
            throw new StorageException("Could not store file " + fileName, e);
        }
        return fileName;
    }

    public Resource load(String filename) {
        try {
            Resource resource = new UrlResource(this.storePath.resolve(filename).toUri());
            if (!resource.exists()) {
                throw new StorageFileNotFoundException(filename);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new StorageException("Could not load file " + filename, e);
        }
    }
}
