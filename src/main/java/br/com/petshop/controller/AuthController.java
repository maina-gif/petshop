package br.com.petshop.controller;

import jakarta.servlet.http.HttpSession; import org.springframework.security.authentication.*; import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; import org.springframework.security.core.context.*;  import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") public class AuthController { private final AuthenticationManager manager; public AuthController(AuthenticationConfiguration c)throws Exception{manager=c.getAuthenticationManager();}
 @PostMapping("/login") public String login(@RequestParam String username,@RequestParam String password,HttpSession session){var auth=manager.authenticate(new UsernamePasswordAuthenticationToken(username,password));SecurityContextHolder.getContext().setAuthentication(auth);return "Login realizado";}
 @PostMapping("/logout") public String logout(HttpSession session){session.invalidate();return "Logout realizado";}
}
