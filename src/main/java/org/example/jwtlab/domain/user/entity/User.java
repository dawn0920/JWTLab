package org.example.jwtlab.domain.user.entity;

import lombok.*;
import org.example.jwtlab.domain.user.enums.UserRole;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    public static User builder;
    private Long id;
    private String email;
    private String password;
    private String name;
    private UserRole userRole;

    public User(String email, String password, String name, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
