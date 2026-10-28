package br.com.petshop.service;

import br.com.petshop.repository.UsuarioRepository; import org.springframework.security.core.userdetails.*; import org.springframework.stereotype.Service;

@Service public class UsuarioDetailsService implements UserDetailsService { private final UsuarioRepository repo; public UsuarioDetailsService(UsuarioRepository r){repo=r;} public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{return repo.findByUsername(username).map(u->User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRole()).build()).orElseThrow(()->new UsernameNotFoundException("Usuário não encontrado"));}}
