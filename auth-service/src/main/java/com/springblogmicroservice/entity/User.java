package com.springblogmicroservice.entity;

import com.springblogmicroservice.validation.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
@Table(name = "users")
public class User implements UserDetails {

    public enum UserStatus{
        ACTIVE,CLOSED,CANCELED,BLACKLISTED,NONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 1,max = 135)
    @Pattern(regexp = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$")
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @ValidPassword
    private String password;

    @Length(min=10,max=10)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "UserStatus")
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER,cascade ={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;


    @OneToOne
    @JoinColumn(name = "refresh_id", referencedColumnName = "id")
    private RefreshToken refreshToken;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        for(Role r : getRoles()){
            list.add(new SimpleGrantedAuthority(r.getName()));
        }
        return list;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
