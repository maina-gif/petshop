package br.com.petshop.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Confere a inatividade no servidor tambem, porque o JS do navegador pode ser desligado.
// Nao deu pra usar so server.servlet.session.timeout porque o Tomcat arredonda pra 1 minuto.
public class InatividadeFilter extends OncePerRequestFilter {

    public static final long LIMITE_MS = 30_000;
    private static final String ULTIMO_ACESSO = "ULTIMO_ACESSO";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // css e js nao contam como atividade
        String uri = request.getRequestURI();
        return uri.startsWith("/css/") || uri.startsWith("/js/") || uri.equals("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        long agora = System.currentTimeMillis();

        // so controla quem ta logado
        boolean logado = session != null
                && session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) != null;

        if (logado) {
            Long ultimo = (Long) session.getAttribute(ULTIMO_ACESSO);
            if (ultimo != null && agora - ultimo > LIMITE_MS) {
                session.invalidate();
                if (!request.getRequestURI().equals("/login")) {
                    response.sendRedirect(request.getContextPath() + "/login?expirado");
                    return;
                }
                chain.doFilter(request, response);
                return;
            }
            session.setAttribute(ULTIMO_ACESSO, agora);
        }

        chain.doFilter(request, response);
    }
}
