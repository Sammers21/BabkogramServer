package service.repository;


import org.springframework.data.repository.CrudRepository;
import service.entity.Dialog;

import java.util.List;
import java.util.Set;


public interface DialogRepository extends CrudRepository<Dialog, Long> {
    Dialog findByDialogId(String dialogId);
}
