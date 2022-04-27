package com.thangthai.training.backend.service;

import com.thangthai.training.backend.entity.User;
import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.exception.UserException;
import com.thangthai.training.backend.repository.UserRepository;
import com.thangthai.training.backend.util.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email){
        return repository.findByEmail(email);
    }

    public Optional<User> findById(String id){
        return repository.findById(id);
    }

    public Optional<User> findByToken(String token){
        return repository.findByToken(token);
    }

    public User update(User user) {
        return repository.save(user);
    }

    public User updateName(String id, String name) throws BaseException {
        Optional<User> opt = repository.findById(id);
        if ( opt.isEmpty() ) {
            throw UserException.notFound();
        }

        User user = opt.get();
        user.setName(name);

        return repository.save(user);
    }

    public void deleteById(String id){
        repository.deleteById(id);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User create(String email, String password, String name, String token, Date tokenExpireDate) throws BaseException {
        // validate
        if (Objects.isNull(email)) {
            throw UserException.createEmailNull();
        }

        if (Objects.isNull(password)) {
            throw UserException.createPasswordNull();
        }

        if (Objects.isNull(name)) {
            throw UserException.createNameNull();
        }

        // verify
        if (repository.existsByEmail(email)){
            throw UserException.createEmailDuplicated();
        }

        // save
        User entity = new User();
        entity.setEmail(email);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setName(name);
        entity.setToken(token);
        entity.setTokenExpire(tokenExpireDate);//nextMinute(30)

        return repository.save(entity);
    }
}
