package ufpb.br.apilocadora.controller;

import org.springframework.web.bind.annotation.RestController;
import ufpb.br.apilocadora.resource.EmailMessage;
import ufpb.br.apilocadora.service.EmailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class EmailController {

    private final EmailSenderService emailSenderService;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage) {
        this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }

}
