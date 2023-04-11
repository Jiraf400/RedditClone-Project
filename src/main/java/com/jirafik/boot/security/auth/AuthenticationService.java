package com.jirafik.boot.security.auth;

import com.jirafik.boot.entity.Person;
import com.jirafik.boot.entity.Role;
import com.jirafik.boot.repository.PersonRepository;
import com.jirafik.boot.repository.TokenRepository;
import com.jirafik.boot.security.mailSender.MailService;
import com.jirafik.boot.security.mailSender.NotificationEmail;
import com.jirafik.boot.security.service.JwtService;
import com.jirafik.boot.security.token.Token;
import com.jirafik.boot.security.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepository personRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private final AuthenticationManager manager;
    private final MailService mailService;


    public AuthenticationResponse register(RegisterRequest request) {
        var person = Person.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        var savedUser = personRepository.save(person);
        var jwtToken = service.generateToken(person);
        saveToken(savedUser, jwtToken);
        mailService.sendMail(new NotificationEmail("You need to authorize",
                person.getEmail(), "Thank you for signing up to RedditClone, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/verify/" + jwtToken));

        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var person = personRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = service.generateToken(person);
        revokeAllTokens(person);
        saveToken(person, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    private void revokeAllTokens(Person person) {
        var validTokens = tokenRepository.findAllValidTokensByUser(person.getPersonId());
        if (validTokens.isEmpty()) return;


        for (Token t : validTokens) {
            t.setExpired(true);
            t.setRevoked(true);
        }
        tokenRepository.saveAll(validTokens);
    }

    private void saveToken(Person person, String jwtToken) {
        var token = Token.builder()
                .person(person)
                .token(jwtToken)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public Person getCurrentUser() {
        Person principal = (Person) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return personRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}



































