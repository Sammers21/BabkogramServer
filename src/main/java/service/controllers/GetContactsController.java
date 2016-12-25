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
import service.objects.DialogMessageInResponse;
import service.objects.ErrorResponseObject;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static service.controllers.SendMessageController.checkToken;
import static service.controllers.SendMessageController.checkUser;

@RestController
@RequestMapping("/{auth_token}/contacts")
public class GetContactsController extends BaseController {
    private static final Logger log = Logger.getLogger(GetContactsController.class.getName());

    private static final long DEFAULT_COUNT_OF_CONTACTS = 25;

    @Autowired
    public GetContactsController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    /**
     * default method
     *
     * @param auth_token user's token
     * @return response
     */
    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<?> getDefaultCountOfContacts(
            @PathVariable String auth_token
    ) {
        log.debug("mapping:/" + auth_token + "/contacts");
        log.debug("defaultCount of contacts");
        return getResponseEntityWithOffSet(auth_token, 0);
    }

    /**
     * method to get not default count
     *
     * @param auth_token user's token
     * @param offset     how much dialogs should be skipped
     * @return response
     */
    @RequestMapping(value = "/offset/{offset}", method = RequestMethod.GET)
    ResponseEntity<?> getCustomCountOfContacts(
            @PathVariable String auth_token,
            @PathVariable int offset
    ) {
        log.debug("mapping:/" + auth_token + "/contacts" + "/offset/" + offset);

        return getResponseEntityWithOffSet(auth_token, offset);
    }


    /**
     * method to realise following methods
     *
     * @param auth_token user's token
     * @param offset     how much dialogs should be skipped
     * @return response
     */
    private ResponseEntity<?> getResponseEntityWithOffSet(
            String auth_token, int offset) {
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token)) {
            log.error("invalid token");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }

        User current = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, current)) {
            log.error("token without username");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(getContactsWithOffset(current, offset), HttpStatus.OK);
    }

    /**
     * insert from repository method
     *
     * @param user   user
     * @param offset how much dialogs should be skipped
     * @return response
     */
    private ContactsResponse getContactsWithOffset(User user, int offset) {

        String currentUsername = user.getUsername();

        ContactsResponse contactsResponse = new ContactsResponse();
        ArrayList<DialogMessageInResponse> dialogs = contactsResponse.getDialogs();

        //query mesages
        List<Message> messages = messageRepository.findByToUsername(currentUsername);
        List<Message> messages2 = messageRepository.findBySender(currentUsername);


        //map to fill
        HashMap<String, Message> loginUserMap = new HashMap<>();
        //fill map

        //dialogs
        List<Message> DialoglastMessages = user.getDialogList()
                .stream()
                .map(s -> messageRepository.findByToUsername(s))
                .map(s -> s.stream().max(Comparator.comparingLong(Message::getTimestamp)).get())
                .collect(Collectors.toList());

        for (Message message : DialoglastMessages) {
            loginUserMap.put(message.getToUsername(), message);
        }

        for (Message message : messages) {
            if (loginUserMap.containsKey(message.getSender())
                    && loginUserMap.get(message.getSender()).getTimestamp() < message.getTimestamp()) {
                loginUserMap.put(message.getSender(), message);
            } else if (!loginUserMap.containsKey(message.getSender())) {
                loginUserMap.put(message.getSender(), message);
            }
        }
        for (Message message : messages2) {
            if (loginUserMap.containsKey(message.getToUsername())
                    && loginUserMap.get(message.getToUsername()).getTimestamp() < message.getTimestamp()) {
                loginUserMap.put(message.getToUsername(), message);
            } else if (!loginUserMap.containsKey(message.getToUsername())) {
                loginUserMap.put(message.getToUsername(), message);
            }
        }


        //sort messages by timestamp
        //List<Message> uniqMessages = loginUserMap.values().stream().sorted(Message::compareTo).collect(Collectors.toList());
        List<String> strings = loginUserMap.keySet().stream()
                .sorted(Comparator.comparingLong(x -> loginUserMap.get(x).getTimestamp()))
                .collect(Collectors.toList());

        for (int i = offset; i < loginUserMap.keySet().size() && i - offset < DEFAULT_COUNT_OF_CONTACTS; i++) {
            String message = strings.get(i);
            DialogMessageInResponse d;
            d = new DialogMessageInResponse(message, loginUserMap.get(message));
            dialogs.add(d);
        }
        log.debug("returned list of contacts: " + contactsResponse.getDialogs().size());
        return contactsResponse;


    }


}
