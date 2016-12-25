package service.controllers;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.entity.TimeStatistics;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.TImeStatResponse;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/{auth_token}/spelling")
public class StatisticsController extends BaseController {
    private final Logger log = Logger.getLogger(StatisticsController.class.getName());

    @RequestMapping("/{user_id}")
    ResponseEntity<?> getStatelats30Days(
            @PathVariable String auth_token,
            @PathVariable String user_id
    ) {

        User userFromDataBase;
        try {
            userFromDataBase = getUserFromDataBase(auth_token);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<Object>(new ErrorResponseObject(e.getMessage()), HttpStatus.FORBIDDEN);
        }
        User byUsername = userRepository.findByUsername(user_id);
        if (byUsername == null) {
            log.error("no such user");
            return new ResponseEntity<Object>(new ErrorResponseObject("no such user"), HttpStatus.FORBIDDEN);
        } else {
            //filter by last 30 days
            Set<TimeStatistics> statistics = byUsername.getStatistics().stream()
                    .filter(s -> {
                        DateTime now = DateTime.now();
                        int days = Days.daysBetween(s.getDate().toLocalDate(), now.toLocalDate()).getDays();
                        if (days > 30)
                            return false;
                        else
                            return true;
                    }).collect(Collectors.toSet());
            //calculate statistics
            long tm = statistics.stream().
                    mapToLong(TimeStatistics::getCoutOfMistakes).reduce(0, (x, y) -> x + y);
            long total = statistics.stream().
                    mapToLong(TimeStatistics::getTotalCoutOfWords).reduce(0, (x, y) -> x + y);
            return new ResponseEntity<Object>(new TImeStatResponse(total, tm), HttpStatus.OK);
        }

    }

    @RequestMapping("/{user_id}/month/{year}/{month}")
    ResponseEntity<?> getState(
            @PathVariable String auth_token,
            @PathVariable String user_id,
            @PathVariable Long year,
            @PathVariable Long month
    ) {

        User userFromDataBase;
        try {
            userFromDataBase = getUserFromDataBase(auth_token);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<Object>(new ErrorResponseObject(e.getMessage()), HttpStatus.FORBIDDEN);
        }
        User byUsername = userRepository.findByUsername(user_id);
        if (byUsername == null) {
            log.error("no such user");
            return new ResponseEntity<Object>(new ErrorResponseObject("no such user"), HttpStatus.FORBIDDEN);
        } else {
            //get for some month
            Set<TimeStatistics> statistics = byUsername.getStatistics().stream()
                    .filter(s -> s.getMonth() == month && s.getYear() == year)
                    .collect(Collectors.toSet());
            //calculate statistics
            long tm = statistics.stream().
                    mapToLong(TimeStatistics::getCoutOfMistakes).reduce(0, (x, y) -> x + y);
            long total = statistics.stream().
                    mapToLong(TimeStatistics::getTotalCoutOfWords).reduce(0, (x, y) -> x + y);
            return new ResponseEntity<Object>(new TImeStatResponse(total, tm), HttpStatus.OK);

        }

    }


    @Autowired
    public StatisticsController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }
}
