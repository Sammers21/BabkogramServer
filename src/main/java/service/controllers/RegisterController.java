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


    /**
     * method who responsible for registr new users
     * @param input register user data
     * @return status of operation
     */
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> registerNewUser(@RequestBody RegisterUserObject input) {
        log.info(String.format("someonce call register with json : %s", input.toString()));
        if (!isRegistred(input.getUsername())) {
            Token token = getTokenAndSaveUserInRepository(input);
            log.info("Username " + input.getUsername() + " been register");
            return new ResponseEntity<>(token.getJSONObject(), HttpStatus.OK);
        } else {
            log.info("Username " + input.getUsername() + " already taken");
            return new ResponseEntity<>(new ErrorResponseObject("Username already taken"), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * method to insert new token for current user
     * @param input register user data
     * @return token for new user
     */
    private Token getTokenAndSaveUserInRepository(RegisterUserObject input) {
        Token token = Token.generate(tokenRepository, input.getUsername());

        User user = new User(input.getUsername(), input.getPassword());

        userRepository.save(user);
        return token;
    }

    /**
     * check for registration method
     * @param username username to check
     * @return is this user within a database ?
     */
    private boolean isRegistred(String username) {
        User byUsername = userRepository.findByUsername(username);
        return byUsername != null;
    }


}
