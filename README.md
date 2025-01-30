# helpdesk-system (em desenvolvimento)

# Visão Geral

O projeto possui microsserviços para simular o gerenciamento de informações dos usuários e ordens de serviços.

# Tecnologias Utilizadas

- Spring Boot: Framework Java para criação de aplicações web e microsserviços.
- Spring Cloud: Conjunto de ferramentas para construir sistemas distribuídos.
- Spring Security: Framework de autenticação e autorização.
- JWT (JSON Web Tokens): Para autenticação e autorização segura.
- RabbitMQ: Sistema de mensageria para comunicação assíncrona.
- MongoDB Atlas: Serviço de banco de dados NoSQL gerenciado.
- Amazon RDS: Serviço de banco de dados relacional gerenciado.
- Docker: Plataforma para criação e gerenciamento de contêineres.
- GitHub: Plataforma de hospedagem de código e controle de versão.

# Estrutura do Projeto

### Componentes Principais

- API Gateway: Gerencia todas as requisições para os microsserviços.
- Config Server: Armazena e distribui configurações centralizadas.
- Service Discovery: Localiza e registra instâncias de serviço dinamicamente.
- Auth Service API: Gerencia tokens de acesso e autenticação de usuários.
- Commons Lib: Biblioteca compartilhada entre os microsserviços.
- Email Service: Envia e-mails utilizando RabbitMQ para gerenciamento de filas.
- User Service API: Gerencia informações de usuários.
- Order Service API: Gerencia ordens de serviço.

### Funcionalidades

- Autenticação e Autorização: Utilizando Spring Security e JWT.
- Balanceamento de Carga: Distribuição automática de solicitações entre serviços.
- Configurações Centralizadas: Atualização dinâmica de configurações em tempo de execução.
- Mensageria: Comunicação assíncrona com RabbitMQ.
- Integração Contínua: Utilizando GitHub e Docker para versionamento e implantação.

# Instalação

### Pré-requisitos

- Java 17 ou superior
- Docker
- Maven
- Git

### Iniciando o Projeto com Gradle

1. Clone este repositório.
2. Navegue até o diretório do projeto.
3. Execute `./gradlew build` para compilar o projeto.
4. Execute `./gradlew run` para iniciar o servidor.
