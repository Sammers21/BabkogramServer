package service.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.entity.Dialog;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static service.controllers.SendMessageController.checkToken;
import static service.controllers.SendMessageController.checkUser;

public class BaseController {


    @Autowired
    public BaseController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.messageRepository = messageRepository;
        this.dialogRepository = dialogRepository;
    }

    protected DialogRepository dialogRepository;
    protected UserRepository userRepository;
    protected TokenRepository tokenRepository;
    protected MessageRepository messageRepository;

    static boolean checkToken(String auth_token, Token token) {
        if (token == null) {
            return true;
        }
        return false;
    }

    protected User getUserFromDataBase(String auth_token) throws IllegalArgumentException {
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token)) {
            throw new IllegalArgumentException("Token is not exist");
        }
        User user = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, user)) {
            throw new IllegalArgumentException("no such User with this token");
        }
        return user;
    }

    protected Dialog getDialogFromDataBase(String auth_token, String dialog_id) throws IllegalArgumentException {
        if (dialog_id.charAt(0) != '+') {
            throw new IllegalArgumentException("dialog nae must start with +");
        }
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token)) {
            throw new IllegalArgumentException("Token is not exist");
        }
        User user = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, user)) {
            throw new IllegalArgumentException("no such User with this token");
        }
        Dialog byDialogId = dialogRepository.findByDialogId(dialog_id);
        if (byDialogId == null) {
            throw new IllegalArgumentException("no such dialog");
        }
        if (byDialogId.contains(user.getUsername()) || user.getUsername().equals("BATYA"))
            return byDialogId;
        else {
            throw new IllegalArgumentException("user does not contain in dialog");
        }
    }

    protected void sendMesaageToDialogMembers(Message message, Dialog dialog) {
        dialog.getUserNameList()
                .forEach((String s) -> {
                    if (!s.equals(message.getSender())) {
                        Message messageToSend = new Message(
                                Instant.now().getEpochSecond(),
                                genereteGuid(messageRepository),
                                message.getType(),
                                message.getContent(),
                                message.getSender(),
                                s
                        );
                        messageRepository.save(messageToSend);
                    }
                });
    }

    public void systemDialogAssertion(String textOfmessage, Dialog dialog) {
        Message messageToSend = new Message(
                Instant.now().getEpochSecond(),
                genereteGuid(messageRepository),
                "system",
                textOfmessage,
                "",
                dialog.getDialogId()
        );
        messageRepository.save(messageToSend);
        // sendMesaageToDialogMembers(messageToSend, dialog);
    }

    public long findJoinTime(String username, Dialog dialog) {

        List<Message> created =
                messageRepository.findBySenderAndContent("", "created");
        List<Message> inv =
                messageRepository.findBySenderAndContent("", "invited|" + username);
        created.addAll(inv);
        if (created.stream()
                .filter(s -> s.getToUsername().equals(dialog.getDialogId()))
                .max(Comparator.comparingLong(Message::getTimestamp))
                .isPresent()) {
            return created.stream()
                    .filter(s -> s.getToUsername().equals(dialog.getDialogId()))
                    .max(Comparator.comparingLong(Message::getTimestamp)).get().getTimestamp();
        }
        return 0;

    }

    /**
     * generate UUID
     *
     * @param messageRepository repo in which we need to insert new user
     * @return UUID
     */
    public static String genereteGuid(MessageRepository messageRepository) {
        UUID randomUUID;

        do {
            randomUUID = UUID.randomUUID();
        } while (messageRepository.findByGuid(randomUUID.toString()) != null);

        return randomUUID.toString();

    }


}
