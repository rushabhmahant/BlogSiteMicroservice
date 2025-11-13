package com.blogsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogSiteRepository extends JpaRepository<T, ID> {

}
