package com.thangthai.training.backend.exception;

public class EmailException extends BaseException{
    public EmailException(String code) {
        super("email."+code);
    }

    public static EmailException tamplateNotFound(){
        return new EmailException("template.not.found");
    }
}
