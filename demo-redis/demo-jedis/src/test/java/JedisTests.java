import com.demo.redis.DemoJedisApplication;
import com.demo.redis.jedis.service.JedisServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoJedisApplication.class)
public class JedisTests {

    @Resource
    private JedisServiceImpl jedisService;

    @Test
    public void test1(){
        jedisService.jedisSet();
    }

}
