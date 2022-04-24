package com.thangthai.training.backend.api;

import com.thangthai.training.backend.business.TestBusiness;
import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.model.MRegisterRequest;
import com.thangthai.training.backend.model.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
public class TestApi {
    //Method 1 = Field injection
    //@Autowired
    //private TestBusiness business;

    //Method 2 => constructor injection
    private final TestBusiness business;

    public TestApi(TestBusiness business) {
        this.business = business;
    }


    @GetMapping
    public TestResponse test() {
        TestResponse response = new TestResponse();
        response.setName("Thangthai");
        response.setFood("KFC");
        return response;
    }

    @PostMapping
    @RequestMapping("register")
    public ResponseEntity<String> register(@RequestBody MRegisterRequest request) throws BaseException {
        String response = business.register(request);
        return ResponseEntity.ok(response);
    }
}
