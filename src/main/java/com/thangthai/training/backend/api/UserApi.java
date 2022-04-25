package com.thangthai.training.backend.api;

import com.thangthai.training.backend.business.UserBusiness;
import com.thangthai.training.backend.entity.User;
import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.model.MLoginRequest;
import com.thangthai.training.backend.model.MRegisterRequest;
import com.thangthai.training.backend.model.MRegisterResponse;
import com.thangthai.training.backend.model.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserApi {
    //Method 1 = Field injection
    //@Autowired
    //private TestBusiness business;

    //Method 2 => constructor injection
    private final UserBusiness business;

    public UserApi(UserBusiness business) {
        this.business = business;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MLoginRequest request) throws BaseException {
        String response = business.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<MRegisterResponse> register(@RequestBody MRegisterRequest request) throws BaseException {
        MRegisterResponse response = business.register(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/upload-profile")
    public ResponseEntity<String> uploadProfilePicture(@RequestPart MultipartFile file) throws BaseException {
        String response = business.uploadProfilePicture(file);
        return ResponseEntity.ok(response);
    }
}
