package service.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

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

}
