package com.BSS.Bank.System;

import com.BSS.Bank.System.model.Account;
import com.BSS.Bank.System.model.Transaction;
import com.BSS.Bank.System.model.User;
import com.BSS.Bank.System.repository.AccountRepository;
import com.BSS.Bank.System.repository.TransactionRepository;
import com.BSS.Bank.System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class BankSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BankSystemApplication.class, args);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;


    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void run(String... args) throws Exception {

       
    }
}
