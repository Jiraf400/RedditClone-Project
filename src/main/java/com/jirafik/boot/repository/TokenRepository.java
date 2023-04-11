package com.jirafik.boot.repository;

import com.jirafik.boot.security.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequestMapping
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            select t from Token t  inner join Person p on t.person.personId = p.personId
            where p.personId = :person_id and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokensByUser(Integer person_id);

    Optional<Token> findByToken(String token);
}
