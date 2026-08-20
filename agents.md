# AGENTS.md

## 🧠 Contexto do projeto

Este projeto é um mini sistema financeiro pessoal desenvolvido para controle de:

- saldo;
- receitas;
- despesas;
- categorias de gastos;
- histórico financeiro;
- comparação de gastos;
- investimentos e simulações;
- evolução financeira ao longo do tempo.

O objetivo é criar um projeto pequeno, funcional e organizado que também sirva como projeto de portfólio, demonstrando conhecimento em:

- Java;
- JavaScript;
- CSS;
- desenvolvimento de APIs;
- banco de dados;
- organização de software.

---

# 🎯 Objetivo do agente

Você é um agente de desenvolvimento trabalhando neste projeto através do OpenCode.

Sua função é:

- analisar o código existente;
- implementar funcionalidades;
- corrigir bugs;
- melhorar a organização;
- preservar funcionalidades existentes;
- manter o projeto simples e sustentável.

O projeto deve continuar sendo um **mini sistema financeiro**, não um banco digital completo.

---

# ⚠️ Regra mais importante

O código existente é a fonte definitiva da realidade do projeto.

O README descreve objetivos e funcionalidades planejadas, mas não prova que determinada funcionalidade já existe.

Antes de modificar qualquer sistema:

1. Leia os arquivos relevantes.
2. Confirme como o sistema atualmente funciona.
3. Identifique dependências.
4. Só então implemente.

Nunca invente:

- arquivos;
- classes;
- endpoints;
- tabelas;
- serviços;
- funcionalidades.

---

# 🛠️ Stack

Tecnologias principais:

- **Java** — backend e regras de negócio;
- **Spring Boot** — API/backend web;
- **Maven** — gerenciamento e build;
- **SQLite** — persistência local;
- **JavaScript** — frontend e interação;
- **HTML** — estrutura da interface;
- **CSS** — estilização.

Preferir JavaScript puro no frontend.

Não introduzir React, Vue, Angular ou outro framework sem necessidade explícita.

---

# 🗄️ Banco de dados

O sistema precisa guardar os dados financeiros.

A persistência inicial deve utilizar:

**SQLite**

O banco deve armazenar informações relevantes como:

- receitas;
- despesas;
- categorias;
- datas;
- descrições;
- valores;
- investimentos/simulações quando implementados.

Evitar armazenar informações duplicadas que possam ser calculadas.

Valores derivados devem ser calculados quando possível.

---

# 💰 Valores monetários

Nunca utilizar `double` ou `float` para representar dinheiro no domínio.

Preferir:

```java
BigDecimal
````

ou uma representação equivalente adequada para valores monetários.

Definir uma convenção consistente para:

* valores;
* arredondamento;
* cálculos;
* percentuais.

---

# 🧩 Arquitetura

Priorizar separação clara entre:

```text
Controller
↓
Service
↓
Repository
↓
Database
```

Estrutura sugerida:

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
│       │   ├── js/
│       │   └── ...
│       └── application.properties
│
└── test/
```

Adaptar à arquitetura real do projeto.

Não criar pastas simplesmente para seguir esse exemplo.

---

# 🧱 Regras de desenvolvimento

Priorizar:

* código legível;
* baixo acoplamento;
* classes pequenas;
* responsabilidades claras;
* reutilização;
* tipagem;
* tratamento de erros;
* facilidade de manutenção.

Evitar:

* classes gigantes;
* métodos gigantes;
* lógica de negócio dentro dos controllers;
* SQL espalhado pelo projeto;
* código duplicado;
* `any` no JavaScript sem necessidade;
* `try/catch` genérico escondendo erros;
* soluções complexas para problemas simples.

---

# 🔒 Dados financeiros

Este projeto pode conter informações financeiras pessoais.

Tratar os dados como sensíveis.

Nunca:

* colocar valores pessoais reais no código;
* criar senhas padrão;
* deixar credenciais no Git;
* colocar banco de dados pessoal no repositório;
* colocar tokens/API keys no projeto;
* incluir dados financeiros reais em testes públicos.

Utilizar dados fictícios nos testes e exemplos.

O `.gitignore` deve impedir que arquivos locais de banco ou dados pessoais sejam enviados para o GitHub quando apropriado.

---

# 💳 Escopo financeiro

O sistema deve ser tratado como:

**ferramenta pessoal de organização financeira e simulação.**

Não é:

* banco;
* corretora;
* sistema de pagamentos;
* consultor financeiro;
* plataforma de investimento real.

Qualquer calculadora de investimentos deve apresentar resultados como **simulação**, sem prometer retornos.

Não criar recomendações automáticas de compra/venda de ativos nesta etapa.

---

# 📊 Funcionalidades principais

O projeto deve evoluir gradualmente.

Prioridade inicial:

1. cadastro de receitas;
2. cadastro de despesas;
3. categorias;
4. saldo;
5. histórico;
6. dashboard;
7. comparação de gastos;
8. filtros por período;
9. persistência no SQLite;
10. calculadora/simulador de investimentos.

