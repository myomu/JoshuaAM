package site.joshua.am.security.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.joshua.am.domain.CustomUser;
import site.joshua.am.domain.User;
import site.joshua.am.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

//    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("login - loadUserByUsername : {}", username);
        /*User user = userRepository.findByLoginId(username).orElseGet(User::new);

        if (user.getId() == null) {
            log.info("사용자 없음... (일치하는 아이디가 없음)");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
        }*/

        // Users -> CustomUser
        CustomUser customUser = new CustomUser();

        try {
            User user = userRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username));
            // User user = findByLoginId.orElseGet(User::new);
            log.info("user : {}", user);
            customUser.setUser(user);
        } catch (UsernameNotFoundException e) {
            log.info("사용자 없음... (일치하는 아이디가 없음)");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
        }

        log.info("customUser: {}", customUser);
        return customUser;
    }
}
