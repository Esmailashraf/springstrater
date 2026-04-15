package org.studyeasy.springstrater.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studyeasy.springstrater.Model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
}
