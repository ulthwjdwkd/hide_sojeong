package project.test.user.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.test.user.dto.AuthDto;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AuthDto authDto;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } // authDto에 설정하지 않은 값은 true로 변환

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // authDto에 설정하지 않은 값은 true로 변환

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // authDto에 설정하지 않은 값은 true로 변환

    @Override
    public boolean isEnabled() {
        return true;
    } // authDto에 설정하지 않은 값은 true로 변환

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return authDto.getRole();
            }

        });

        return collection;
    }

    @Override
    public String getPassword() {
        return authDto.getPassword();
    }

    @Override
    public String getUsername() { return authDto.getLoginId(); }
}
