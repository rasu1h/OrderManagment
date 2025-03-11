package source.testmodule.Domain.Model;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import source.testmodule.Domain.Enums.UserRole;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;

    // В данном случае, вместо коллекции GrantedAuthority, используется коллекция UserRole
    private List<? extends GrantedAuthority> authorities = new ArrayList<>();

    public User(Long id,UserRole userRole ,String name, @Email @Unique String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = new ArrayList<>(authorities);
    }

    // Если требуется, можно оставить метод для удобства
    public List<GrantedAuthority> computeAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
