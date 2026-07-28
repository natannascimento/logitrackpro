CREATE TABLE tokens_revogados (
    jti VARCHAR(36) PRIMARY KEY,
    expira_em TIMESTAMP NOT NULL
);
