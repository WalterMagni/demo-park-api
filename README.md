DEMO PARK - Sistema de Gestão de Estacionamento
O DEMO PARK é uma API REST desenvolvida para gerenciar operações de estacionamento, como o cadastro de vagas, check-in e check-out de veículos, gerenciamento de usuários e controle de acessos. O projeto utiliza Spring Boot, garantindo uma estrutura robusta e escalável para a aplicação.

Funcionalidades Principais
Gestão de Vagas: Cadastro de novas vagas de estacionamento, busca por vagas disponíveis, e consultas por código de vaga.
Check-in e Check-out de Veículos: Gerenciamento da entrada e saída de veículos no estacionamento.
Controle de Usuários: Cadastro, atualização de senha, e gerenciamento de permissões para usuários com diferentes perfis de acesso (ADMIN e CLIENTE).
Controle de Acesso via Token JWT: Acesso aos recursos protegido por autenticação com tokens JWT (JSON Web Token).
Tecnologias Utilizadas
Java 11
Spring Boot 2.5.x
Spring Security para autenticação e autorização
Spring Data JPA para persistência de dados
Hibernate como provedor de ORM
Swagger/OpenAPI para documentação da API
H2 Database para ambiente de desenvolvimento
Maven para gerenciamento de dependências

Estrutura do Projeto
O projeto está organizado da seguinte maneira:
src
└── main
    ├── java
    │   └── com.example.demo.park
    │       ├── controller      # Controladores REST (APIs)
    │       ├── dto             # Data Transfer Objects (DTOs)
    │       ├── model           # Entidades do domínio
    │       ├── repository      # Repositórios JPA
    │       ├── security        # Configurações de segurança
    │       ├── service         # Lógica de negócios
    │       └── mapper          # Mapeamento de entidades e DTOs
    └── resources
        └── application.yml     # Configurações do projeto

Endpoints Principais
1. Vagas
POST /api/v1/vagas: Criação de novas vagas (ADMIN)
GET /api/v1/vagas/{codigo}: Busca de vagas por código (ADMIN)
2. Estacionamento
POST /api/v1/estacionamentos/check-in: Check-in de um veículo no estacionamento (ADMIN)
PUT /api/v1/estacionamentos/check-out/{recibo}: Check-out de um veículo (ADMIN)
GET /api/v1/estacionamentos/check-in/{recibo}: Busca de veículo pelo número do recibo (ADMIN ou CLIENTE)
GET /api/v1/estacionamentos/cpf/{cpf}: Consultar histórico de estacionamento por CPF (ADMIN)
GET /api/v1/estacionamentos: Consultar histórico do cliente logado (CLIENTE)
3. Usuários
POST /api/v1/usuarios: Criação de um novo usuário
GET /api/v1/usuarios: Listagem de todos os usuários (ADMIN)
GET /api/v1/usuarios/{id}: Buscar usuário por ID (ADMIN ou CLIENTE)
PATCH /api/v1/usuarios/{id}: Atualização de senha do usuário (ADMIN ou CLIENTE)
Configuração e Execução
Pré-requisitos
Java 11+
Maven 3.6+
IDE com suporte a Spring Boot (Eclipse, IntelliJ IDEA, etc.)
