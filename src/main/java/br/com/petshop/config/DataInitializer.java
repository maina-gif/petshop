package br.com.petshop.config;

import br.com.petshop.entity.Usuario; import br.com.petshop.repository.UsuarioRepository; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.*; import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration public class DataInitializer { @Bean CommandLineRunner seed(UsuarioRepository repo,PasswordEncoder encoder){return args->{String user=env("ADMIN_USERNAME","admin");if(!repo.existsByUsername(user))repo.save(new Usuario(user,encoder.encode(env("ADMIN_PASSWORD","admin123")),"ADMIN"));};} private String env(String n,String d){String v=System.getenv(n);return v==null||v.isBlank()?d:v;} }
