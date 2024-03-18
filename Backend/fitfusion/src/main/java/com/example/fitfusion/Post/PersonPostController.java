package com.example.fitfusion.Post;

import com.example.fitfusion.Business.Business;
import com.example.fitfusion.Image.Image;
import com.example.fitfusion.Image.ImageRepository;
import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class PersonPostController {

    @Autowired
    PersonPostRepository personPostRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/posts/people")
    public List<PersonPost> getAllPosts()
    {
        return personPostRepository.findAll();
    }

    @GetMapping("/posts/{username}/feed")
    public List<Post> getUserFeed(@PathVariable String username)
    {

        Person currentUser = personRepository.findByUsername(username);

        Set<Person> friends = currentUser.getFriends();
        Business gym = currentUser.getMyGym();

        ArrayList<Post> feed = new ArrayList<Post>();

        for(Person friend : friends)
        {
            List<PersonPost> friendPosts = friend.getPersonPosts();
            Post post1 = null;
            Post post2 = null;
            if(friendPosts.size() >= 1) {
                post1 = friendPosts.get(friendPosts.size() - 1);
                if(post1 != null) feed.add(post1);
            }
            if(friendPosts.size() >= 2){
                post2 = friendPosts.get(friendPosts.size() - 2);
                if(post2 != null) feed.add(post2);
            }
        }

        if(gym != null) {
            List<BusinessPost> gymPosts = gym.getPosts();
            Post gymPost1 = null;
            Post gymPost2 = null;
            if(gymPosts.size() >= 1) {
                gymPost1 = gymPosts.get(gymPosts.size() - 1);
                if(gymPost1 != null) feed.add(gymPost1);
            }
            if(gymPosts.size() >= 2) {
                gymPost2 = gymPosts.get(gymPosts.size() - 2);
                if(gymPost2 != null) feed.add(gymPost2);
            }
        }

        return feed;

    }

    @GetMapping("/posts/{username}")
    public List<PersonPost> getPersonPosts(@PathVariable String username)
    {
        return personRepository.findByUsername(username).getPersonPosts();
    }

    @PostMapping("/posts/{username}")
    public String postPersonPosts(@PathVariable String username, @RequestBody PersonPost personPost)
    {

        if(personPost == null)
        {
            return "failure";
        }

        Person curPerson = personRepository.findByUsername(username);

        personPost.setPerson(curPerson);
        personPostRepository.save(personPost);

        curPerson.addPersonPost(personPost);
        personRepository.save(curPerson);

        return "Success!";
    }

    @PostMapping("/posts/person/{username}/withImage/{imageID}")
    public ResponseEntity<String> postPersonPostWithImage(@PathVariable String username, @PathVariable Long imageID, @RequestBody PersonPost personPost)
    {

        if(personPost == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person post null");
        }

        if(personRepository.findByUsername(username) == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person name not found");
        }

        Optional<Image> optionalImage = imageRepository.findById(imageID);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            Person tempPerson = personRepository.findByUsername(username);
            personPost.setPerson(tempPerson);
            personPost.setPersonImage(image);
            personPost.setPersonImageID(imageID);
            personPostRepository.save(personPost);
            tempPerson.addPersonPost(personPost);
            personRepository.save(tempPerson);
            return ResponseEntity.status(HttpStatus.OK).body("Post Successful");
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/posts/person/findByID/{id}")
    public PersonPost findBusinessPostByID(@PathVariable Long id)
    {
        Optional<PersonPost> optionalPersonPost = personPostRepository.findById(id);
        if(optionalPersonPost.isPresent())
        {
            PersonPost personPost = optionalPersonPost.get();
            return personPost;
        }
        else
        {
            return null;
        }
    }

    @PutMapping("/posts/person/{id}/like/{person}")
    public ResponseEntity<String> incrementLike(@PathVariable Long id, @PathVariable String person)
    {
        PersonPost pp = findBusinessPostByID(id);
        if(pp != null)
        {

            if(pp.getLikedBy() != null && pp.getLikedBy().contains(person))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure, user already in set");
            }

            int likes = pp.getLikes();
            likes++;
            pp.setLikes(likes);
            if(pp.getLikedBy() == null)
            {
                Set<String> likedBy = new HashSet<>();
                pp.setLikedBy(likedBy);
            }
            pp.addLiker(person);
            personPostRepository.save(pp);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure");
    }

    @PutMapping("/posts/person/{id}/unlike/{person}")
    public ResponseEntity<String> decrementLike(@PathVariable Long id, @PathVariable String person)
    {
        PersonPost pp = findBusinessPostByID(id);
        if(pp != null)
        {

            if(pp.getLikedBy() != null && !pp.getLikedBy().contains(person))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure, user not in set");
            }

            int likes = pp.getLikes();
            likes--;
            pp.setLikes(likes);
            pp.removeLiker(person);
            personPostRepository.save(pp);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure");
    }

    @GetMapping("/posts/person/{id}/like/{person}")
    public ResponseEntity<String> findIfPersonLiked(@PathVariable Long id, @PathVariable String person)
    {
        PersonPost pp = findBusinessPostByID(id);
        if(pp == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post Null");

        if(pp.getLikedBy().contains(person))
        {
            return ResponseEntity.status(HttpStatus.OK).body("liked");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body("not liked");
        }

    }

}
