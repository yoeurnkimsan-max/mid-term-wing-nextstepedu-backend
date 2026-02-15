package com.NextStepEdu.services;

import com.NextStepEdu.models.AccountStatus;

public interface UserService {
    void updateUserStatus(Integer userId, AccountStatus status);
}
