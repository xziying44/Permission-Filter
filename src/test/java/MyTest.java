import org.junit.Test;

/**
 * MyTest
 *
 * @author : xziying
 * @create : 2020-11-14 23:35
 */
public class MyTest {
    @Test
    public void test1(){
        String link = "manage/manager.html";
        String re = "manage2/.+";
        System.out.println(link.matches(re));
    }
}
