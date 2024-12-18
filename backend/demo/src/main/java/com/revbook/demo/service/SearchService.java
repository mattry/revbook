package com.revbook.demo.service;

import com.revbook.demo.dto.PostDTO;
import com.revbook.demo.dto.SearchResultDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.User;
import com.revbook.demo.repository.PostRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    public SearchResultDTO performSearch(String searchTerm) {
        // Search and map users
        List<User> userList = userRepository.searchUsersByName(searchTerm);
        List<UserDTO> users = new ArrayList<>();
        for (User user : userList) {
            if (user != null) {
                // we'll use the full constructor with the email as null  to avoid sending private information to the frontend
                UserDTO userDTO = new UserDTO(user.getUserId(), null, user.getFirstName(), user.getLastName());
                users.add(userDTO);
            }
        }

        // Search and map posts
        List<Post> postList = postRepository.searchPostsByText(searchTerm);
        List<PostDTO> posts = getPostDTOS(postList);

        // Combine results
        return new SearchResultDTO(users, posts);

    }

    private static List<PostDTO> getPostDTOS(List<Post> postList) {
        List<PostDTO> posts = new ArrayList<>();
        for (Post post : postList) {
            if (post != null) {
                PostDTO postSearchDTO = new PostDTO(
                        post.getPostId(),
                        post.getPoster().getUserId(),
                        post.getPostText(),
                        post.getTimePosted(),
                        post.getPoster().getFirstName(),
                        post.getPoster().getLastName()
                        );
                posts.add(postSearchDTO);
            }
        }
        return posts;
    }
}
