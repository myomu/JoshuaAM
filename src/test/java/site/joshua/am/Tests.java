package site.joshua.am;

import org.junit.jupiter.api.Test;
import site.joshua.am.domain.UserAuth;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

public class Tests {

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
        String s = "-Q!G.&/g]T5Ud_wGs<@>;FG&q$$r4f~+|UOVWrJVFH/2DyL6TKAvk8)[8Do*uw|u";
        int length = s.getBytes().length;
        System.out.println(length*8);
    }

}
