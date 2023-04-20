package com.springblogmicroservice.setup;


import com.springblogmicroservice.entity.Privilege;
import com.springblogmicroservice.entity.Role;
import com.springblogmicroservice.repository.PostTagRepository;
import com.springblogmicroservice.repository.PrivilegeRepository;
import com.springblogmicroservice.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    PostTagRepository postTagRepository;

    boolean alreadySetup = false;
    //TODO learn how to active hear on ci/cd

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        postTagRepository.checkTable();

        alreadySetup = true;
    }


}
