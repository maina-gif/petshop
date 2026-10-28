// Desloga depois de 30s sem mexer na tela.
// Clique, tecla, scroll, toque ou mouse reiniciam o tempo.
// O servidor tambem confere (InatividadeFilter), caso o JS esteja desligado.
(function () {
    'use strict';

    const LIMITE_MS = 30 * 1000;  // 30 segundos
    const AVISO_MS = 5 * 1000;    // avisa quando faltar 5s
    const URL_EXPIRADO = '/login?expirado';
    const URL_PING = '/sessao/ping';
    const PING_MIN_MS = 5 * 1000; // no máximo 1 ping a cada 5s

    let timerLogout = null;
    let timerAviso = null;
    let intervaloContagem = null;
    let ultimaAtividade = Date.now();
    let encerrando = false;
    let ultimoPing = Date.now(); // o carregamento da página já conta como acesso

    const aviso = document.getElementById('avisoSessao');
    const contagem = document.getElementById('contagemSessao');

    function esconderAviso() {
        clearInterval(intervaloContagem);
        intervaloContagem = null;
        if (aviso) aviso.hidden = true;
    }

    function mostrarAviso() {
        if (!aviso) return;
        aviso.hidden = false;
        atualizarContagem();
        clearInterval(intervaloContagem);
        intervaloContagem = setInterval(atualizarContagem, 250);
    }

    function atualizarContagem() {
        const restante = Math.max(0, Math.ceil((LIMITE_MS - (Date.now() - ultimaAtividade)) / 1000));
        if (contagem) contagem.textContent = restante;
    }

    function encerrarSessao() {
        if (encerrando) return; // pra não rodar duas vezes
        encerrando = true;
        clearTimeout(timerLogout);
        clearTimeout(timerAviso);
        esconderAviso();
        // o servidor também invalida a sessão quando recebe essa requisição
        window.location.replace(URL_EXPIRADO);
    }

    // avisa o servidor que o usuario ta ativo (se voltar pro login, a sessao ja caiu)
    function avisarServidor() {
        const agora = Date.now();
        if (agora - ultimoPing < PING_MIN_MS) return;
        ultimoPing = agora;
        fetch(URL_PING, { credentials: 'same-origin', cache: 'no-store' })
            .then(function (resp) {
                if (resp.redirected || resp.url.indexOf('/login') !== -1) {
                    encerrarSessao();
                }
            })
            .catch(function () { /* sem rede: o timer local continua valendo */ });
    }

    function reiniciarTimer() {
        if (encerrando) return;
        ultimaAtividade = Date.now();
        avisarServidor();
        clearTimeout(timerLogout);
        clearTimeout(timerAviso);
        esconderAviso();
        timerAviso = setTimeout(mostrarAviso, LIMITE_MS - AVISO_MS);
        timerLogout = setTimeout(encerrarSessao, LIMITE_MS);
    }

    // mousemove dispara toda hora, entao so conta 1x por segundo
    let ultimoMovimento = 0;
    function aoMoverMouse() {
        const agora = Date.now();
        if (agora - ultimoMovimento > 1000) {
            ultimoMovimento = agora;
            reiniciarTimer();
        }
    }

    ['click', 'keydown', 'scroll', 'touchstart'].forEach(function (evento) {
        document.addEventListener(evento, reiniciarTimer, { passive: true });
    });
    document.addEventListener('mousemove', aoMoverMouse, { passive: true });

    // quando a aba fica em segundo plano o navegador pausa os timers, entao confere ao voltar
    document.addEventListener('visibilitychange', function () {
        if (document.visibilityState === 'visible') {
            if (Date.now() - ultimaAtividade >= LIMITE_MS) {
                encerrarSessao();
            }
        }
    });

    reiniciarTimer();
})();
