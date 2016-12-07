package service.repository;


import org.springframework.data.repository.CrudRepository;
import service.entity.Message;

import java.awt.*;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
    Dialog findByDialog_id(String dialog_id);
}
