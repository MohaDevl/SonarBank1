package com.BSS.Bank.System.service;

import com.BSS.Bank.System.dto.TransferDTO;
import com.BSS.Bank.System.model.Account;
import com.BSS.Bank.System.model.Transaction;
import com.BSS.Bank.System.model.User;
import com.BSS.Bank.System.repository.AccountRepository;
import com.BSS.Bank.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    public Account createAccount(Account account) {
        // Business logic to create an account
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        // Business logic to retrieve an account by ID
        return accountRepository.findById(id).orElse(null);
    }

    public boolean withdrawFromAccount(Long userId, Long amount) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        } else {
            User userObj = user.get();
            if (userObj.getAccount().getBalance() < amount) {
                return false;
            } else {
                Account account = userObj.getAccount();
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);
                recordWithdrawlTransaction(userObj, amount);
                return true;
            }
        }
    }

    private void recordWithdrawlTransaction(User user, double amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount("-" + amount);
        transaction.setDate(new Date());
        transaction.setDescription("Withdrawal");
        transaction.setUser(user);
        user.getTransactions().add(transaction);
        userRepository.save(user);
    }

    private void recordDepositTransaction(User user, double amount) {
        Transaction transaction = new Transaction();
        transaction.setAmount("+" + amount);
        transaction.setDate(new Date());
        transaction.setDescription("Deposit");
        transaction.setUser(user);
        user.getTransactions().add(transaction);
        userRepository.save(user);
    }


    public void depositIntoAccount(Long userId, Long amount) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        } else {
            User userObj = user.get();
            Account account = userObj.getAccount();
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
            recordDepositTransaction(userObj, amount);
        }
    }


    public String transferToAccount(Long userId, TransferDTO transferDTO) {
        Optional<User> sender = userRepository.findById(userId);
        if (sender.isEmpty()) {
            return "Sender not found";
        } else {
            User senderObj = sender.get();
            if (senderObj.getAccount().getBalance() < transferDTO.getAmount()) {
                return "There is not enough balance in your account";
            } else {
                Optional<Account> receiverAccount = accountRepository.findById(transferDTO.getReceiverAccountNumber());
                if (receiverAccount.isEmpty()) {
                    return "Receiver account not found";
                } else if (Objects.equals(receiverAccount.get().getUser().getId(), userId)) {
                    return "You can't transfer money to yourself";
                } else {

                    Account receiverAccountObj = receiverAccount.get();
                    senderObj.getAccount().setBalance(senderObj.getAccount().getBalance() - transferDTO.getAmount());
                    receiverAccountObj.setBalance(receiverAccountObj.getBalance() + transferDTO.getAmount());
                    accountRepository.save(receiverAccountObj);

                    userRepository.save(senderObj);
                    recordTransferTransaction(receiverAccountObj.getUser(), transferDTO.getAmount(), "+");
                    recordTransferTransaction(senderObj, transferDTO.getAmount(), "-");
                    return "Transfer successful";
                }
            }
        }
    }

    private void recordTransferTransaction(User user, double amount, String symbol) {
        Transaction transaction = new Transaction();
        transaction.setAmount(symbol + amount);
        transaction.setDate(new Date());
        transaction.setDescription("Transfer");
        transaction.setUser(user);
        user.getTransactions().add(transaction);
        userRepository.save(user);
    }


}