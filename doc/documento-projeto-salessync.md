# Documento de Projeto - SalesSync

## Apresentação (introdução ao sistema)

O SalesSync é um sistema de gestão de recursos empresariais (ERP) especializado em análise financeira e geração de relatórios detalhados, desenvolvido para atender às necessidades de pequenas e médias empresas. Projetado como uma aplicação desktop utilizando Java e MySQL, o sistema tem como foco principal a emissão de relatórios financeiros precisos e detalhados, permitindo uma visão clara do balancete empresarial em diferentes períodos (diário, semanal, mensal e anual).

O diferencial do SalesSync está na sua capacidade avançada de processamento de dados financeiros, gerando relatórios detalhados em formato CSV que incluem métricas essenciais como lucro bruto, impostos estimados, lucro líquido, total de vendas e despesas. Essas informações permitem ao gestor uma análise aprofundada do desempenho da empresa, facilitando a tomada de decisões estratégicas baseadas em dados concretos.

## Descrição do projeto (descrição básica do sistema planejado)

O SalesSync é um sistema desktop para gestão de recursos empresariais com foco principal na geração de relatórios financeiros detalhados. O sistema será desenvolvido em Java utilizando Swing para a interface gráfica com a biblioteca FlatLaf para melhorar a aparência e usabilidade, proporcionando um visual moderno e consistente com o sistema operacional. O MySQL será utilizado como sistema gerenciador de banco de dados, seguindo o padrão de arquitetura MVC (Model-View-Controller) e utilizando Hibernate para mapeamento objeto-relacional.

O sistema contará com módulos para gerenciamento de clientes, controle de vendas, registro de despesas e emissão de ordens de serviço, todos integrados ao módulo central de geração de relatórios financeiros. Os relatórios serão exportados em formato CSV, contendo informações detalhadas sobre vendas, despesas, lucros, impostos e análises de desempenho. Diferentes níveis de acesso aos relatórios serão implementados com base no perfil do usuário, permitindo que proprietários tenham acesso a relatórios mais abrangentes, enquanto funcionários possam consultar apenas relatórios diários.

## Descrição dos usuários (quem serão os usuários do sistema)

O sistema SalesSync foi projetado para atender três perfis distintos de usuários:

1. **Administrador (ADMIN)**:
   - Responsável pela configuração e manutenção do sistema.
   - Tem acesso aos logs do sistema para fins de auditoria e resolução de problemas.
   - Pode criar e gerenciar perfis de usuários.
   - Não participa diretamente das operações comerciais da empresa.

2. **Proprietário (OWNER)**:
   - Proprietário ou gestor da empresa.
   - Possui acesso completo às funcionalidades operacionais do sistema.
   - Pode gerar relatórios detalhados diários, semanais, mensais e anuais.
   - Tem acesso a métricas avançadas como lucro bruto, impostos estimados e lucro líquido.
   - Tem permissão para excluir registros do sistema.
   - Não tem acesso às funções administrativas do sistema.

3. **Funcionário (EMPLOYEE)**:
   - Colaboradores que realizam as operações diárias da empresa.
   - Pode inserir, visualizar e editar registros de vendas.
   - Tem permissão para cadastrar e editar informações de clientes.
   - Pode gerar ordens de serviço.
   - Tem acesso limitado à geração de relatórios (apenas relatórios diários).

## Necessidades observadas e regras de negócio

### Necessidades observadas:

1. Geração de relatórios financeiros detalhados em formato CSV para análise de desempenho em diferentes períodos (diário, semanal, mensal e anual).
2. Cálculo preciso de métricas financeiras como lucro bruto, impostos estimados, lucro líquido, total de vendas e despesas.
3. Identificação de produtos com melhor desempenho de vendas através de relatórios analíticos.
4. Controle eficiente de fluxo de caixa com registro detalhado de entradas e saídas.
5. Centralização das informações de clientes, vendas e despesas em um único sistema.
6. Necessidade de emissão de ordens de serviço para formalização dos trabalhos realizados.
7. Segurança no acesso às informações com diferentes níveis de permissão para geração de relatórios.
8. Interface gráfica moderna e amigável utilizando FlatLaf para melhor experiência do usuário.

