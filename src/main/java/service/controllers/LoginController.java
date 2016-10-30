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
@RequestMapping("/login")
public class LoginController {
    private static final Logger log = Logger.getLogger(LoginController.class.getName());

    @Autowired
    UserRepository userRepository;


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> generateToken(@RequestBody RegisterUserObject input) {
        log.info(String.format("someone call login with json : %s", input.toString()));

        User byUsername = userRepository.findByUsername(input.getUsername());

        if (byUsername == null) {
            return new ResponseEntity<AlredyTakenError>(new AlredyTakenError("Invalid username or password"), HttpStatus.FORBIDDEN);

        } else if (byUsername.getPassword().equals(input.getPassword())) {

            Token token = Token.generate(userRepository);

            byUsername.setToken(token.toString());

            return new ResponseEntity<Token>(token, HttpStatus.OK);
        }

        return new ResponseEntity<AlredyTakenError>(new AlredyTakenError("Invalid username or password"), HttpStatus.FORBIDDEN);

    }
}
