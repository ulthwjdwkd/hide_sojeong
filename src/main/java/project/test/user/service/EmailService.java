package project.test.user.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public MimeMessage createMessage(String to, String authNum) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 수신자 설정
        message.setSubject("[Hide] 회원가입 인증번호 입니다."); // 이메일 제목
        String textContent = "인증 코드: " + authNum;

        message.setText(textContent, "utf-8"); // 평문 설정
        message.setFrom(new InternetAddress(fromEmail, "groom_hide")); // 발신자 설정

        return message;
    }

    public String createCode(){
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));

    }

    public String sendEmailAuthentication(String sendEmail) throws Exception{

        String authNum = createCode();	//랜덤 인증번호 생성

        MimeMessage message = createMessage(sendEmail, authNum); //메일 발송
        try{
            javaMailSender.send(message);
            return authNum;
        }catch (MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
