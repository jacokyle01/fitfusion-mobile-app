package com.example.fitfusion.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/upload")
    //public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
    public Long handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setData(file.getBytes());
            imageRepository.save(image);
            return image.getId();
        } catch (IOException e) {
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
            return -1L;
        }
    }

    @GetMapping("/retrieve/{id}")
    public ResponseEntity<byte[]> retrieveImage(@PathVariable Long id) {
        Optional<Image> optionalImage = imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
