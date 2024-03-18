package com.example.fitfusion.Post;

import java.util.List;
import java.util.Optional;

import com.example.fitfusion.Business.Business;
import com.example.fitfusion.Business.BusinessRepository;
import com.example.fitfusion.Image.Image;
import com.example.fitfusion.Image.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BusinessPostController {
    @Autowired
    BusinessPostRepository businessPostRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/posts") 
    public List<BusinessPost> getAllPosts() {
        return businessPostRepository.findAll();
    }

    @PostMapping("/posts/{businessName}/withImage/{imageID}")
    //public ResponseEntity<String> postPersonPostWithImage(@PathVariable String businessName, @RequestBody BusinessPost businessPost, @RequestParam("file") MultipartFile file)
    public ResponseEntity<String> postPersonPostWithImage(@PathVariable String businessName, @PathVariable Long imageID, @RequestBody BusinessPost businessPost)
    {

        if(businessPost == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Business post null");
        }

        if(businessRepository.findByBusinessName(businessName) == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Business name not found");
        }

//        try {
//            Image image = new Image();
//            image.setName(file.getOriginalFilename());
//            image.setData(file.getBytes());
//            imageRepository.save(image);
//            Business tempBusiness = businessRepository.findByBusinessName(businessName);
//            businessPost.setBusiness(tempBusiness);
//            businessPost.setImage(image);
//            businessPostRepository.save(businessPost);
//            tempBusiness.addPost(businessPost);
//            businessRepository.save(tempBusiness);
//            return ResponseEntity.status(HttpStatus.OK).body("Image uploaded successfully");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
//        }

        Optional<Image> optionalImage = imageRepository.findById(imageID);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            Business tempBusiness = businessRepository.findByBusinessName(businessName);
            businessPost.setBusiness(tempBusiness);
            businessPost.setImage(image);
            businessPost.setImageID(imageID);
            businessPostRepository.save(businessPost);
            tempBusiness.addPost(businessPost);
            businessRepository.save(tempBusiness);
            return ResponseEntity.status(HttpStatus.OK).body("Post Successful");
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping("/posts/findByID/{id}")
    public BusinessPost findBusinessPostByID(@PathVariable Long id)
    {
        Optional<BusinessPost> optionalBusinessPost = businessPostRepository.findById(id);
        if(optionalBusinessPost.isPresent())
        {
            BusinessPost businessPost = optionalBusinessPost.get();
            return businessPost;
        }
        else
        {
            return null;
        }
    }
    
}
