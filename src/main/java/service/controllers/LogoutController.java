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

@RestController
@RequestMapping("/{auth_token}/logout")
public class LogoutController extends BaseController {

    private static final Logger log = Logger.getLogger(LogoutController.class.getName());

    @Autowired
    public LogoutController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    /**
     * remove current token of user
     * @param auth_token user's token
     * @return status of operation
     */
    @RequestMapping
    ResponseEntity<?> logoutSingleToken(@PathVariable String auth_token) {

        log.debug("auth_token is " + auth_token);

        Token token = tokenRepository.findByToken(auth_token);
        if (token == null) {
            log.info("Invalid auth_token");

            return new ResponseEntity<>(new ErrorResponseObject("Invalid auth_token"), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findByUsername(token.getUsername());
        if (user == null) {
            log.info("auth_token without username has removed");

            tokenRepository.delete(token);
            return new ResponseEntity<>(new ErrorResponseObject("Invalid auth_token"), HttpStatus.FORBIDDEN);
        } else {
            tokenRepository.delete(token);

            log.info("token " + token.getToken() + "has removed ");
            log.info("username " + user.getUsername() + " logged out");


            return new ResponseEntity<>(new JSONMessage("Logged out"), HttpStatus.OK);
        }


    }
}
