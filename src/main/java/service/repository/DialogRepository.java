package service.repository;


import org.springframework.data.repository.CrudRepository;
import service.entity.Dialog;

import java.util.List;


public interface DialogRepository extends CrudRepository<Dialog, Long> {
    Dialog findByDialogId(String dialogId);

    List<Dialog> findByOwner(String owner);
}
