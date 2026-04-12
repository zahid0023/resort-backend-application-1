package com.example.resortbackendapplication1.auth.service;

import com.example.resortbackendapplication1.auth.dto.request.role.CreateRoleRequest;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.auth.model.enitty.RoleEntity;

public interface RoleService {
    SuccessResponse createRole(CreateRoleRequest request);

    RoleEntity getRoleEntity(String name);
}
