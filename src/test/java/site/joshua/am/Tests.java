package site.joshua.am;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.joshua.am.domain.UserAuth;
import site.joshua.am.dto.MemberListDto;
import site.joshua.am.repository.MemberRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SpringBootTest
public class Tests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void test1() throws Exception {
        //given
        UserAuth auth = UserAuth.ROLE_USER;
        System.out.println(auth);
        //when

        //then

    }

    @Test
    public void testTimeZone() throws Exception {
//        LocalDateTime time = LocalDateTime.now();
//        System.out.println(new Date());
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("** 국제 표준 시간 **");
        System.out.println(LocalDateTime.now());

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("** 한국 시간 **");
        System.out.println(LocalDateTime.now());
    }

    @Test
    public void test2() throws Exception {
//        String s = "P/Bx7HgqR|=6w+?T%_em9<.4Ses6((@F+<tQH9TJr8}&8)4GX9(^)@U)oxv=Y}Dh";
//        int length = s.getBytes().length;
//        System.out.println(length*8);
        List<MemberListDto> members = memberRepository.findMembers();
        for (MemberListDto m : members) {
//            LocalDateTime test = m.getBirthdate().atStartOfDay();
//            System.out.println(test);
        }

    }

}
