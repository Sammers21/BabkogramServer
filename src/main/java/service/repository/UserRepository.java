package service.repository;


import service.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    List<User> findTop25ByUsernameContainingIgnoreCase(String username);

    List<User> findTop25ByDisplayNameContainingIgnoreCase(String displayName);
}
