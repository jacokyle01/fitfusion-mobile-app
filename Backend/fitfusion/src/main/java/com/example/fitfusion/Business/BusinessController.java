package com.example.fitfusion.Business;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.fitfusion.Image.Image;
import com.example.fitfusion.Image.ImageRepository;
import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Post.BusinessPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fitfusion.Post.BusinessPostRepository;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BusinessController {

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    BusinessPostRepository businessPostRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/businesses")
    List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    @GetMapping("/businesses/@/{businessName}")
    Business findBusiness(@PathVariable String businessName) {
        return businessRepository.findByBusinessName(businessName);
    }

    @PostMapping("/businesses")
    String addBusiness(@RequestBody Business business) {
        if (business == null) {
            return failure;
        }

        businessRepository.save(business);
        return success;
    }

    @GetMapping("businesses/@/{businessName}/posts")
    List<BusinessPost> getBusinessPosts(@PathVariable String businessName) {
        Business business = businessRepository.findByBusinessName(businessName);
        return business.getPosts();
    }

    @Transactional
    @PostMapping("/businesses/@/{businessName}/posts")
    String postToBusinessPage(@RequestBody BusinessPost businessPost, @PathVariable String businessName) {
        Business business = businessRepository.findByBusinessName(businessName);
        businessPost.setBusiness(business);
        businessPostRepository.save(businessPost);
        business.addPost(businessPost);
        return success;
    }

    @GetMapping("/businesses/@/{businessName}/isBanned")
    boolean getIsBanned(@PathVariable String businessName) {
        Business business = businessRepository.findByBusinessName(businessName);
        return business.isBanned();
    }

    @PostMapping("/businesses/@/{businessName}/ban")
    String banBusiness(@PathVariable String businessName) {
        Business business = businessRepository.findByBusinessName(businessName);
        business.setBanned(true);
        businessRepository.save(business);
        return "banned " + businessName;
    }

    @PostMapping("/businesses/@/{businessName}/unban")
    String unbanBusiness(@PathVariable String businessName) {
        Business business = businessRepository.findByBusinessName(businessName);
        business.setBanned(false);
        businessRepository.save(business);
        return "unbanned " + businessName;
    }

}
