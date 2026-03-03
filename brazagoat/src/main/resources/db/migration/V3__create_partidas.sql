CREATE TABLE IF NOT EXISTS partidas (
    id_partida BIGSERIAL PRIMARY KEY,
    numero_da_partida INTEGER NOT NULL,
    status_partida VARCHAR(50),
    placar_equipe_a INTEGER DEFAULT 0,
    placar_equipe_b INTEGER DEFAULT 0,
    partida_iniciada BOOLEAN DEFAULT FALSE,
    partida_finalizada BOOLEAN DEFAULT FALSE,
    tempo_max_duracao BIGINT,
    tempo_max_duracao_acrescismo BIGINT,
    tempo_de_partida BIGINT,
    tempo_de_acrescimo BIGINT,
    data_inicio DATE,
    data_final DATE,
    hora_do_inicio TIME,
    hora_do_final TIME,
    sorteio_id UUID REFERENCES sorteios(id_sorteio)
);

CREATE TABLE IF NOT EXISTS partida_jogadores_equipe_a (
    partida_id BIGINT REFERENCES partidas(id_partida),
    jogador_id UUID REFERENCES jogadores(id_jogador),
    PRIMARY KEY (partida_id, jogador_id)
);

CREATE TABLE IF NOT EXISTS partida_jogadores_equipe_b (
    partida_id BIGINT REFERENCES partidas(id_partida),
    jogador_id UUID REFERENCES jogadores(id_jogador),
    PRIMARY KEY (partida_id, jogador_id)
);

CREATE TABLE IF NOT EXISTS partida_jogadores_reservas (
    partida_id BIGINT REFERENCES partidas(id_partida),
    jogador_id UUID REFERENCES jogadores(id_jogador),
    PRIMARY KEY (partida_id, jogador_id)
);
