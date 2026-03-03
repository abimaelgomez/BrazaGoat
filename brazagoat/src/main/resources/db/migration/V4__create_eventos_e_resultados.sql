CREATE TABLE IF NOT EXISTS gols (
    id BIGSERIAL PRIMARY KEY,
    jogador_id UUID REFERENCES jogadores(id_jogador),
    partida_id BIGINT REFERENCES partidas(id_partida),
    assistencia_id BIGINT,
    status_gol VARCHAR(30),
    tipo_de_gol VARCHAR(30),
    numero_do_gol INTEGER,
    hora_do_gol TIME,
    data_do_gol DATE,
    ponto_por_gol INTEGER DEFAULT 0,
    ponto_por_gol_contra INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS assist (
    id BIGSERIAL PRIMARY KEY,
    jogador_id UUID REFERENCES jogadores(id_jogador),
    partida_id BIGINT REFERENCES partidas(id_partida)
);

ALTER TABLE gols ADD CONSTRAINT fk_gol_assistencia
    FOREIGN KEY (assistencia_id) REFERENCES assist(id);

CREATE TABLE IF NOT EXISTS substituicoes (
    id BIGSERIAL PRIMARY KEY,
    jogador_saiu UUID REFERENCES jogadores(id_jogador),
    jogador_entrou UUID REFERENCES jogadores(id_jogador),
    partida_id BIGINT REFERENCES partidas(id_partida)
);

CREATE TABLE IF NOT EXISTS resultados (
    id BIGSERIAL PRIMARY KEY,
    partida_id BIGINT UNIQUE REFERENCES partidas(id_partida),
    placar_equipe_a INTEGER NOT NULL,
    placar_equipe_b INTEGER NOT NULL,
    vencedor VARCHAR(20) NOT NULL,
    data_resultado TIMESTAMP NOT NULL
);
