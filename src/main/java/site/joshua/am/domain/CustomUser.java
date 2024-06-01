package site.joshua.am.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class CustomUser implements UserDetails {

    private User user;

    public CustomUser() {}

    public CustomUser(User user) {
        this.user = user;
    }

    /**
     * 권한 getter 메서드
     * List<UserAuth> --> Collection<SimpleGrantedAuthority>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<UserAuth> authList = user.getAuthList();
        // SimpleGrantedAuthority() - "ROLE_USER"
//        Collection<SimpleGrantedAuthority> roleList = authList.stream()
//                .map((auth) -> new SimpleGrantedAuthority(auth.getAuth()))
//                .collect(Collectors.toList());
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getAuth().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getUserPw();
    }

    @Override
    public String getUsername() {
        return user.getUserLoginId();
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
        return user.getEnabled() != 0;
    }
}
