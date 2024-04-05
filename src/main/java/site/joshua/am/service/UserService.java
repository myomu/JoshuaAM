package site.joshua.am.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.Member;
import site.joshua.am.domain.User;
import site.joshua.am.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long addUser(User user) {
        userRepository.save(user);
        return user.getId();
    }
}
