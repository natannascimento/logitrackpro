-- Extensões sobre o schema da carga inicial para suportar o CRUD de Viagens
-- e as regras de negócio: data_chegada >= data_saida e km_percorrida > 0.

ALTER TABLE viagens
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT now();

ALTER TABLE viagens
    ADD CONSTRAINT chk_viagens_km_percorrida_positiva CHECK (km_percorrida IS NULL OR km_percorrida > 0),
    ADD CONSTRAINT chk_viagens_datas_coerentes CHECK (data_chegada IS NULL OR data_chegada >= data_saida);

CREATE INDEX idx_viagens_veiculo_id ON viagens (veiculo_id);
CREATE INDEX idx_manutencoes_veiculo_id ON manutencoes (veiculo_id);
