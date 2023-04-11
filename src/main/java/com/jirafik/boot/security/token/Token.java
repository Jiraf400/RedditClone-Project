package com.jirafik.boot.security.token;

import com.jirafik.boot.entity.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String token;
    @Enumerated(STRING)
    private TokenType type;
    private boolean expired;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}




