Não implementar tudo de uma vez.

---

# 📅 Datas

Transações devem possuir uma data adequada.

Preferir tipos de data do Java, como:

```java
LocalDate
```

ou:

```java
LocalDateTime
```

quando horário realmente for necessário.

Não armazenar datas importantes apenas como strings quando existir uma opção tipada apropriada.

---

# 🧮 Regras financeiras

Receitas aumentam o saldo.

Despesas diminuem o saldo.

O saldo deve ser calculado com base no histórico sempre que possível.

Não duplicar saldo em múltiplos locais sem necessidade.

Exemplo:

```text
Saldo =
receitas - despesas
```

ou equivalente conforme a arquitetura escolhida.

---

# 📈 Dashboard

O dashboard deve priorizar informações úteis:

* saldo atual;
* total de receitas;
* total de despesas;
* gastos do período;
* maiores categorias;
* evolução mensal;
* percentual de economia;
* investimentos/simulações quando disponíveis.

Evitar excesso de gráficos.

Cada gráfico precisa ter utilidade.

---

# 🏷️ Categorias

Inicialmente permitir categorias como:

* Alimentação;
* Transporte;
* Educação;
* Lazer;
* Assinaturas;
* Casa;
* Saúde;
* Outros.

O sistema deve permitir adicionar categorias futuramente.

Evitar fixar toda a lógica em uma enumeração se isso impedir categorias personalizadas.

---

# 🔄 API

O backend deverá expor endpoints claros.

Exemplo conceitual:

```text
GET    /api/transactions
POST   /api/transactions
PUT    /api/transactions/{id}
DELETE /api/transactions/{id}

GET    /api/categories
POST   /api/categories

GET    /api/dashboard
```

Os endpoints finais devem seguir a arquitetura real.

Não criar endpoints sem necessidade.

---

# 🧪 Testes

Sempre que possível:

* testar regras financeiras;
* testar services;
* testar repositories;
* testar endpoints importantes.

Priorizar testes para:

* soma de receitas;
* soma de despesas;
* saldo;
* filtros;
* criação de transações;
* atualização;
* exclusão;
* cálculos de investimento.

---

# 🛠️ Tratamento de erros

A API deve retornar respostas coerentes.

Exemplos:

```text
400 Bad Request
```

para dados inválidos.

```text
404 Not Found
```

quando uma entidade não existir.

```text
500
```

somente para erros inesperados.

Não retornar stack traces ao frontend.

---

# 🎨 Frontend

O frontend deve ser:

* simples;
* responsivo;
* moderno;
* legível;
* rápido.

Preferir:

* cards;
* tabelas;
* gráficos simples;
* filtros;
* modais quando apropriado.

Não transformar cada pequena funcionalidade em uma tela diferente.

---

# 📦 Dependências

Não adicionar dependências sem necessidade.

Antes de instalar algo:

1. verificar se já existe solução no projeto;
2. verificar se o Spring Boot/JDK resolve;
3. justificar dependências adicionais quando forem importantes.

---

# 🔄 Antes de implementar

Para tarefas complexas:

1. leia README.md;
2. leia AGENTS.md;
3. examine a estrutura;
4. identifique sistemas existentes;
5. planeje a alteração;
6. implemente;
7. teste;
8. compile.

Não refatorar tudo para implementar uma funcionalidade pequena.

---

# 🐛 Correção de bugs

Sempre:

1. identificar a causa;
2. corrigir a causa;
3. testar;
4. verificar efeitos colaterais.

Evitar gambiarras.

---

# 🧪 Validação

Sempre que possível executar:

```bash
mvn test
```

e:

```bash
mvn package
```

ou os comandos equivalentes definidos pelo projeto.

Caso o frontend possua comandos próprios, executá-los também.

---

# 📚 README

O README deve refletir o estado real do projeto.

Atualizar quando houver mudança significativa.

Não atualizar para cada pequena alteração interna.

Nunca declarar como implementada uma funcionalidade que ainda não existe.

---

# 🚫 Não fazer

Não:

* criar banco financeiro complexo;
* implementar login antes de ser necessário;
* implementar multiusuário sem solicitação;
* integrar bancos reais sem solicitação;
* integrar corretoras;
* movimentar dinheiro;
* criar sistema de pagamentos;
* inventar dados financeiros;
* instalar dezenas de dependências;
* transformar o projeto em uma arquitetura empresarial desnecessária.

---

# 🎯 Filosofia do projeto

Prioridades:

1. Correção dos cálculos
2. Persistência confiável
3. Simplicidade
4. Organização
5. UX
6. Visual
7. Performance

O projeto deve ser pequeno o suficiente para ser compreendido por uma pessoa, mas organizado o suficiente para parecer um projeto profissional.

---

# ✅ Regra final

Antes de implementar uma funcionalidade grande, explique brevemente:

* onde ela será implementada;
* quais sistemas serão afetados;
* quais arquivos provavelmente precisarão mudar.

Depois implemente.

Preserve sempre o que já funciona.

````