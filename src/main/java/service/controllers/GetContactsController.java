package service.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.ContactsResponse;
import service.objects.Dialogs;
import service.objects.ErrorResponseObject;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static service.controllers.SendMessageController.checkToken;
import static service.controllers.SendMessageController.checkUser;

@RestController
@RequestMapping("/{auth_token}/contacts")
public class GetContactsController {
    private static final Logger log = Logger.getLogger(GetContactsController.class.getName());

    private static final long DEFAULT_COUNT_OF_CONTACTS = 25;


    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private MessageRepository messageRepository;

    @Autowired
    public GetContactsController(UserRepository userRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.messageRepository = messageRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<?> getDefaultCountOfContacts(
            @PathVariable String auth_token
    ) {
        log.info("mapping:/" + auth_token + "/contacts");
        log.info("defaultCount of contacts");
        return getResponseEntityWithOffSet(auth_token, 0);
    }

    @RequestMapping(value = "/offset/{offset}", method = RequestMethod.GET)
    ResponseEntity<?> getCustomCountOfContacts(
            @PathVariable String auth_token,
            @PathVariable int offset
    ) {
        log.info("mapping:/" + auth_token + "/contacts" + "/offset/" + offset);

        return getResponseEntityWithOffSet(auth_token, offset);
    }

    private ResponseEntity<?> getResponseEntityWithOffSet(
            String auth_token, int offset) {
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token)) {
            log.info("invalid token");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, user)) {
            log.info("token without username");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(getContactsWithOffset(user.getUsername(), offset), HttpStatus.OK);
    }

    private ContactsResponse getContactsWithOffset(String username, int offset) {
        ContactsResponse contactsResponse = new ContactsResponse();

        ArrayList<Dialogs> dialogs = contactsResponse.getDialogs();

        List<Message> messages = messageRepository.findByToUsername(username);

        for (int i = offset; i < messages.size() && i - offset <= DEFAULT_COUNT_OF_CONTACTS; i++) {
            Message message = messages.get(i);
            Dialogs d = new Dialogs(message.getFromUsername(), message);
            dialogs.add(d);
        }

        log.debug("returned list of contacts" + contactsResponse);
        return contactsResponse;


    }


}
