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
import service.objects.ConferenceUserResponse;
import service.objects.ErrorResponseObject;
import service.objects.ReturnDialogConferenceId;
import service.objects.UserInConferenceList;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
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

    @RequestMapping("/{conference_id}/invite/{user_id}")
    ResponseEntity<?> inviteUser(
            @PathVariable String auth_token,
            @PathVariable String conference_id,
            @PathVariable String user_id
    ) {
        Dialog dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
        dialogFromDataBase.addUser(user_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/{conference_id}/kick/{user_id}")
    ResponseEntity<?> kickUser(
            @PathVariable String auth_token,
            @PathVariable String conference_id,
            @PathVariable String user_id
    ) {
        Dialog dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
        String owner = getUserFromDataBase(auth_token).getUsername();
        if (dialogFromDataBase.getOwner().equals(owner)) {
            dialogFromDataBase.deleteUser(user_id);
            log.info("succes kick from conference");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Only owner ay have opportynity to kick");
            return new ResponseEntity<>(new ErrorResponseObject("Only owner ay have opportynity to kick"), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping("/{conference_id}/leave")
    ResponseEntity<?> leave(
            @PathVariable String auth_token,
            @PathVariable String conference_id,
            @PathVariable String user_id
    ) {
        Dialog dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
        String owner = getUserFromDataBase(auth_token).getUsername();
        if (dialogFromDataBase.getOwner().equals(owner)) {
            log.error("user can't leave from own conference");
            return new ResponseEntity<>(new ErrorResponseObject("user can't leave from own conference"), HttpStatus.FORBIDDEN);
        } else {
            dialogFromDataBase.deleteUser(user_id);
            log.info("user " + user_id + " leave from " + dialogFromDataBase.getDialogId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping("/{conference_id}/user_list")
    ResponseEntity<?> user_list(
            @PathVariable String auth_token,
            @PathVariable String conference_id
    ) {
        Dialog dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
        ConferenceUserResponse conferenceUserResponse = new ConferenceUserResponse();
        ArrayList<UserInConferenceList> users = conferenceUserResponse.getUsers();

        List<UserInConferenceList> u = dialogFromDataBase.getUserNameList().stream()
                .map((String s) ->
                        {
                            return new UserInConferenceList(s, 0);
                        }
                ).collect(toList());
        users.addAll(u);
        return new ResponseEntity<>(conferenceUserResponse, HttpStatus.OK);
    }


}
