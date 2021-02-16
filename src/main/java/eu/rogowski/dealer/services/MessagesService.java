package eu.rogowski.dealer.services;

import eu.rogowski.dealer.configuration.jwt.JwtUtils;
import eu.rogowski.dealer.models.User;
import eu.rogowski.dealer.payload.ResponseJSON;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import javax.mail.internet.AddressException;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessagesService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    public ResponseEntity sendVerifyUserMail(String mailTo) throws AddressException {
        User user = userService.updateVerificationToken(mailTo);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("no-reply@mail.project");
        simpleMailMessage.setTo(mailTo);
        simpleMailMessage.setSubject("no-reply");
        simpleMailMessage.setText("To verify your account click on the link below: \n" + "http://localhost:8443/message?username=" + user.getUsername() + "&token=activate");//user.getActivationToken());

        javaMailSender.send(simpleMailMessage);
        return ResponseEntity.ok(new ResponseJSON("Successfully sended mail", 200));
    }

    public ResponseEntity verifyEmailToken(String username, String token){
        return userService.enableUser(username, token);
    }


}

