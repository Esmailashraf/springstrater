package org.studyeasy.springstrater.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.springstrater.Model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
}
