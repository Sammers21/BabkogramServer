package service.repository;

import service.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByContent(String content);

    List<Message> findByToUsername(String toUsername);

    List<Message> findByToUsernameAndSender(String toUsername, String sender);

    Message findByGuid(String guid);

}
