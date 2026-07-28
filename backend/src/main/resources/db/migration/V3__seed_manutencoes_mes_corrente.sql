-- Garante que exista ao menos uma manutenção com data_inicio no mês/ano
-- corrente, para que o card de projeção financeira do dashboard não
-- apareça zerado por padrão em um ambiente recém-provisionado (V1 usa
-- datas fixas de 2024, que nunca coincidem com o mês corrente).
INSERT INTO manutencoes (veiculo_id, data_inicio, data_finalizacao, tipo_servico, custo_estimado, status) VALUES
(4, date_trunc('month', CURRENT_DATE)::date + 5, NULL, 'Revisão Geral', 980.00, 'PENDENTE');
