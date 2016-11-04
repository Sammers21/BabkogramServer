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
import service.objects.JSONToken;
import service.objects.RegisterUserObject;
import service.entity.Token;
import service.objects.ErrorResponseObject;
import service.repository.TokenRepository;
import service.repository.UserRepository;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private static final Logger log = Logger.getLogger(RegisterController.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> generateToken(@RequestBody RegisterUserObject input) {
        log.info(String.format("someonce call register with json : %s", input.toString()));
        if (!isRegistred(input.getUsername())) {
            Token token = getTokenAndSaveUserInRepository(input);
            return new ResponseEntity<JSONToken>(token.getJSONObject(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ErrorResponseObject>(new ErrorResponseObject("Username already taken"), HttpStatus.FORBIDDEN);
        }
    }

    private Token getTokenAndSaveUserInRepository(RegisterUserObject input) {
        Token token = Token.generate(tokenRepository, input.getUsername());

        User user = new User(input.getUsername(), input.getPassword());

        userRepository.save(new User(input.getUsername(), input.getPassword()));
        return token;
    }

    private boolean isRegistred(String username) {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername == null)
            return false;
        return true;
    }


}
