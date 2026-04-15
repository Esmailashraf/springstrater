package org.studyeasy.springstrater.util.constants;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    EDITOR("ROLE_EDITOR");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
