CREATE TABLE IF NOT EXISTS sorteios (
    id_sorteio UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    data_sorteio TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sorteio_equipe_a (
    sorteio_id UUID REFERENCES sorteios(id_sorteio),
    jogador_id UUID REFERENCES jogadores(id_jogador),
    PRIMARY KEY (sorteio_id, jogador_id)
);

CREATE TABLE IF NOT EXISTS sorteio_equipe_b (
    sorteio_id UUID REFERENCES sorteios(id_sorteio),
    jogador_id UUID REFERENCES jogadores(id_jogador),
    PRIMARY KEY (sorteio_id, jogador_id)
);

CREATE TABLE IF NOT EXISTS sorteio_reservas (
    sorteio_id UUID REFERENCES sorteios(id_sorteio),
    jogador_id UUID REFERENCES jogadores(id_jogador),
    PRIMARY KEY (sorteio_id, jogador_id)
);
