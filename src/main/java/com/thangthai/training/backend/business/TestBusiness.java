package com.thangthai.training.backend.business;

import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.exception.UserException;
import com.thangthai.training.backend.model.MRegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TestBusiness {
    public String register(MRegisterRequest request) throws BaseException {
        if (request == null) {
            throw UserException.requestNull();
        }

        if (Objects.isNull(request.getEmail())) {
            throw UserException.emailNull();
        }

        // validate
        return "";
    }
}
