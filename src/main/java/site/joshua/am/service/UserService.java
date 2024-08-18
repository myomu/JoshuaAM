package site.joshua.am.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.joshua.am.domain.User;
import site.joshua.am.domain.UserAuth;
import site.joshua.am.form.EditUserForm;
import site.joshua.am.form.JoinForm;
import site.joshua.am.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * 회원 등록 (회원 가입)
     * 1. 비밀번호 암호화
     * 2. 회원 등록
     * 3. 권한 등록
     */
    @Transactional
    public Long addUser(JoinForm form) throws Exception {
        // 비밀번호 암호화
        String userPw = form.getUserPw();
        String encodedPw = passwordEncoder.encode(userPw);
        Date regDate = new Date();
        Date updateDate = new Date();
        int enabled = 1; // 계정 활성화
        User user = new User();
        user.createUser(form.getUserLoginId(), encodedPw, form.getUserName(), form.getEmail(), regDate, updateDate, UserAuth.ROLE_USER, enabled);

        // 회원 등록
        userRepository.save(user);

        return user.getId();
    }

    /**
     * 회원 아이디 조회
     */
    public Optional<User> findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    /**
     * 회원 조회
     */
    public User findUser(Long userId) throws Exception {
        return userRepository.findOne(userId);
    }

    /**
     * 로그인
     */
    public void login(User user, HttpServletRequest request) throws Exception {
        String username = user.getUserLoginId();
        String password = user.getUserPw();
        log.info("username : {}", username);
        log.info("password : {}", password);

        // AuthenticationManager
        // 아이디, 패스워드 인증 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // 토큰에 요청정보 등록
        token.setDetails(new WebAuthenticationDetails(request));

        // 토큰을 이용하여 인증 요청 - 로그인
        Authentication authentication = authenticationManager.authenticate(token);
        log.info("인증 여부 : {}", authentication.isAuthenticated());

        org.springframework.security.core.userdetails.User authUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        log.info("인증된 사용자 아이디 : {}", authUser.getUsername());

        // 시큐리티 컨텍스트에 인증 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public int editUser(EditUserForm user) throws Exception {
        Long userId = user.getId();
        User findUser = userRepository.findOne(userId);

        // 비밀번호 암호화와 변경감지를 이용하여 찾아온 User 에 비밀번호, 이름, 이메일 세팅
        String userPw = user.getUserPw();
        String encodedPw = passwordEncoder.encode(userPw);
        findUser.setUpdDate(new Date()); // 변경한 날짜 등록
        findUser.editPasswordAndNameAndEmail(encodedPw, user.getUserName(), user.getEmail());

        return 1;
    }

    /**
     * 회원 삭제 (회원 탈퇴)
     */
    @Transactional
    public int deleteUser(Long userId) throws Exception {
        User findUser = userRepository.findOne(userId);
        findUser.setDisabled(false);
        return 1;
    }
}
