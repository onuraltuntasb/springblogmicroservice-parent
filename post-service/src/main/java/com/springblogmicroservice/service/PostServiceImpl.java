package com.springblogmicroservice.service;


import com.springblogmicroservice.dto.PostListIdRequest;
import com.springblogmicroservice.dto.PostRequest;
import com.springblogmicroservice.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{


    @Override
    public Post savePost(PostRequest postRequest, Long userId) {

        //TODO wc post call tagIs is ok if its ok then wc post call write post-tag ids if its ok then write post with user-id

        List<PostListIdRequest> requestPostIds = postRequest.getTags();

//        if(requestPostIds!=null){
//            for (int i = 0; i < requestPostIds.size();i++){
//                log.info("postId : {}",requestPostIds.get(i).getId());
//
//                Long rId =requestPostIds.get(i).getId();
//                tags.add(tagRepository.findById(rId)
//                        .orElseThrow(()-> new ResourceNotFoundException("Tag not found with this id : "+rId )));
//
//            }
//        }




        return postRepository.save(post) ;
    }
}
