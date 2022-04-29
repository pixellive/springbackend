package com.thangthai.training.backend.business;

import com.thangthai.training.backend.entity.User;
import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.exception.FileException;
import com.thangthai.training.backend.exception.UserException;
import com.thangthai.training.backend.mapper.UserMapper;
import com.thangthai.training.backend.model.*;
import com.thangthai.training.backend.service.TokenService;
import com.thangthai.training.backend.service.UserService;
import com.thangthai.training.backend.util.SecurityUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Log4j2
public class UserBusiness {

    private final UserService userService;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    private final EmailBusiness emailBusiness;

    public UserBusiness(UserService userService, TokenService tokenService, UserMapper userMapper, EmailBusiness emailBusiness) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.emailBusiness = emailBusiness;
    }

    public MUserProfile getMyUserProfile() throws BaseException {
        Optional<String> opt = SecurityUtil.getCurrentUserId();
        if(opt.isEmpty()){
            throw UserException.unauthorized();
        }

        String userId = opt.get();
        Optional<User> optUser = userService.findById(userId);
        if (optUser.isEmpty()){
            throw UserException.notFound();
        }

        return userMapper.toUserProfile(optUser.get());
    }

    public MUserProfile updateMyUserProfile(MUpdateUserProfileRequest request) throws BaseException {
        Optional<String> opt = SecurityUtil.getCurrentUserId();
        if(opt.isEmpty()){
            throw UserException.unauthorized();
        }

        String userId = opt.get();

        // validate
        if(ObjectUtils.isEmpty(request.getName())){
            throw UserException.updateNameNull();
        }

        User user = userService.updateName(userId, request.getName());
        return userMapper.toUserProfile(user);
    }

    public MLoginResponse login(MLoginRequest request) throws BaseException {

        // validate request

        // verify database
        Optional<User> opt = userService.findByEmail(request.getEmail());
        if (opt.isEmpty()) {
            throw UserException.loginFailEmailNotFound();
        }

        User user = opt.get();

        //verify password
        if ( !userService.matchPassword(request.getPassword(), user.getPassword())) {
            throw UserException.loginFailPasswordIncorrect();
        }

        // validate activate status
        if (!user.isActivated()){
            throw UserException.loginFailUserUnactivated();
        }

        MLoginResponse response = new MLoginResponse();
        response.setToken(tokenService.tokenize(user));
        return response;
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

        String token = SecurityUtil.generateToken();
        User user = userService.create(request.getEmail(), request.getPassword(), request.getName(), token, nextMinute(30));

        sendEmail(user);

        return userMapper.toRegisterResponse(user);
    }

    public MActivateResponse activate(MActivateRequest request) throws BaseException{
        String token = request.getToken();
        if(StringUtil.isNullOrEmpty(token)){
            throw UserException.activateNoToken();
        }

        Optional<User> opt = userService.findByToken(token);
        if (opt.isEmpty()) {
            throw UserException.activateFail();
        }

        User user = opt.get();
        if (user.isActivated()){
            throw UserException.activateAlready();
        }

        Date now = new Date();
        Date expireDate = user.getTokenExpire();
        if ( now.after(expireDate)) {
            // TODO:resend Email

            // TODO:remove user
            throw UserException.activateTokenExpire();
        }

        /**
         * Update activated user
         */
        user.setActivated(true);
        userService.update(user);

        MActivateResponse response = new MActivateResponse();
        response.setSuccess(true);
        return  response;
    }

    public void resendActivationEmail(MResendActivationEmailRequest request) throws BaseException{
        String token = request.getToken();
        if (StringUtil.isNullOrEmpty(token)) {
            throw UserException.resendActivationEmailNoToken();
        }

        Optional<User> opt = userService.findByToken(token);
        if (opt.isEmpty()) {
            throw UserException.resendActivationTokenNotFound();
        }

        User user = opt.get();
        if(user.isActivated()){
            throw UserException.activateAlready();
        }

        user.setToken(SecurityUtil.generateToken());
        user.setTokenExpire(nextMinute(30));
        user = userService.update(user);

        sendEmail(user);
    }


    private Date nextMinute(int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private void sendEmail(User user){

        // TODO: generate token
        String token = user.getToken();

        try {
            emailBusiness.sendActivateUserEmail(user.getEmail(), user.getName(), token);
        } catch (BaseException e) {
            e.printStackTrace();
        }
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

    // TODO: tobe deleted
    public void testDeleteMyAccount()throws BaseException{
        Optional<String> opt = SecurityUtil.getCurrentUserId();
        if(opt.isEmpty()){
            throw UserException.unauthorized();
        }

        String userId = opt.get();
        userService.deleteById(userId);
    }
}
