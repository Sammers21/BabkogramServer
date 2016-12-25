package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.*;
import static service.controllers.SendMessageController.checkToken;


@RestController
@RequestMapping("/{auth_token}/conferences")
public class ConferenceController extends BaseController {
    private static final Logger log = Logger.getLogger(ConferenceController.class.getName());

    @Autowired
    public ConferenceController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    /**
     * create dialog metod
     */
    @RequestMapping("/create")
    ResponseEntity<?> createDialog(
            @PathVariable String auth_token
    ) {
        User user = null;
        try {
            user = getUserFromDataBase(auth_token);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponseObject(e.toString()), HttpStatus.FORBIDDEN);
        }
        Dialog generatedDialog = Dialog.generate(dialogRepository, user);

        user.addDialog(generatedDialog.getDialogId());
        userRepository.save(user);

        systemDialogAssertion("created", generatedDialog);
        return new ResponseEntity<>(new ReturnDialogConferenceId(generatedDialog.getDialogName()), HttpStatus.OK);
    }

    /**
     * invite user to conference
     */
    @RequestMapping("/{conference_id}/invite/{user_id}")
    ResponseEntity<?> inviteUser(
            @PathVariable String auth_token,
            @PathVariable String conference_id,
            @PathVariable String user_id
    ) {
        try {
            Dialog dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
            User inviter = getUserFromDataBase(auth_token);
            User toInviteUser = userRepository.findByUsername(user_id);

            if (toInviteUser == null) {
                return new ResponseEntity<>(new ErrorResponseObject("user is not exist"), HttpStatus.FORBIDDEN);
            }

            dialogFromDataBase.addUser(user_id);
            systemDialogAssertion(
                    "invited|" + toInviteUser.getUsername(),
                    dialogFromDataBase);
            dialogRepository.save(dialogFromDataBase);

            toInviteUser.addDialog(dialogFromDataBase.getDialogId());
            userRepository.save(toInviteUser);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponseObject(e.getMessage()), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/{conference_id}/kick/{user_id}")
    ResponseEntity<?> kickUser(
            @PathVariable String auth_token,
            @PathVariable String conference_id,
            @PathVariable String user_id
    ) {
        Dialog dialogFromDataBase = null;
        User userFromDataBase = null;
        try {
            dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
            userFromDataBase = getUserFromDataBase(auth_token);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponseObject(e.toString()), HttpStatus.FORBIDDEN);
        }
        String owner = userFromDataBase.getUsername();

        User userToKick = userRepository.findByUsername(user_id);

        if (userToKick == null) {
            log.error("No such user to kick");
            return new ResponseEntity<>(new ErrorResponseObject("No such user to kick"), HttpStatus.FORBIDDEN);
        }
        if (dialogFromDataBase.getOwner().equals(owner)) {
            systemDialogAssertion("kicked|" + userFromDataBase.getUsername(), dialogFromDataBase);
            dialogFromDataBase.deleteUser(user_id);
            dialogRepository.save(dialogFromDataBase);

            userToKick.deleteDialog(dialogFromDataBase.getDialogId());
            userRepository.save(userToKick);

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
            @PathVariable String conference_id
    ) {
        Dialog dialogFromDataBase = null;
        User userFromDataBase = null;
        try {
            dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
            userFromDataBase = getUserFromDataBase(auth_token);
        } catch (FileNotFoundException e) {
            log.error(e.toString());
            return new ResponseEntity<>(new ErrorResponseObject(e.toString()), HttpStatus.FORBIDDEN);
        }

        String username = userFromDataBase.getUsername();

        if (dialogFromDataBase.getOwner().equals(username)) {
            log.error("user can't leave from own conference");
            return new ResponseEntity<>(new ErrorResponseObject("user can't leave from own conference"), HttpStatus.FORBIDDEN);
        } else {
            systemDialogAssertion("left|" + userFromDataBase.getUsername(), dialogFromDataBase);
            dialogFromDataBase.deleteUser(username);
            dialogRepository.save(dialogFromDataBase);

            userFromDataBase.deleteDialog(dialogFromDataBase.getDialogId());
            userRepository.save(userFromDataBase);

            log.info("user " + userFromDataBase.getUsername() + " leave from " + dialogFromDataBase.getDialogId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @RequestMapping("/{conference_id}/user_list")
    ResponseEntity<?> user_list(
            @PathVariable String auth_token,
            @PathVariable String conference_id
    ) {
        Dialog dialogFromDataBase;
        try {
            dialogFromDataBase = getDialogFromDataBase(auth_token, conference_id);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponseObject(e.toString()), HttpStatus.FORBIDDEN);
        }
        ConferenceUserResponse conferenceUserResponse = new ConferenceUserResponse(dialogFromDataBase.getOwner());
        ArrayList<UserInConferenceList> users = conferenceUserResponse.getUsers();
        List<UserInConferenceList> u = dialogFromDataBase.getUserNameList().stream()
                .filter(s -> !s.equals(dialogFromDataBase.getOwner()))
                .map((String s) ->
                        {
                            return new UserInConferenceList(s, findJoinTime(s, dialogFromDataBase));
                        }
                ).collect(toList());
        users.addAll(u);
        return new ResponseEntity<>(conferenceUserResponse, HttpStatus.OK);
    }


}
