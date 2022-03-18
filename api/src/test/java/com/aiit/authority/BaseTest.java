package com.aiit.authority;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = AuthorityApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BaseTest {

    @Test
    public void test() {

    }
}
