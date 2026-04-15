package org.studyeasy.springstrater.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.studyeasy.springstrater.Model.Account;
import org.studyeasy.springstrater.Model.Post;
import org.studyeasy.springstrater.Services.AccountService;
import org.studyeasy.springstrater.Services.PostService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public String getPost(@PathVariable Long id, Model model, Principal principal) {
        Optional<Post> posOptional = postService.getPostById(id);

        String authUser = "email";
        if (posOptional.isPresent()) {
            Post post = posOptional.get();
            model.addAttribute("post", post);
            if (principal != null) {
                authUser = principal.getName();

            }
            if (authUser.equals(post.getAccount().getEmail())) {
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
            }

            return "post_views/post";
        } else {
            return "404";
        }
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")

    public String addPost(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> accountOptional = accountService.getAccountByEmail(authUser);
        if (accountOptional.isPresent()) {
            Post post = new Post();
            post.setAccount(accountOptional.get());
            post.setCreatedAt(LocalDateTime.now());
            model.addAttribute("post", post);
            return "post_views/add_post";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addPost(@Valid@ModelAttribute Post post, Principal principal,BindingResult result) {
        if(result.hasErrors()){
            return "post_views/add_post";

        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        if (post.getAccount().getEmail().compareTo(authUser) < 0) {
            return "redirect:/?error";
        }

        postService.savePost(post);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editPost(@PathVariable Long id, Model model) {
        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute("post", post);
            return "post_views/edit_post";
        }
        return "404";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String editPost(@Valid @ModelAttribute Post post,@PathVariable Long id,BindingResult result) {

        if(result.hasErrors()){
            return "post_views/edit_post";
        }

        Optional<Post> postOptional = postService.getPostById(id);
        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            postService.savePost(existingPost);
            return "redirect:/post/" + id;
        } else {

            return "404";
        }

    }
    @GetMapping("{id}/delete")
    public String getMethodName(@PathVariable Long id) {
        Optional<Post> optionalPost=postService.getPostById(id);
        if(optionalPost.isPresent()){
            Post post=optionalPost.get();
            postService.delete(post);
            return "redirect:/";
        }else{

            return "redirect:/?error";
        }
    }
    

}