### Regras de negócio:

1. **Cadastro de Clientes:**
   - Cada cliente deve ter um código único no sistema.
   - É obrigatório o cadastro de nome/razão social, CPF/CNPJ e pelo menos um contato (telefone ou e-mail).
   - Não é permitido o cadastro de clientes com CPF/CNPJ duplicados.

2. **Registro de Vendas:**
   - Toda venda deve estar associada a um cliente cadastrado.
   - É obrigatório o registro da data da venda, data de pagamento, nome do cliente, forma de pagamento, status da venda (concluída/pendente/cancelada), data de conclusão, valor total e nome do vendedor.
   - Uma venda pode conter diversos itens com suas respectivas quantidades e valores unitários.
   - O sistema deve calcular automaticamente o valor total da venda com base nos itens adicionados.

3. **Registro de Despesas:**
   - Toda despesa deve ser categorizada (ex: aluguel, salários, insumos, etc.).
   - É obrigatório o registro da data, valor e descrição da despesa.
   - Despesas podem ser recorrentes ou pontuais.

4. **Emissão de Ordens de Serviço:**
   - Toda ordem de serviço deve estar associada a um cliente.
   - É obrigatório o registro da descrição do serviço, data de solicitação, previsão de entrega e valor.
   - A ordem de serviço deve ter um status (pendente, em andamento, concluído, cancelado).

5. **Geração de Relatórios:**
   - Relatórios diários são acessíveis a todos os perfis de usuário (ADMIN, OWNER e EMPLOYEE).
   - Relatórios semanais, mensais e anuais são restritos aos perfis OWNER e ADMIN.
   - Os relatórios devem ser exportados em formato CSV.
   - Os relatórios devem apresentar o balanço entre vendas e despesas no período selecionado.
   - Os relatórios devem calcular e apresentar o lucro bruto, impostos estimados, lucro líquido, total de vendas e total de despesas.
   - Os relatórios de vendas devem incluir: data da venda, data de pagamento, nome do cliente, forma de pagamento, status da venda, data de conclusão, valor total e nome do vendedor.
   - Os relatórios devem identificar e incluir o produto mais vendido no período.

6. **Controle de Acesso:**
   - O acesso ao sistema é permitido apenas mediante autenticação válida.
   - Cada usuário deve estar associado a um dos perfis definidos (ADMIN, OWNER ou EMPLOYEE).
   - As permissões de acesso às funcionalidades do sistema são definidas pelo perfil do usuário.

## Requisitos funcionais

### RF01 - Autenticação de Usuários
- O sistema deve permitir o login de usuários cadastrados através de usuário e senha.
- O sistema deve validar o perfil do usuário e aplicar as restrições de acesso correspondentes.
- O sistema deve permitir a alteração de senha pelo próprio usuário.

### RF02 - Gerenciamento de Usuários (ADMIN)
- O sistema deve permitir a criação de novos usuários com definição de perfil.
- O sistema deve permitir a edição e desativação de usuários existentes.
- O sistema deve manter um registro de logs de acesso e ações realizadas no sistema.

### RF03 - Cadastro de Clientes
- O sistema deve permitir o cadastro de clientes com os seguintes dados: código, nome/razão social, CPF/CNPJ, endereço, telefone, e-mail, data de cadastro e observações.
- O sistema deve permitir a edição dos dados cadastrais dos clientes.
- O sistema deve permitir a consulta de clientes por código, nome ou CPF/CNPJ.
- O sistema deve permitir a visualização do histórico de compras e serviços do cliente.

### RF04 - Registro de Vendas
- O sistema deve permitir o registro de vendas com os seguintes dados: código, cliente, data da venda, data de pagamento, itens vendidos, quantidades, valores unitários, valor total, forma de pagamento, status da venda (concluída/pendente/cancelada), data de conclusão, nome do vendedor e observações.
- O sistema deve permitir a edição de vendas ainda não finalizadas.
- O sistema deve permitir a consulta de vendas por código, cliente, período ou status.
- O sistema deve permitir a exclusão de vendas (apenas para perfil OWNER).

