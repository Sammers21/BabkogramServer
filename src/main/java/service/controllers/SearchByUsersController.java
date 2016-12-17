package service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.entity.User;
import service.objects.SearchResponse;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{auth_token}/search_users")
public class SearchByUsersController extends BaseController {
    @Autowired
    public SearchByUsersController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    @RequestMapping("/{search_request}")
    ResponseEntity<?> search(
            @PathVariable String auth_token,
            @PathVariable String search_request

    ) {

        List<String> userIds = userRepository
                .findTop25ByUsernameContaining(search_request)
                .stream()
                .map(s -> s.getUsername())
                .collect(Collectors.toList());

        return new ResponseEntity<>(new SearchResponse(userIds), HttpStatus.OK);


    }
}
