package com.BSS.Bank.System.service;

import com.BSS.Bank.System.dto.RegisterDTO;
import com.BSS.Bank.System.model.Account;
import com.BSS.Bank.System.model.User;
import com.BSS.Bank.System.repository.AccountRepository;
import com.BSS.Bank.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    public void saveUser(RegisterDTO registerDTO) {
        // Here we're assuming the username is unique and can be used as a key
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(registerDTO.getUsername(), registerDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists with username: " + registerDTO.getUsername());
        } else {
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            user.setEmail(registerDTO.getEmail());
            user.setPassword(registerDTO.getPassword());

            // Create a new account for the user
            Account account = new Account();
            account.setBalance(0.0);
            account.setAccountType(registerDTO.getAccountType());
            user.setAccount(accountRepository.save(account));
            userRepository.save(user);
            account.setUser(user);
            accountRepository.save(account);
        }
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> existingUser = userRepository.findByUsernameAndPassword(username, password);
        // Authentication failed
        return existingUser.isPresent(); // User is authenticated successfully
    }

    // Optionally, add a method to retrieve a user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}