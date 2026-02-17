package com.NextStepEdu.services.impl;

import com.NextStepEdu.models.AccountStatus;
import com.NextStepEdu.models.UserModel;
import com.NextStepEdu.repositories.UserRepository;
import com.NextStepEdu.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public void updateUserStatus(Integer userId, AccountStatus status) {

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        user.setStatus(status);
        userRepository.save(user);
    }
}
