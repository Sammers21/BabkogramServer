package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.MessageResponse;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static service.controllers.SendMessageController.checkToken;
import static service.controllers.SendMessageController.checkUser;


@RestController
@RequestMapping("/{auth_token}/messages/{dialog_id}")
public class GetMessagesController {
    private static final Logger log = Logger.getLogger(GetMessagesController.class.getName());

    @RequestMapping(value = "/after/{timestamp}")
    ResponseEntity<?> getCustomMessageAfterSomeTime
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id,
                    @PathVariable long timestamp
            ) {


        return getMessages(auth_token, dialog_id, DEFAULT_COUNT_OF_MESSAGES, 0, timestamp);
    }

    @RequestMapping(value = "/after/{timestamp}/limit/{limit}")
    ResponseEntity<?> getCustomMessageAfterSomeTimeWithLimit
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id,
                    @PathVariable int limit
            ) {


        return getMessages(auth_token, dialog_id, limit, 0, 0);
    }

    @RequestMapping
    ResponseEntity<?> getDefaultMessageCount
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id
            ) {

        return getMessages(auth_token, dialog_id, DEFAULT_COUNT_OF_MESSAGES, 0, 0);
    }

    @RequestMapping(value = "/limit/{limit}")
    ResponseEntity<?> getCustomMessageCountWithoutOffset
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id,
                    @PathVariable int limit
            ) {


        return getMessages(auth_token, dialog_id, limit, 0, 0);
    }

    @RequestMapping(value = "/limit/{limit}/skip/{offset}")
    ResponseEntity<?> getCustomMessageCountWithCustomOffset
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id,
                    @PathVariable int limit,
                    @PathVariable int offset
            ) {


        return getMessages(auth_token, dialog_id, limit, offset, 0);
    }

    private ResponseEntity<?> getMessages(
            String auth_token,
            String dialog_id,
            int limit,
            int skip,
            long timestamp
    ) {
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

        MessageResponse response = new MessageResponse();
        List<Message> messageList = response.getMessages();

        List<Message> list = messageRepository.findByToUsername(user.getUsername());

        List<Message> dialog = Arrays.asList((Message[]) list.stream().filter(
                l ->
                        l.getFromUsername().equals(dialog_id)
                                && l.getTimestamp() > timestamp
        ).toArray());

        for (int i = skip; i < dialog.size() && i - skip <= limit; i++) {
            messageList.add(list.get(i));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static final int DEFAULT_COUNT_OF_MESSAGES = 25;


    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private MessageRepository messageRepository;

    @Autowired
    public GetMessagesController(UserRepository userRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.messageRepository = messageRepository;
    }


}
