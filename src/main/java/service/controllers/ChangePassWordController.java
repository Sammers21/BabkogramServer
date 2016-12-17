package service.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.PairOfNewAndOldPassword;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

@RestController
@RequestMapping("/{auth_token}")
public class ChangePassWordController extends BaseController {
    private static final Logger log = Logger.getLogger(ChangePassWordController.class.getName());

    /**
     * Controller aimed to hcange password
     * @param auth_token token
     * @param pair pair of old and new password
     * @return response
     */
    @RequestMapping()
    ResponseEntity<?> changePassword(
            @PathVariable String auth_token,
            @RequestBody PairOfNewAndOldPassword pair
    ) {

        User userFromDataBase;
        try {
            userFromDataBase = getUserFromDataBase(auth_token);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ErrorResponseObject(e.getMessage()), HttpStatus.FORBIDDEN);
        }
        if (!userFromDataBase.getPassword().equals(pair.getPassword())) {
            log.error("invalid passowrd from " + userFromDataBase.getUsername());
            return new ResponseEntity<>(new ErrorResponseObject("invalid password"), HttpStatus.FORBIDDEN);
        }
        userFromDataBase.setPassword(pair.getNew_password());
        userRepository.save(userFromDataBase);
        log.info("new password for " + userFromDataBase.getUsername() + " is " + pair.getNew_password());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public ChangePassWordController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }
}
