package project.test.user.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.test.user.repository.UserRepository;
import project.test.user.dto.AuthDto;
import project.test.user.entity.User;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // DB에서 조회
        User userData = userRepository.findByUserId(loginId);

        if (userData != null) {

            AuthDto authDto = AuthDto.builder()
                    .loginId(userData.getUserId())
                    .password(userData.getUserPassword())
                    .role(userData.getUserRole())
                    .build();

            // UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(authDto);
        }

        return null;
    }
}
