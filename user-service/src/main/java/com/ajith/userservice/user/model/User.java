package com.ajith.userservice.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean isEmailVerified = false;
    private String emailVerificationToken;
    private boolean isBlocked = false;
    private Date joinDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Override
    public Collection < ? extends GrantedAuthority > getAuthorities ( ) {
        return List.of ( new SimpleGrantedAuthority ( role.name ( ) ) );
    }

    @Override
    public String getUsername ( ) {
        return email;
    }

    @Override
    public boolean isAccountNonExpired ( ) {
        return true;
    }

    @Override
    public boolean isAccountNonLocked ( ) {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired ( ) {
        return true;
    }

    @Override
    public boolean isEnabled ( ) {
        return true;
    }
}
