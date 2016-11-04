package service.repository;


import org.springframework.data.repository.CrudRepository;
import service.entity.Token;
import service.entity.User;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findByToken(String token);

    List<Token> findByUsername(String username);

    List<Token> deleteByUsername(String username);
}
