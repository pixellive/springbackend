package com.thangthai.training.backend.business;

import com.thangthai.training.backend.config.SecurityConfig;
import com.thangthai.training.backend.entity.User;
import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.exception.FileException;
import com.thangthai.training.backend.exception.UserException;
import com.thangthai.training.backend.mapper.UserMapper;
import com.thangthai.training.backend.model.MLoginRequest;
import com.thangthai.training.backend.model.MRegisterRequest;
import com.thangthai.training.backend.model.MRegisterResponse;
import com.thangthai.training.backend.service.TokenService;
import com.thangthai.training.backend.service.UserService;
import com.thangthai.training.backend.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserBusiness {

    private final UserService userService;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    public UserBusiness(UserService userService, TokenService tokenService, UserMapper userMapper) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
    }

    public String login(MLoginRequest request) throws BaseException {

        // validate request

        // verify database
        Optional<User> opt = userService.findByEmail(request.getEmail());
        if (opt.isEmpty()) {
            throw UserException.loginFailEmailNotFound();
        }

        User user = opt.get();
        if ( !userService.matchPassword(request.getPassword(), user.getPassword())) {
            throw UserException.loginFailPasswordIncorrect();
        }

        return tokenService.tokenize(user);
    }

    public String refreshToken() throws BaseException {

        Optional<String> opt = SecurityUtil.getCurrentUserId();
        if(opt.isEmpty()){
            throw UserException.unauthorized();
        }

        String userId = opt.get();
        Optional<User> optUser = userService.findById(userId);
        if (optUser.isEmpty()){
            throw UserException.notFound();
        }

        User user = optUser.get();
        return tokenService.tokenize(user);
    }

    public MRegisterResponse register(MRegisterRequest request) throws BaseException {

        User user = userService.create(request.getEmail(), request.getPassword(), request.getName());

        // TODO: mapper
        return userMapper.toRegisterResponse(user);
    }

    public String uploadProfilePicture(MultipartFile file) throws BaseException{
        // validate file
        if (file == null) {
            throw FileException.fileNull();
        }

        // validate size
        if (file.getSize() > 1048576 * 2) {
            throw FileException.fileMaxSize();
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw FileException.Unsupported();
        }

        List<String> supportTypes = Arrays.asList("image/jpeg", "image/png");
        if (supportTypes.contains(contentType)) {
            throw FileException.Unsupported();
        }

        // TODO: upload file into File Storage (AWS s3, etc...)
        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
