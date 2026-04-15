package org.studyeasy.springstrater.Config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class AppConfig {
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private String mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean mailSmtpAuth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean mailSmtpStarttlsEnable;
    @Value("${spring.mail.smtp.ssl.trust}")
    private String mailSmtpSslTrust;
    @Value("${mail.transport.protocol}")
    private String mailTransportProtocol;

    
    @Bean
    public JavaMailSender gtJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(Integer.parseInt(mailPort));
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        props.put("mail.smtp.ssl.trust", mailSmtpSslTrust);
        props.put("mail.transport.protocol", mailTransportProtocol);
        return mailSender;



    }

}