package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.joshua.am.domain.User;
import site.joshua.am.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    /**
     * @return null 로그인 실패 시 null 반환
     */
//    public User login(String loginId, String password) {
//        return userRepository.findByLoginId(loginId)
//                .filter(u -> u.getUserPw().equals(password))
//                .orElse(null);
//    }

}
