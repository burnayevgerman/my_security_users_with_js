//package ru.kata.spring.boot_security.demo.controllers;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import ru.kata.spring.boot_security.demo.models.Role;
//import ru.kata.spring.boot_security.demo.models.User;
//import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
//import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Controller
//public class AuthController {
//    private final UserServiceImpl userService;
//    private final RoleServiceImpl roleService;
//    private final DaoAuthenticationProvider daoAuthenticationProvider;
//
//    public AuthController(UserServiceImpl userService,
//                          DaoAuthenticationProvider daoAuthenticationProvider,
//                          RoleServiceImpl roleService) {
//        this.userService = userService;
//        this.roleService = roleService;
//        this.daoAuthenticationProvider = daoAuthenticationProvider;
//    }
//
//    @GetMapping("/register")
//    public String register(Model model) {
//        model.addAttribute("user", new User());
//        model.addAttribute("allRoles", roleService.getAllRoles());
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String createNewUser(HttpServletRequest request,
//                                HttpServletResponse response,
//                                @RequestParam(value = "selectedRoles", required = false) Set<Long> selectedRoles,
//                                @ModelAttribute("user") User user) {
//        try {
//            if (selectedRoles != null) {
//                user.setRoles(selectedRoles
//                        .stream()
//                        .map(roleService::findRoleById)
//                        .collect(Collectors.toSet()));
//            } else {
//                user.setRoles(new HashSet<>());
//            }
//
//            String tempPasswordNotEncrypt = user.getPassword();
//            user = userService.createUser(user);
//
//            if (user == null) {
//                return "redirect:/register?error";
//            }
//
//            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                    user.getEmail(),
//                    tempPasswordNotEncrypt,
//                    user.getAuthorities()
//            );
//
//            Authentication authentication = daoAuthenticationProvider.authenticate(token);
//            SecurityContext securityContext = SecurityContextHolder.getContext();
//            securityContext.setAuthentication(authentication);
//            HttpSession session = request.getSession(true);
//            session.setAttribute(
//                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
//                    securityContext
//            );
//
//            if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().contains("ADMIN"))) {
//                return "redirect:/admin";
//            } else if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().contains("USER"))) {
//                return "redirect:/user";
//            } else {
//                return "redirect:/";
//            }
//        } catch (Exception e) {
//            return "redirect:/register?error";
//        }
//    }
//}
