package com.springblogmicroservice.repository;

import com.springblogmicroservice.entity.FollowedPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowPostRepository extends JpaRepository<FollowedPosts,Long> {
}
