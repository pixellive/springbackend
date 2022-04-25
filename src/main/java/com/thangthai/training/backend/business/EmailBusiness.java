package com.thangthai.training.backend.business;

import com.thangthai.training.backend.exception.BaseException;
import com.thangthai.training.backend.exception.EmailException;
import com.thangthai.training.backend.service.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;

@Service
public class EmailBusiness {

    private final EmailService emailService;

    public EmailBusiness(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendActivateUserEmail(String email, String name, String token) throws BaseException {
        // prepare content HTML
        String html = null;
        try {
            html = readEmailTemplate("email-activate-user.html");
        } catch (IOException e) {
            throw EmailException.tamplateNotFound();
        }

        String finalLink = "http://localhost:4200/activate/" + token;
        html = html.replace("${P_NAME}", name);
        html = html.replace("${LINK}", finalLink);

        // prepare subject
        String subject = "Please activate your account";

        emailService.send(email, subject, html);
    }

    private String readEmailTemplate(String fileName) throws IOException {
        File file = ResourceUtils.getFile("classpath:email/" + fileName);
        return FileCopyUtils.copyToString(new FileReader(file));
    }
}
