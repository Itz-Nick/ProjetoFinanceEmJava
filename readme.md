
## `README.md`

```md
# 💰 NikoFinance

> Um mini sistema financeiro pessoal para organizar gastos, acompanhar receitas e visualizar minha evolução financeira.

O **NikoFinance** é um projeto pessoal desenvolvido para praticar desenvolvimento de software utilizando principalmente **Java, JavaScript e CSS**.

A ideia é transformar uma necessidade real — organizar meu próprio dinheiro — em uma aplicação completa com persistência de dados, dashboard e ferramentas de análise.

---

## 🎯 Objetivo

O NikoFinance permite registrar e acompanhar informações financeiras do dia a dia.

A aplicação tem como objetivo ajudar a responder perguntas como:

- Quanto dinheiro eu tenho?
- Quanto entrou este mês?
- Quanto eu gastei?
- Com o que estou gastando mais?
- Como meus gastos estão mudando ao longo do tempo?
- Quanto consegui economizar?
- Como uma possível aplicação/investimento poderia evoluir em uma simulação?

---

## 💰 Funcionalidades

### 📊 Dashboard

Visão geral da situação financeira:

- saldo atual;
- receitas;
- despesas;
- gastos do período;
- categorias com maior gasto;
- evolução financeira;
- indicadores e gráficos.

### 💸 Controle de despesas

Registrar:

- valor;
- descrição;
- categoria;
- data.

Exemplo:

```text
Alimentação
R$ 35,90
20/08/2026
````

### 💵 Controle de receitas

Registrar entradas de dinheiro, como:

* salário;
* mesada;
* trabalhos;
* outros recebimentos.

### 🏷️ Categorias

Organizar os gastos por categorias.

Categorias iniciais:

* 🍔 Alimentação
* 🚗 Transporte
* 📚 Educação
* 🎮 Lazer
* 📱 Assinaturas
* 🏠 Casa
* ❤️ Saúde
* 📦 Outros

As categorias poderão ser expandidas posteriormente.

---

## 📈 Análise financeira

O sistema deve comparar os dados registrados para mostrar tendências.

Exemplos:

```text
Gastos em agosto
████████████████  R$ 650

Gastos em julho
██████████       R$ 420
```

Também poderão existir análises como:

* gastos por categoria;
* comparação mensal;
* receitas x despesas;
* percentual de economia;
* evolução do saldo.

---

## 📅 Histórico

Todas as transações ficam armazenadas para consulta posterior.

Será possível:

* pesquisar;
* filtrar;
* ordenar;
* visualizar por período;
* editar;
* excluir.

---

## 📊 Investimentos

O projeto terá uma área de **simulação de investimentos**.

O objetivo é permitir testar cenários hipotéticos, como:

* valor inicial;
* aportes;
* período;
* taxa estimada;
* crescimento projetado;
* resultado final.

Os cálculos serão apresentados como **simulações**, não como recomendações financeiras.

---

## 🗄️ Persistência

Os dados serão armazenados localmente utilizando:

**SQLite**

Isso permite manter:

* receitas;
* despesas;
* categorias;
* histórico;
* dados necessários para os cálculos.

Dados pessoais e arquivos locais do banco não devem ser enviados para o repositório público.

---

## 🛠️ Tecnologias

### Backend

* ☕ **Java**
* 🌱 **Spring Boot**
* 📦 **Maven**

### Banco de dados

* 🗄️ **SQLite**

### Frontend

* 🌐 **HTML**
* ⚡ **JavaScript**
* 🎨 **CSS**

A ideia é manter o frontend propositalmente simples para que o foco também esteja no aprendizado de Java e desenvolvimento de backend.

---

## 🧱 Arquitetura

Arquitetura inicial:

```text
Frontend
   │
   ▼
REST API
   │
   ▼
Controllers
   │
   ▼
Services
   │
   ▼
Repositories
   │
   ▼
SQLite
```

Estrutura esperada:

```text
src/
├── main/
│   ├── java/
│   │   └── ...
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── model/
│   │       ├── dto/
│   │       └── config/
│   │
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   └── js/
│       └── application.properties/
│
└── test/
```

A estrutura poderá evoluir conforme o projeto crescer.

---

## 🚀 Roadmap

### 🟢 Fundação

* [ ] Criar projeto Java + Spring Boot
* [ ] Configurar Maven
* [ ] Configurar SQLite
* [ ] Criar estrutura inicial do backend
* [ ] Criar frontend inicial
* [ ] Conectar frontend e backend

### 💸 Finanças

* [ ] Criar receitas
* [ ] Criar despesas
* [ ] Editar transações
* [ ] Excluir transações
* [ ] Categorias
* [ ] Histórico
* [ ] Filtros por período

### 📊 Dashboard

* [ ] Saldo atual
* [ ] Total de receitas
* [ ] Total de despesas
* [ ] Gastos por categoria
* [ ] Comparação mensal
* [ ] Gráfico de evolução
* [ ] Percentual de economia

### 📈 Investimentos

* [ ] Calculadora de juros compostos
* [ ] Aportes periódicos
* [ ] Simulação por período
* [ ] Comparação de cenários
* [ ] Gráficos de projeção

### 🎨 UX

* [ ] Dashboard responsivo
* [ ] Feedback visual
* [ ] Validação de formulários
* [ ] Estados vazios
* [ ] Tratamento de erros
* [ ] Melhorias visuais

### 🔐 Futuro

* [ ] Exportação de dados
* [ ] Backup
* [ ] Relatórios
* [ ] Mais opções de análise

---

## 🧮 Regras financeiras

O sistema utilizará valores monetários com precisão adequada, evitando `float`/`double` para cálculos financeiros.

Regra básica:

```text
Saldo = Receitas - Despesas
```

Os valores derivados devem ser calculados a partir das transações armazenadas.

---

## 🔒 Privacidade

O NikoFinance é um projeto pessoal.

Não devem ser armazenados no repositório:

* dados financeiros pessoais reais;
* senhas;
* tokens;
* credenciais;
* banco SQLite com informações pessoais;
* dados sensíveis.

Utilizar dados fictícios nos testes e exemplos públicos.

---

## 🚧 Status

**🟡 Desenvolvimento inicial**

O projeto está sendo construído gradualmente, começando pela fundação do backend, banco de dados e controle básico de receitas e despesas.

---

## 🎓 Objetivo de aprendizado

Além de ser uma ferramenta pessoal, o NikoFinance faz parte do meu portfólio e tem como objetivo fortalecer principalmente meus conhecimentos em:

* Java;
* desenvolvimento backend;
* APIs REST;
* banco de dados;
* SQL;
* JavaScript;
* CSS;
* arquitetura de software;
* persistência de dados;
* cálculos financeiros.

---

## 👨‍💻 Autor

**Nicolas**

Desenvolvedor em formação com interesse em:

* 💻 Software Development
* 🎮 Game Development
* ☕ Java
* 🌐 Web Development
* 🗄️ Databases

---

## 📌 Observação

O NikoFinance é uma ferramenta pessoal de organização e **simulação financeira**.

Os resultados de simuladores de investimento não constituem recomendação financeira.

```
