package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.entity.User;
import service.objects.RegisterUserObject;
import service.objects.Token;
import service.objects.AlredyTakenError;
import service.repository.UserRepository;

import java.util.Iterator;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private static final Logger log = Logger.getLogger(RegisterController.class.getName());

    @Autowired
    UserRepository userRepository;


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> generateToken(@RequestBody RegisterUserObject input) {
        User byUsername = userRepository.findByUsername(input.getUsername());
        log.debug("usr " + byUsername);
        if (byUsername == null) {

            Token token = Token.generate(userRepository);

            User user = new User(input.getUsername(), input.getPassword());

            user.setToken(token.toString());

            userRepository.save(new User(input.getUsername(), input.getPassword()));

            return new ResponseEntity<Token>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<AlredyTakenError>(new AlredyTakenError(), HttpStatus.FORBIDDEN);
        }
    }
}
