package site.joshua.am.apicontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.joshua.am.domain.CustomUser;
import site.joshua.am.domain.JoinAuthKey;
import site.joshua.am.domain.User;
import site.joshua.am.form.EditUserForm;
import site.joshua.am.form.JoinForm;
import site.joshua.am.repository.JoinAuthKeyRepository;
import site.joshua.am.service.UserService;

import java.util.List;
import java.util.Optional;

/**
 *  [GET]       /users/info     - 회원정보 조회   (ROLE_USER)
 *  [POST]      /users          - 회원가입        ALL
 *  [PUT]       /users          - 회원정보 수정   (ROLE_USER)
 *  [DELETE]    /users          - 회원탈퇴       (ROLE_ADMIN)
 *  */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final JoinAuthKeyRepository joinAuthKeyRepository;

    /**
     * 사용자 정보 조회
     * @param customUser
     * @return
     */
    @Secured("ROLE_USER")           // USER 권한 설정
    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal CustomUser customUser) {

        log.info("::::: customUser :::::");
        log.info("customUser : {}", customUser);

        User user = customUser.getUser();
        log.info("user : {}", user);

        // 인증된 사용자 정보
        if( user != null )
            return new ResponseEntity<>(user, HttpStatus.OK);

        // 인증 되지 않음
        log.info("인증되지 않음!!");
        return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }

    /**
     * 회원가입
     * @param form
     * @return
     * @throws Exception
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinForm form) throws Exception {
        log.info("[POST] - /users, user : {}", form.toString());

        Optional<User> userLoginId = userService.findUserByLoginId(form.getUserLoginId());
        if (userLoginId.isPresent()) {
            return new ResponseEntity<>("중복된 아이디 입니다.", HttpStatus.BAD_REQUEST);
        }

        List<JoinAuthKey> keys = joinAuthKeyRepository.findKeys();

        boolean checkAuthKey = false;
        for (JoinAuthKey key : keys) {
            if (form.getAuthKey().equals(key.getAuthKey())) {
                checkAuthKey = true;
                break;
            }
        }

        if (!checkAuthKey) {
            return new ResponseEntity<>("인증키가 틀렸습니다.", HttpStatus.BAD_REQUEST);
        }

        // 회원가입 진행
        Long result = userService.addUser(form);

        if( result > 0 ) {
            log.info("회원가입 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        else {
            log.info("회원가입 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 회원 정보 수정
     * @param form
     * @return
     * @throws Exception
     */
    @Secured("ROLE_USER")           // USER 권한 설정
    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody EditUserForm form) throws Exception {
        log.info("[PUT] - /users");
        int result = userService.editUser(form);

        if( result > 0 ) {
            log.info("회원수정 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        else {
            log.info("회원수정 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 회원 탈퇴
     * @param userId
     * @return
     * @throws Exception
     */
    @Secured("ROLE_USER")          //  ADMIN 권한 설정
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> destroy(@PathVariable("userId") Long userId) throws Exception {
        log.info("[DELETE] - /users/{userId}");

        int result = userService.deleteUser(userId);

        if( result > 0 ) {
            log.info("회원삭제 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        else {
            log.info("회원삭제 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

    }


}
