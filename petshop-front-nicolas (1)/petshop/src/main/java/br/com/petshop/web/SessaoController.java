package br.com.petshop.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessaoController {

    // O inatividade.js chama esta rota quando o usuário mexe na tela
    // (mouse, teclado...) sem trocar de página. Assim o servidor também
    // fica sabendo que ele está ativo. Não devolve conteúdo nenhum.
    @GetMapping("/sessao/ping")
    public ResponseEntity<Void> ping() {
        return ResponseEntity.noContent().build();
    }
}
