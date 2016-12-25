package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.entity.Dialog;
import service.entity.User;
import service.objects.Dialog_IdResponse;
import service.objects.ErrorResponseObject;
import service.objects.JSONInputNewName;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/{auth_token}/name")
public class SetNameController extends BaseController {
    private final Logger log = Logger.getLogger(SetNameController.class.getName());

    @Autowired
    public SetNameController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    @RequestMapping(value = "/{dialog_id}", method = RequestMethod.GET)
    ResponseEntity<?> getNameOfUserOrDialog(
            @PathVariable String auth_token,
            @PathVariable String dialog_id
    ) {
        String name = "";
        //if dialog
        if (dialog_id.charAt(0) == '+') {
            Dialog dialog = dialogRepository.findByDialogId(dialog_id);
            if (dialog == null) {
                log.error("no such dialog_id");
                return new ResponseEntity<>(new ErrorResponseObject("no such dialog_id"), HttpStatus.FORBIDDEN);
            }
            name = dialog.getDialogName();
        }
        // if user
        else {
            User user = userRepository.findByUsername(dialog_id);
            if (user == null) {
                log.error("no such user");
                return new ResponseEntity<>(new ErrorResponseObject("no such user"), HttpStatus.FORBIDDEN);
            }
            name = user.getDisplayName();
        }
        return new ResponseEntity<>(new Dialog_IdResponse(name), HttpStatus.OK);
    }

    @RequestMapping(value = "/{dialog_id}", method = RequestMethod.POST)
    ResponseEntity<?> setNameOfDialog(
            @PathVariable String dialog_id,
            @PathVariable String auth_token,
            @RequestBody JSONInputNewName newName
    ) {
        //if dialog
        if (dialog_id.charAt(0) == '+') {
            Dialog dialog = dialogRepository.findByDialogId(dialog_id);
            if (dialog == null) {
                log.error("no such dialog_id");
                return new ResponseEntity<>(new ErrorResponseObject("no such dialog_id"), HttpStatus.FORBIDDEN);
            }
            dialog.setDialogName(newName.getNew_name());
            dialogRepository.save(dialog);
        }
        else {
            log.error("dialog_id must start with +");
            return new ResponseEntity<>(new ErrorResponseObject("dialog_id must start with +"), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping( method = RequestMethod.POST)
    ResponseEntity<?> setNameOfUser(
            @PathVariable String auth_token,
            @RequestBody JSONInputNewName newName
    ) {
        // if user
            try {
                User user = getUserFromDataBase(auth_token);
                user.setDisplayName(newName.getNew_name());
                userRepository.save(user);
            } catch (FileNotFoundException e) {
                log.error(e.toString());
                return new ResponseEntity<>(new ErrorResponseObject("no such user"), HttpStatus.FORBIDDEN);
            }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
