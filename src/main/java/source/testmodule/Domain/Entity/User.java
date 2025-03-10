package source.testmodule.Domain.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import source.testmodule.Domain.Enums.UserRole;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "user_id")
    private Long id;
    private String name;
    @Email
    @Unique
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String mail, String password) {
        this.email = mail;
        this.password = password;
    }

    public User(String mail, String encodedPassword, List<GrantedAuthority> roleUser) {
        this.email = mail;
        this.password = encodedPassword;
        this.role = UserRole.ROLE_USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
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
    public boolean isEnabled() { return true;}

}
