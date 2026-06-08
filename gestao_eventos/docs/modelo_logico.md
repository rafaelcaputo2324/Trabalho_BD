```mermaid
erDiagram

    PARTICIPANTES {
        INT id PK
        VARCHAR nome
        VARCHAR email UK
        VARCHAR telefone
    }

    EVENTOS {
        INT id PK
        VARCHAR titulo
        VARCHAR descricao
        DATE data_evento
        VARCHAR local
        INT capacidade_maxima
    }

    PALESTRANTES {
        INT id PK
        VARCHAR nome
        VARCHAR email UK
        VARCHAR especialidade
    }

    INSCRICOES {
        INT id PK
        INT participante_id FK
        INT evento_id FK
        TIMESTAMP data_inscricao
        VARCHAR status
    }

    ATIVIDADES {
        INT id PK
        INT evento_id FK
        INT palestrante_id FK
        VARCHAR titulo
        TIMESTAMP data_hora
        INT duracao_minutos
    }

    CERTIFICADOS {
        INT id PK
        INT inscricao_id FK
        VARCHAR codigo_autenticacao
        TIMESTAMP data_emissao
    }

    PARTICIPANTES ||--o{ INSCRICOES : realiza
    EVENTOS ||--o{ INSCRICOES : recebe

    EVENTOS ||--o{ ATIVIDADES : possui
    PALESTRANTES ||--o{ ATIVIDADES : ministra

    INSCRICOES ||--|| CERTIFICADOS : gera
```
