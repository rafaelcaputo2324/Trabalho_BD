# Resumo do Código - EventFlow

Este documento descreve a estrutura de classes do projeto **EventFlow (Sistema de Gestão de Eventos)** de forma técnica e resumida para auxiliar o entendimento do sistema e a criação do roteiro de apresentação.

## Camadas do Sistema (gestaoeventos)

### 1. Modelo (gestaoeventos.model)
Contém os objetos que representam os dados das tabelas:
- **Participante**: Nome, e-mail (único) e telefone.
- **Evento**: Título, descrição, data, local e capacidade máxima (limite de vagas).
- **Inscricao**: Associação N:N entre Participante e Evento. Controla o status (Confirmada, Pendente, Cancelada).
- **Certificado**: Ligado a uma Inscrição Confirmada, possui um código de autenticação único.
- **Palestrante** e **Atividade**: Suporte para cadastrar palestrantes e cronograma de palestras de um evento.

### 2. Acesso a Dados (gestaoeventos.dao)
Realiza a comunicação JDBC com o banco de dados embutido H2:
- **DatabaseConnection**: Inicializa o banco lendo o script 'db/schema.sql' se as tabelas ainda não existirem.
- **DAOs**: Contêm comandos SQL (CRUD) executados via PreparedStatement para segurança contra SQL Injection.

### 3. Camada de Serviço (gestaoeventos.service)
Gerencia as validações das Regras de Negócio (RN):
- **RN1 (Capacidade Máxima)**: Impede confirmação de inscrições se o evento já estiver lotado.
- **RN2 (Bloqueio de Certificado)**: Só permite a emissão de certificados se a inscrição estiver no status 'Confirmada'.
- **RN3 (Inscrição Única)**: Evita que um participante se registre duas vezes no mesmo evento.

### 4. Interface Gráfica (gestaoeventos.ui)
Interface estilizada com a biblioteca FlatLaf (visual limpo):
- **MainFrame**: Janela principal com menu lateral CardLayout e atualização automática de dados.
- **DashboardPanel**: Estatísticas gerais e o Relatório de Ocupação de vagas por evento.
- **ParticipantesPanel** e **EventosPanel**: Telas CRUD completas com validações de dados obrigatórios.
- **InscricoesPanel**: Registro de matrículas, alteração de status e acionamento para geração do certificado.
- **CertificadosPanel**: Consulta todos os certificados emitidos e exibe um diploma estilizado.

## 🛠️ Como Compilar e Executar

Abra o terminal de comandos (Prompt de Comando ou PowerShell) na pasta raiz do projeto (`gestao_eventos`) e execute:

### 1. Criar pasta para os arquivos binários compilados
```cmd
mkdir bin
```

### 2. Compilar o Projeto
```cmd
javac -encoding UTF-8 -cp "lib/h2.jar;lib/flatlaf.jar" -d bin -sourcepath src src/gestaoeventos/Main.java
```

### 3. Executar o Sistema
```cmd
java -cp "bin;lib/h2.jar;lib/flatlaf.jar" gestaoeventos.Main
```
