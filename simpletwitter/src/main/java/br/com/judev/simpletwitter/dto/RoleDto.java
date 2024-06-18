package br.com.judev.simpletwitter.dto;

import br.com.judev.simpletwitter.entities.Role;

public class RoleDto {
    private long roleId;
    private String name;

    public RoleDto(Role entity) {
     roleId = entity.getRoleId();
     name = entity.getName();
    }


    public RoleDto() {
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
