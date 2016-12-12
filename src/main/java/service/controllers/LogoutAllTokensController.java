package service.controllers;


import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.JSONMessage;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/{auth_token}/logoutall")
public class LogoutAllTokensController extends BaseController {

    private static final Logger log = Logger.getLogger(LogoutAllTokensController.class.getName());

    @Autowired
    public LogoutAllTokensController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    /**
     *  delete all tokens of some user
     * @param auth_token users token
     * @return status of operation
     */
    @RequestMapping
    ResponseEntity<?> logoutAllTokens(@PathVariable String auth_token) {

        log.debug("auth_token is " + auth_token);

        Token token = tokenRepository.findByToken(auth_token);
        if (token == null) {
            log.error("Invalid auth_token");

            return new ResponseEntity<>(new ErrorResponseObject("Invalid auth_token"), HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findByUsername(token.getUsername());
        if (user == null) {
            log.error("auth_token without username has removed");
            tokenRepository.delete(token);
            return new ResponseEntity<>(new ErrorResponseObject("Invalid auth_token"), HttpStatus.FORBIDDEN);
        } else {
            deleteAllUsersTokens(user);
            log.info("username " + user.getUsername() + " logged out");
            return new ResponseEntity<>(new JSONMessage("Logged out"), HttpStatus.OK);
        }


    }

    /**
     * this method delete all tokens of some user
     * @param user user which tokens need to delete
     */
    private void deleteAllUsersTokens(User user) {
        List<Token> TokenList = tokenRepository.findByUsername(user.getUsername());
        for (Token aTokenList : TokenList) {
            tokenRepository.delete(aTokenList);
            log.info("token " + aTokenList.getToken() + "has removed ");
        }
    }
}
