package com.BSS.Bank.System.controller;

import com.BSS.Bank.System.dto.TransferDTO;
import com.BSS.Bank.System.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/withdraw")
    public String withdrawForm(Model model) {
        if (UserController.currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("userId", UserController.currentUser.getId());
        model.addAttribute("message", null);
        return "withdrawForm";
    }

    @PostMapping("/withdraw/submit/{userId}")
    public String withdrawFormSubmit(@PathVariable(name = "userId") Long userId, @RequestParam(name = "amount") Long amount, Model model) {
        if (UserController.currentUser == null) {
            return "redirect:/login";
        }
        boolean withdrawSuccess = accountService.withdrawFromAccount(userId, amount);
        if (withdrawSuccess) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("message", "Insufficient funds");
            return "withdrawForm";
        }
    }


    // Add more endpoints as needed...

    @GetMapping("/deposit")
    public String depositForm(Model model) {
        if (UserController.currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("userId", UserController.currentUser.getId());
        model.addAttribute("message", null);
        return "depositForm";
    }

    @PostMapping("/deposit/submit/{userId}")
    public String depositFormSubmit(@PathVariable(name = "userId") Long userId, @RequestParam(name = "amount") Long amount, Model model) {
        if (UserController.currentUser == null) {
            return "redirect:/login";
        }
        accountService.depositIntoAccount(userId, amount);
        return "redirect:/dashboard";
    }


    @GetMapping("/transfer")
    public String transferForm(Model model) {
        if (UserController.currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("userId", UserController.currentUser.getId());
        model.addAttribute("transferDTO", new TransferDTO());
        model.addAttribute("message", null);
        return "transferForm";
    }

    @PostMapping("/transfer/submit/{userId}")
    public String transferFormSubmit(@PathVariable(name = "userId") Long userId, @ModelAttribute TransferDTO transferDTO, Model model) {
        if (UserController.currentUser == null) {
            return "redirect:/login";
        }
        String message = accountService.transferToAccount(userId, transferDTO);
        if (message.equalsIgnoreCase("Transfer successful")) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("message", message);
            return "transferForm";
        }
    }


}