### RF05 - Registro de Despesas
- O sistema deve permitir o registro de despesas com os seguintes dados: código, descrição, categoria, data, valor, recorrência e observações.
- O sistema deve permitir a edição de despesas registradas.
- O sistema deve permitir a consulta de despesas por código, categoria ou período.
- O sistema deve permitir a exclusão de despesas (apenas para perfil OWNER).

### RF06 - Emissão de Ordens de Serviço
- O sistema deve permitir a emissão de ordens de serviço com os seguintes dados: código, cliente, descrição do serviço, data de solicitação, previsão de entrega, valor e status.
- O sistema deve permitir a edição e atualização do status das ordens de serviço.
- O sistema deve permitir a consulta de ordens de serviço por código, cliente, status ou período.
- O sistema deve permitir a impressão das ordens de serviço.

### RF07 - Geração de Relatórios Financeiros
- O sistema deve permitir a geração de relatório balancete diário com detalhamento de vendas e despesas, acessível para todos os perfis de usuário.
- O sistema deve permitir a geração de relatório balancete semanal com resumo de vendas e despesas por categoria (apenas para perfis OWNER e ADMIN).
- O sistema deve permitir a geração de relatório balancete mensal com resumo de vendas e despesas por categoria (apenas para perfis OWNER e ADMIN).
- O sistema deve permitir a geração de relatório balancete anual com análise comparativa de períodos (apenas para perfis OWNER e ADMIN).
- O sistema deve calcular automaticamente o lucro bruto, impostos estimados, lucro líquido, total de vendas e total de despesas para cada relatório gerado.
- O sistema deve identificar e incluir no relatório o produto mais vendido no período analisado.
- O sistema deve incluir nos relatórios de vendas: data da venda, data de pagamento, nome do cliente, forma de pagamento, status da venda, data de conclusão, valor total e nome do vendedor.
- O sistema deve exportar os relatórios em formato CSV.

## Requisitos não funcionais

### RNF01 - Usabilidade
- O sistema deve possuir uma interface intuitiva e de fácil utilização, utilizando a biblioteca FlatLaf para proporcionar um visual moderno e consistente.
- O sistema deve apresentar mensagens de erro claras e objetivas.
- O sistema deve possuir ajuda contextual para auxílio aos usuários.
- A interface gráfica deve ser responsiva e adaptar-se a diferentes resoluções de tela.

### RNF02 - Desempenho
- O sistema deve responder a consultas simples em no máximo 2 segundos.
- O sistema deve gerar relatórios complexos em no máximo 10 segundos.
- O sistema deve suportar o acesso simultâneo de pelo menos 10 usuários sem degradação de desempenho.

### RNF03 - Segurança
- Os dados sensíveis devem ser armazenados de forma criptografada no banco de dados.
- O sistema deve possuir mecanismos de proteção contra ataques de injeção SQL.
- O sistema deve encerrar automaticamente a sessão do usuário após 15 minutos de inatividade.
- O sistema deve manter um registro de logs de ações críticas para auditoria.

### RNF04 - Confiabilidade
- O sistema deve realizar backup automático diário do banco de dados.
- O sistema deve validar os dados de entrada para evitar inconsistências.
- O sistema deve possuir mecanismos de recuperação em caso de falhas.

### RNF05 - Portabilidade
- O sistema deve ser compatível com os sistemas operacionais Windows 10, Windows 11 e distribuições Linux baseadas em Debian.
- O sistema deve ser capaz de operar em computadores com configurações modestas (processador dual-core, 4GB de RAM).
- A utilização da biblioteca FlatLaf garantirá uma aparência consistente em diferentes sistemas operacionais.

### RNF06 - Manutenibilidade
- O código-fonte deve ser modular e bem documentado para facilitar manutenções futuras.
- O sistema deve seguir o padrão de arquitetura MVC para facilitar a separação das responsabilidades.
- O sistema deve utilizar Hibernate para o mapeamento objeto-relacional, facilitando a manutenção da camada de persistência.
- A interface gráfica deve ser implementada utilizando FlatLaf e seguindo boas práticas de organização de componentes Swing.
