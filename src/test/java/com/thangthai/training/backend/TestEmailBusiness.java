package com.thangthai.training.backend;

import com.thangthai.training.backend.business.EmailBusiness;
import com.thangthai.training.backend.exception.BaseException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmailBusiness {

    @Autowired
    private EmailBusiness emailBusiness;

    @Order(1)
    @Test
    void testSendActivateEmail() throws BaseException {
        emailBusiness.sendActivateUserEmail(
                TestData.email,
                TestData.name,
                TestData.token);
    }

    interface TestData {
        String email = "a.phosong@gmail.com";

        String name = "Anucha Phosong";

        String token = "m#@:LSDIFDISIDF099020kkddd";
    }
}
