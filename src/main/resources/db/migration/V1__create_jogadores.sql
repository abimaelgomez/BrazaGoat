CREATE TABLE IF NOT EXISTS jogadores (
    id_jogador UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    posicao VARCHAR(50) NOT NULL,
    idade INTEGER NOT NULL,
    numero_camisa INTEGER NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    status_do_jogador_partida VARCHAR(50),
    pontuacao_dapartida BIGINT DEFAULT 0,
    pontuacao_total BIGINT DEFAULT 0,
    pontuacao_media BIGINT DEFAULT 0,
    gols_marcados INTEGER DEFAULT 0,
    assistencias INTEGER DEFAULT 0,
    partidas_disputadas INTEGER DEFAULT 0,
    minutos_jogados INTEGER NOT NULL DEFAULT 0
);
