package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.objects.RegisterUserObject;
import service.objects.Token;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private static final Logger log = Logger.getLogger(RegisterController.class.getName());

    @RequestMapping(method = RequestMethod.POST)
    Token generateToken(@RequestBody RegisterUserObject input) {
        log.info("/regisert map");

        log.info(String.format("input %s", input));

        return new Token("kek");
    }
}
