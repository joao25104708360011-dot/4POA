# Parte 2 - Catálogo: como testar no Eclipse

1. File > Import > Maven > Existing Maven Projects > selecione a pasta `estetica` > Finish.
2. Confira o JDK 17: botão direito no projeto > Properties > Java Build Path / Java Compiler.
3. Botão direito no projeto > Maven > Update Project (baixa as dependências).
4. Abra `br.clinica.EsteticaApp` > botão direito > Run As > Java Application.
5. Acesse http://localhost:8080/swagger-ui.html
6. POST /auth/login com {"login":"admin","senha":"123456"} > copie o token > Authorize (cadeado) > cole só o token.

## Dados de teste (classe DadosCatalogo)
- Especialidades: 1 Estética Facial, 2 Massoterapia
- Profissionais: Ana Souza (facial), Bruno Lima (massoterapia)
- Serviços: Limpeza de pele R$150 / 60 min, Drenagem linfática R$120 / 50 min
- Pacotes: 5 limpezas R$650, 10 drenagens R$1000

## Roteiro (resultado esperado)
| Teste | Esperado |
|---|---|
| GET /api/servicos (qualquer login) | 200 com a lista |
| GET /api/profissionais/especialidade/1 | 200, só a Ana |
| POST /api/especialidades logado como `profissional` | 403 "Sem permissão." |
| POST /api/especialidades {"nome":"Depilação"} como admin ou recepcao | 201 |
| Repetir o mesmo nome | 409 "Já existe uma especialidade com este nome." |
| POST /api/servicos com "preco": 0 | 400 "O preço deve ser maior que zero." |
| POST /api/pacotes com "quantidadeSessoes": 1 | 400 |
| GET /api/servicos/999 | 404 "Serviço não encontrado." |
| DELETE /api/especialidades/1 | 409 (em uso) |
| DELETE /api/servicos/1 | 409 (está em um pacote) |
| PUT /api/pacotes/1 mudando preço | 200 |
| Sem token | 401 |

## Arquivos da Parte 2 (só esses vão no Pull Request, branch parte2-catalogo)
model: Especialidade, Profissional, Servico, Pacote
repository: EspecialidadeRepository, ProfissionalRepository, ServicoRepository, PacoteRepository
dto: *Req e *Resp das 4 entidades
service: *Service e *ServiceImpl das 4 entidades
controller: EspecialidadeController, ProfissionalController, ServicoController, PacoteController
configuracao: DadosCatalogo
Não foi alterado: pom.xml, application.properties, pasta security.
