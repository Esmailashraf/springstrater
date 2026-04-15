package org.studyeasy.springstrater.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.studyeasy.springstrater.Model.Post;
import org.studyeasy.springstrater.Repositories.PostRepository;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    public Optional<Post> getPostById(long id) {
        return postRepository.findById(id);
    }
    public void delete(@NonNull Post post) {
        postRepository.delete(post);
    }
    public List<Post> findAll() {
        return postRepository.findAll();
    }
    public Page<Post> findAll(int offset,int pageSize,String sortBy) {
        return postRepository.findAll(PageRequest.of(offset,pageSize).withSort(Direction.ASC,sortBy));
    }
    public Post savePost(Post post) {
        if(post.getId()==null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
}
