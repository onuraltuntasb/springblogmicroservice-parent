package com.springblogmicroservice.controller;

import com.springblogmicroservice.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/post-tag")
@RequiredArgsConstructor
public class PostTagController {

    PostTagRepository postTagRepository;

    @GetMapping("/save")
    public ResponseEntity<Boolean> savePost(HashMap<Long,Long> map){

        //TODO move to service later

        if(map == null){
            return ResponseEntity.badRequest().body(false);
        }

        for (Map.Entry<Long, Long> set :
                map.entrySet()) {

            postTagRepository.savePostTag(set.getKey(),set.getValue());

            System.out.println(set.getKey() + " = "
                    + set.getValue());
        }

        return ResponseEntity.ok().body(true);
    }




}
