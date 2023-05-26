package com.example.facebooklight;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadService {

    private static final Path rootLocation = Paths.get("storage");

    public static String fileUpload(MultipartFile file) throws IOException {


        //Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));

        // Get the directory path where you want to save the file
        String directoryPath = rootLocation.toFile().getAbsolutePath();

        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        File fileToSave = new File(directoryPath +"/"+ file.getOriginalFilename());
        fileToSave.createNewFile();
        FileOutputStream fout = new FileOutputStream(fileToSave);
        fout.write(file.getBytes());
        return "images/" + file.getOriginalFilename();
    }
}
