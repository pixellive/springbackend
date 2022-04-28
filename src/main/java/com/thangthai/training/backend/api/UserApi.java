package com.thangthai.training.backend.api;

import com.thangthai.training.backend.business.UserBusiness;
import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.model.*;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/profile")
    public ResponseEntity<MUserProfile> getMyUserProfile() throws BaseException {
        MUserProfile response = business.getMyUserProfile();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<MUserProfile> updateMyUserProfile(@RequestBody MUpdateUserProfileRequest request) throws BaseException {
        MUserProfile response = business.updateMyUserProfile(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<MLoginResponse> login(@RequestBody MLoginRequest request) throws BaseException {
        MLoginResponse response = business.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<MRegisterResponse> register(@RequestBody MRegisterRequest request) throws BaseException {
        MRegisterResponse response = business.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activate")
    public ResponseEntity<MActivateResponse> activate(@RequestBody MActivateRequest request) throws BaseException {
        MActivateResponse response = business.activate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-activation-email")
    public ResponseEntity<Void> activate(@RequestBody MResendActivationEmailRequest request) throws BaseException {
        business.resendActivationEmail(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() throws BaseException {
        String reponse = business.refreshToken();
        return ResponseEntity.ok(reponse);
    }


    @PostMapping("/upload-profile")
    public ResponseEntity<String> uploadProfilePicture(@RequestPart MultipartFile file) throws BaseException {
        String response = business.uploadProfilePicture(file);
        return ResponseEntity.ok(response);
    }

    // TODO: to be deleted
    @DeleteMapping("/test-delete-my-account")
    public ResponseEntity<Void> testDeleteMyAccount() throws BaseException {
        business.testDeleteMyAccount();

        return ResponseEntity.ok().build();
    }
}
