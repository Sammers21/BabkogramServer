package service.repository;


import org.springframework.data.repository.CrudRepository;
import service.entity.Dialog;




public interface DialogRepository extends CrudRepository<Dialog, Long> {
    Dialog findByDialogId(String dialogId);
}
