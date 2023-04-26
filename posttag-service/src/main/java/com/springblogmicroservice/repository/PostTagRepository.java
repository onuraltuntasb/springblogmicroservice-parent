package com.springblogmicroservice.repository;

import com.springblogmicroservice.entity.PostTag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag,Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO posttag(post_id,tag_id) VALUES (:post_id,:tag_id)",nativeQuery = true)
    void savePostTag(@Param("post_id")Long postId, @Param("tag_id")Long tagId);

}
