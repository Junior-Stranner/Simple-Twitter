package br.com.judev.simpletwitter.dto;

public class RoleDto {
    private long roleId;
    private String name;

    public RoleDto(String name, long roleId) {
        this.name = name;
        this.roleId = roleId;
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
