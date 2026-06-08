# Modelo Físico

O modelo físico do sistema de Gestão de Eventos foi implementado utilizando o banco de dados H2. A estrutura contempla tabelas, chaves primárias, chaves estrangeiras e restrições de integridade para garantir a consistência dos dados.

## Tabela PARTICIPANTES

* id (PK)
* nome (NOT NULL)
* email (NOT NULL, UNIQUE)
* telefone

## Tabela EVENTOS

* id (PK)
* titulo (NOT NULL)
* descricao
* data_evento (NOT NULL)
* local (NOT NULL)
* capacidade_maxima (NOT NULL, CHECK capacidade_maxima > 0)

## Tabela PALESTRANTES

* id (PK)
* nome (NOT NULL)
* email (NOT NULL, UNIQUE)
* especialidade

## Tabela INSCRICOES

* id (PK)
* participante_id (FK → PARTICIPANTES.id)
* evento_id (FK → EVENTOS.id)
* data_inscricao
* status (CHECK status IN ('Pendente', 'Confirmada', 'Cancelada'))

Restrições:

* UNIQUE(participante_id, evento_id)
* ON DELETE CASCADE para as chaves estrangeiras

## Tabela ATIVIDADES

* id (PK)
* evento_id (FK → EVENTOS.id)
* palestrante_id (FK → PALESTRANTES.id)
* titulo (NOT NULL)
* data_hora (NOT NULL)
* duracao_minutos (NOT NULL, CHECK duracao_minutos > 0)

Restrições:

* ON DELETE CASCADE para evento_id
* ON DELETE SET NULL para palestrante_id

## Tabela CERTIFICADOS

* id (PK)
* inscricao_id (FK → INSCRICOES.id)
* codigo_autenticacao (NOT NULL, UNIQUE)
* data_emissao

Restrições:

* UNIQUE(inscricao_id)
* ON DELETE CASCADE para inscricao_id

## Restrições de Integridade Utilizadas

### Chaves Primárias (PK)

* participantes(id)
* eventos(id)
* palestrantes(id)
* inscricoes(id)
* atividades(id)
* certificados(id)

### Chaves Estrangeiras (FK)

* inscricoes.participante_id → participantes.id
* inscricoes.evento_id → eventos.id
* atividades.evento_id → eventos.id
* atividades.palestrante_id → palestrantes.id
* certificados.inscricao_id → inscricoes.id

### Restrições NOT NULL

Aplicadas aos atributos obrigatórios de cada entidade.

### Restrições UNIQUE

* participantes.email
* palestrantes.email
* inscricoes(participante_id, evento_id)
* certificados.inscricao_id
* certificados.codigo_autenticacao

### Restrições CHECK

* capacidade_maxima > 0
* duracao_minutos > 0
* status IN ('Pendente', 'Confirmada', 'Cancelada')
