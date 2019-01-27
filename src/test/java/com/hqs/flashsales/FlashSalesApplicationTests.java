package com.hqs.flashsales;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlashsalesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlashSalesApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void flashsaleTest() {
        String url = "http://localhost:8080/placeOrder";
        for(int i = 0; i < 3000; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(20);
                new Thread(() -> {
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    params.add("orderId", "1");
                    Long result = testRestTemplate.postForObject(url, params, Long.class);
                    if(result != 0) {
                        System.out.println("-------------" + result);
                    }
                }
                ).start();
            } catch (Exception e) {
                log.info("error:{}", e.getMessage());
            }

        }
    }

    @Test
    public void contextLoads() {
    }

}

