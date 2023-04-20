package com.springblogmicroservice.repository;

import com.springblogmicroservice.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findTagByName(String name);

    @Query(value = "SELECT id FROM tag",nativeQuery = true)
    List<Long> getAllIdsFromTable();
}
