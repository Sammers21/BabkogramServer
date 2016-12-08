package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.entity.*;
import service.entity.Dialog;
import service.objects.ErrorResponseObject;
import service.objects.ReturnDialogConferenceId;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.awt.*;

import static service.controllers.SendMessageController.checkToken;
import static service.controllers.SendMessageController.checkUser;


@RestController
@RequestMapping("/{auth_token}/conferences")
public class ConferenceController extends BaseController {
    private static final Logger log = Logger.getLogger(ConferenceController.class.getName());

    @Autowired
    public ConferenceController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    @RequestMapping("/create")
    ResponseEntity<?> createDialog(
            @PathVariable String auth_token
    ) {
        User user = null;
        try {
            user = getUserFromDataBase(auth_token);
        } catch (IllegalArgumentException e) {
            new ResponseEntity<>(new ErrorResponseObject(e.toString()), HttpStatus.FORBIDDEN);
        }
        Dialog generatedDialog = Dialog.generate(dialogRepository, user);
        return new ResponseEntity<>(new ReturnDialogConferenceId(generatedDialog.getDialogName()), HttpStatus.OK);
    }


}
