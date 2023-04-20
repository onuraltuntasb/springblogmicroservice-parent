package com.springblogmicroservice.repository;

import com.springblogmicroservice.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag,Long> {
    @Query(value = "CREATE TABLE posttag(post_id,tag_id)",nativeQuery = true)
    void checkTable();

    @Query(value = "INSERT INTO posttag(post_id,tag_id) VALUES (postId,tagId)",nativeQuery = true)
    void savePostTag(Long postId,Long tagId);

}
