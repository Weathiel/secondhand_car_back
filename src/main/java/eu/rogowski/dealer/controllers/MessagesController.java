package eu.rogowski.dealer.controllers;

import eu.rogowski.dealer.dto.MessageDTO;
import eu.rogowski.dealer.services.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.internet.AddressException;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessagesController {
    @Autowired
    private final MessagesService messagesService;


    @PostMapping("/sendVerify")
    public ResponseEntity sendUserVerifyEmail(@RequestBody MessageDTO messageDTO) {
        try {
            return messagesService.sendVerifyUserMail(messageDTO.getFrom());
        } catch (AddressException e) {
            return ResponseEntity.badRequest().body(messageDTO);
        }
    }

    @GetMapping(name = "/verify", params = {"token", "username"})
    public ModelAndView verifyEmail(@RequestParam String username, @RequestParam String token, RedirectAttributes attributes) {
        messagesService.verifyEmailToken(username, token).getStatusCode();
        return new ModelAndView("redirect:" + "http://localhost:4200/");
    }
}
