package com.springblogmicroservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository {
    @Query(value = "CREATE TABLE posttag(post_id,tag_id)",nativeQuery = true)
    void checkTable();

    @Query(value = "INSERT INTO posttag(post_id,tag_id) VALUES (postId,tagId)",nativeQuery = true)
    void savePostTag(Long postId,Long tagId);

}
