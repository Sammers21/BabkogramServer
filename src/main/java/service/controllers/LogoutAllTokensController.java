package service.controllers;


import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.JSONMessage;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/{auth_token}/logoutall")
public class LogoutAllTokensController {

    private static final Logger log = Logger.getLogger(LogoutAllTokensController.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @RequestMapping
    ResponseEntity<?> logoutAllTokens(@PathVariable String auth_token) {

        log.debug("auth varible is " + auth_token);

        Token token = tokenRepository.findByToken(auth_token);
        if (token == null) {
            log.info("Invalid auth_token");

            return new ResponseEntity<>(new ErrorResponseObject("Invalid auth_token"), HttpStatus.FORBIDDEN);
        }

        log.debug("res of findbyToken query " + token);
        User user = userRepository.findByUsername(token.getUsername());
        log.debug("res of findbyUsername query " + token);
        if (user == null) {
            log.info("auth_token without username has removed");
            tokenRepository.delete(token);
            return new ResponseEntity<>(new ErrorResponseObject("Invalid auth_token"), HttpStatus.FORBIDDEN);
        } else {
            deleteAllUsersTokens(user);
            log.info("username " + user.getUsername() + " logged out");
            return new ResponseEntity<>(new JSONMessage("Logged out"), HttpStatus.OK);
        }


    }

    private void deleteAllUsersTokens(User user) {
        List<Token> TokenList = tokenRepository.findByUsername(user.getUsername());
        for (Token aTokenList : TokenList) {
            tokenRepository.delete(aTokenList);
            log.info("token " + aTokenList.getToken() + "has removed ");
        }
    }
}
