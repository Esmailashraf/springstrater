package org.studyeasy.springstrater.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.studyeasy.springstrater.Model.Account;
import org.studyeasy.springstrater.Services.AccountService;
import org.studyeasy.springstrater.Services.EmailService;
import org.studyeasy.springstrater.util.Apputil.AppUtil;
import org.studyeasy.springstrater.util.Email.EmailDetail;

import jakarta.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.StackWalker.Option;
import java.nio.file.*;
import java.security.Principal;

@Controller
public class AccountController {

    @Value("${password-reset-token-expiry-minutes}")
    private int tokenExpiryMinutes;

    @Value("${Site.Domain}")
    private String siteDomain;

    @Autowired
    AccountService accountService;
    @Autowired
    EmailService emailService;

    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "account_views/register";
        }
        accountService.saveAccount(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal) {
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> accountOptional = accountService.getAccountByEmail(authUser);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "account_views/profile";
        } else {

            return "redirect:/?error";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String postProfile(@Valid @ModelAttribute Account account, BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            return "account_views/profile";
        }
        String authUser = principal.getName();
        Optional<Account> userOptional = accountService.getAccountByEmail(authUser);

        if (userOptional.isPresent()) {

            Account accountById = userOptional.get();
            accountById.setFirstName(account.getFirstName());
            accountById.setLastName(account.getLastName());
            accountById.setEmail(account.getEmail());
            accountById.setPassword(account.getPassword());
            accountById.setAge(account.getAge());
            accountById.setGender(account.getGender());
            accountById.setDate_of_birth(account.getDate_of_birth());
            accountService.saveAccount(accountById);
            SecurityContextHolder.clearContext();
            return "redirect:/";
        }
        return "redirect:/?error";
    }

    @PostMapping("/upload_photo")
    @PreAuthorize("isAuthenticated()")
    public String uploadPhoto(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
            Principal principal) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "file is empty");
            return "redirect:/profile";
        }
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String randomString = RandomStringUtils.randomAlphanumeric(8);
            String newFileName = randomString + "_" + fileName;
            Path path = Paths.get(AppUtil.get_Uploaded_Path(newFileName));
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            String relativePath = "/uploads/" + newFileName;
            String authUser = principal.getName();
            Optional<Account> userOptional = accountService.getAccountByEmail(authUser);
            if (userOptional.isPresent()) {
                Account account = userOptional.get();
                account.setPhoto(relativePath);
                accountService.saveAccount(account);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed");
            return "redirect:/profile";
        }

        return "redirect:/profile";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "account_views/forgot_password";
    }

    @PostMapping("/reset-password")
    public String reset_password(@RequestParam("email") String _email, RedirectAttributes redirectAttributes,
            Model model) {
        Optional<Account> accountOptional = accountService.getAccountByEmail(_email);
        if (accountOptional.isPresent()) {
            Account account = accountService.getAccountById(accountOptional.get().getId()).get();
            String token = UUID.randomUUID().toString();
            account.setResetPasswordToken(token);
            account.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(tokenExpiryMinutes));
            accountService.saveAccount(account);
            String resetLink = siteDomain + "/change-password?token=" + token;
            EmailDetail emailDetail = new EmailDetail(account.getEmail(),
                    "Click the link to reset your password: " + resetLink, "Password Reset Request");
            if (emailService.sendSimpleMail(emailDetail) == false) {
                System.out.println("Failed to send email");
                redirectAttributes.addFlashAttribute("error", "Failed to send email");
                return "account_views/forgot_password";
            }
            redirectAttributes.addFlashAttribute("message", "Password reset link has been sent to your email");

            return "account_views/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Email not found");
            return "account_views/forgot_password";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(@RequestParam("token") String token, Model model,
            RedirectAttributes redirectAttributes) {
        if (token == null || token.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid token");
            return "account_views/forgot_password";
        }
        Optional<Account> accoutOptional = accountService.getAccountByToken(token);
        if (accoutOptional.isPresent()) {
            Account account = accoutOptional.get();
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(accoutOptional.get().getResetPasswordTokenExpiry())) {
                redirectAttributes.addFlashAttribute("error", "Email not found");
                return "account_views/forgot_password";
            }
            model.addAttribute("account", account);
            return "account_views/change_password";
        }
        redirectAttributes.addFlashAttribute("error", "Invalid token");
        return "account_views/forgot_password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute Account account, RedirectAttributes redirectAttributes) {
        if (account.getId() == null) {
            redirectAttributes.addFlashAttribute("message", "Invalid account ID");
            return "account_views/login";
        }
        Account accountId = accountService.getAccountById(account.getId()).get();
        System.out.println(accountId);
        accountId.setPassword(account.getPassword());
        accountId.setResetPasswordToken("");
        accountId.setResetPasswordTokenExpiry(null);
        accountService.saveAccount(accountId);
        redirectAttributes.addFlashAttribute("message", "Password changed successfully");
        return "account_views/login";

    }

}
