package com.NextStepEdu.dto.requests;

import com.NextStepEdu.models.AccountStatus;

public record UpdateStatusRequest(
        AccountStatus status
) {}