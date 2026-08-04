package com.example.resortbackendapplication1.auth.service;

import com.example.resortbackendapplication1.auth.dto.request.role.CreateRoleRequest;
import com.example.resortbackendapplication1.auth.model.entity.RoleEntity;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;

public interface RoleService {
    SuccessResponse createRole(CreateRoleRequest request);

    RoleEntity getRoleEntity(String name);
}
