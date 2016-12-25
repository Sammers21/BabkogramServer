package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.fileIO.Storage;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{auth_token}/avatar")
public class PhotoUploadController extends BaseController {

    private final Logger log = Logger.getLogger(PhotoUploadController.class.getName());
    @Autowired
    private Storage storage;


    @RequestMapping(value = "/{dialog_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPhoto(
            @PathVariable String auth_token,
            @PathVariable String dialog_id
    ) {
        if (storage.isExist(dialog_id)) {
            Resource file = storage.loadAsResource(dialog_id);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .body(file);
        } else {
            log.error("no photo for " + dialog_id);
            return new ResponseEntity<>(new ErrorResponseObject("there is no photo for dialog"), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> handleUserFileUpload(
            @RequestParam("file") MultipartFile file,
            @PathVariable String auth_token
    ) {
        try {
            User userFromDataBase = getUserFromDataBase(auth_token);
            log.info(userFromDataBase.getUsername() + " post photo");
            storage.store(file, userFromDataBase.getUsername());
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<Object>(new ErrorResponseObject(e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{dialog_id}", method = RequestMethod.POST)
    public ResponseEntity<?> handleDialogToFileUpload(
            @RequestParam MultipartFile file,
            @PathVariable String auth_token,
            @PathVariable String dialog_id
    ) {
        try {
            User userFromDataBase = null;
            try {
                userFromDataBase = getUserFromDataBase(auth_token);
            } catch (FileNotFoundException e) {
                return new ResponseEntity<>(new ErrorResponseObject(e.toString()), HttpStatus.FORBIDDEN);
            }
            if (userFromDataBase.getDialogList().stream().filter(s -> s.equals(dialog_id)).collect(Collectors.toList()).size() == 1) {
                storage.store(file, dialog_id);
                log.info(userFromDataBase.getUsername() + " post photo onto conference " + dialog_id);
                return new ResponseEntity<Object>(HttpStatus.OK);
            } else {
                log.error("no such user in this conference");
                return new ResponseEntity<Object>(new ErrorResponseObject("no such user in this conference"), HttpStatus.FORBIDDEN);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return new ResponseEntity<Object>(new ErrorResponseObject(e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }


    @Autowired
    public PhotoUploadController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }
}
