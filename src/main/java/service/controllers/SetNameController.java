package service.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.entity.Dialog;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.JSONInputNewName;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

@RestController
@RequestMapping("/{auth_token}/name")
public class SetNameController extends BaseController {
    @Autowired
    public SetNameController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }

    @RequestMapping
    ResponseEntity<?> setNameOfUser(
            @PathVariable String auth_token,
            @RequestBody JSONInputNewName newNameOfUser
    ) {

        User user = null;
        try {
            user = getUserFromDataBase(auth_token);
        } catch (IllegalArgumentException e) {
            new ResponseEntity<>(new ErrorResponseObject(e.toString()), HttpStatus.FORBIDDEN);
        }
        user.setDisplayName(newNameOfUser.getNew_name());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/{dialog_id}")
    ResponseEntity<?> setNameOfDialog(
            @PathVariable String dialog_id,
            @PathVariable String auth_token,
            @RequestBody JSONInputNewName newNameOfDialog
    ) {
        if (dialog_id.charAt(0) == '+') {
            Dialog dialog = dialogRepository.findByDialogId(dialog_id);
            if (dialog == null)
                return new ResponseEntity<>(new ErrorResponseObject("no such dialog_id"), HttpStatus.FORBIDDEN);
            dialog.setDialogName(newNameOfDialog.getNew_name());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
