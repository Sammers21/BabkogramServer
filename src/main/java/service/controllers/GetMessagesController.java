package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.entity.Dialog;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.MessageResponse;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static service.controllers.SendMessageController.checkToken;
import static service.controllers.SendMessageController.checkUser;


@RestController
@RequestMapping("/{auth_token}/messages/{dialog_id}")
public class GetMessagesController extends BaseController {
    private static final Logger log = Logger.getLogger(GetMessagesController.class.getName());


    @RequestMapping(value = "/after/{timestamp}")
    ResponseEntity<?> getCustomMessageAfterSomeTime
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id,
                    @PathVariable long timestamp
            ) {
        log.info("mapping /" + auth_token + "/messages/" + dialog_id + "/after/" + timestamp);

        return getMessages(auth_token, dialog_id, DEFAULT_COUNT_OF_MESSAGES, 0, timestamp);
    }

    @RequestMapping(value = "/after/{timestamp}/limit/{limit}")
    ResponseEntity<?> getCustomMessageAfterSomeTimeWithLimit
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id,
                    @PathVariable int limit,
                    @PathVariable long timestamp
            ) {
        log.info("mapping /" + auth_token + "/messages/" + dialog_id + "/after/" + timestamp + "/limit/" + limit);

        return getMessages(auth_token, dialog_id, limit, 0, 0);
    }

    @RequestMapping
    ResponseEntity<?> getDefaultMessageCount
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id
            ) {
        log.info("mapping /" + auth_token + "/messages/" + dialog_id);

        return getMessages(auth_token, dialog_id, DEFAULT_COUNT_OF_MESSAGES, 0, 0);
    }

    @RequestMapping(value = "/limit/{limit}")
    ResponseEntity<?> getCustomMessageCountWithoutOffset
            (
                    @PathVariable String auth_token,
                    @PathVariable String dialog_id,
                    @PathVariable int limit
            ) {
        log.info("mapping /" + auth_token + "/messages/" + dialog_id + "/limit/" + limit);

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

        log.info("mapping /" + auth_token + "/messages/" + dialog_id + "/limit/" + limit + "/skip/" + offset);
        return getMessages(auth_token, dialog_id, limit, offset, 0);
    }

    /**
     * template request
     *
     * @param auth_token secret token
     * @param dialog_id  id of dialog
     * @param limit      limit of messages
     * @param skip       how much messages should be skipped
     * @param timestamp  UNIX timestapm
     * @return suitable response <Error suitable too>
     */
    private ResponseEntity<?> getMessages(
            String auth_token,
            String dialog_id,
            int limit,
            int skip,
            long timestamp
    ) {
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token)) {
            log.error("invalid token");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, user)) {
            log.error("token without username");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }

        MessageResponse response = getMessageResponse(dialog_id, limit, skip, timestamp, user, auth_token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * create response for message dialog request
     *
     * @param dialog_id id of dialog
     * @param limit     limit of messages
     * @param skip      how much messages should be skipped
     * @param timestamp UNIX timestapm
     * @param toUser    ref to User from which messages
     * @return suitable response
     */
    private MessageResponse getMessageResponse(String dialog_id, int limit, int skip, long timestamp, User toUser, String auth_token) {
        MessageResponse response = new MessageResponse();

        List<Message> messageToRetunr = new ArrayList<Message>();
        List<Message> messageList = response.getMessages();

        if (dialog_id.charAt(0) == '+') {
            Dialog dialogFromDataBase = getDialogFromDataBase(auth_token, dialog_id);
            long joinTime = findJoinTime(toUser.getUsername(), dialogFromDataBase);
            List<Message> messagesToDilaog = messageRepository.findByToUsername(dialog_id);

            List<Message> collcted = messagesToDilaog.stream().
                    filter(s -> s.getTimestamp() > joinTime)
                    .sorted(Message::compareTo)
                    .collect(Collectors.toList());

            messageToRetunr.addAll(collcted);
        } else {
            List<Message> messages = messageRepository.findByToUsernameAndSender(toUser.getUsername(), dialog_id);
            List<Message> FromTo = messageRepository.findByToUsernameAndSender(dialog_id, toUser.getUsername());
            messages.addAll(FromTo);
            //unique objects
            Set<Message> setOf = new HashSet<>(messages);
            ArrayList<Message> toFrom = new ArrayList<>();
            toFrom.addAll(setOf);

            log.info("dialog between " + toUser.getUsername() + " and " + dialog_id);

            //filter messages
            List<Message> dialog = toFrom.stream().filter(
                    l ->
                            (l.getSender().equals(dialog_id) || l.getToUsername().equals(dialog_id))
                                    && l.getTimestamp() > timestamp
            ).sorted(Message::compareTo).collect(Collectors.toList());
            messageToRetunr.addAll(dialog);
        }
        //fill response object
        for (int i = skip; i < messageToRetunr.size() && i - skip <= limit; i++) {
            messageList.add(messageToRetunr.get(i));
        }
        Collections.reverse(messageList);
        log.info("dialog between " + toUser.getUsername() +
                " and " + dialog_id + " return "
                + response.getMessages().size() +
                "ones");
        if (messageList.size() > 2) {
            log.debug("last message content " + messageList.get(messageList.size() - 1).getContent());
            log.debug("last message content " + messageList.get(0).getContent());
        }

        return response;
    }

    private static final int DEFAULT_COUNT_OF_MESSAGES = 25;

    @Autowired
    public GetMessagesController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }


}
