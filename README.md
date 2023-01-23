# Teste técnico

## Introdução
Seguindo a proposta enviada, este bff faz o intermédio e ntre um front-end e
um sistema de votação, impelementado em Java 11 com o framework Spring Boot 2.7.

### Proposta e entendimentos
O sistema de votação tem por princípio algumas regras assumidas,
baseadas tanto na descrição pedida quanto na interpretação das mensagens padrão informadas:

- Um usuário pode propor quantas pautas quiser;
- Uma pauta proposta precisa ser posta em votação pelo usuário que a criou;
- Apenas o usuario que criou uma pauta pode vê-la antes de abrí-la para votação;
- Uma vez aberta, o usuário que a criou não pode votar;
- Enquanto a votação estiver aberta, o resultado não pode ser visto;
- As seguintes regras foram impelementadass para validar o acesso aos detalhes de uma
  pauta:
  - um usuário pediu para ver seu historico de pautas, e tem acesso as que ainda 
    não foram abertas, às abertas e às fechadas,
  - um usuário pede a lista de pautas abertas, não pode ter acesso a uma pauta que 
    já esteja fechada,
  - um usuário pede a lista de pautas fechadas, não pode acessar pautas abertas,
  - qualquer outro meio de acessar uma pauta, com inconsistencia nos dados da requisição 
    será entendido como um acesso indevido, e terá uma exceção.
- O usuário define o dia de fechamento da votação, e se não for enviado um valor, 
  a votação fica aberta por apenas um minuto.

Dado o formato de mensagens padrão, e o fato que o acesso a cada dado é
feito por uma requet do tipo `POST`, optei por desviar a API do padrão RESTful,
de modo que cada endpoint possa de fato ser usado junto a um site web ou app mobile
que leia o modelo de mensagens proposto.

## Instruções de execução
Este projeto não precisa de instruções especiais para a primeira execuçao, além do comando 
`SPRING_PROFILES_ACTIVE=local ./gradlew bootRun'` no terminal, 
ou configurar o profile `local` no Intellij. Foi usada a biblioteca H2 para instanciar 
um banco para os testes e execução em ambiente de desenvolvimento, e no momento de 
início do programa um arqvivo deve ser criado como path `./db/demo.mv.db`.

Entretanto, recomendo deletar este arquivo auto gerado ou renomear o arquivo 
[data.sql](./src/main/resources/data.sql), pois uma das colunas do banco possui
uma `unique constraint`, de modo que um restart do programa irá ser mal sucedido.
Vale notar também que o arquivo `data.sql` precisa existir para que os testes sejam 
executados com suceso.

## Documentação
Mais detalhes sobre o processo de implementação 
podem ser encontrados nas [notas de implementação](./docs/notes.md).
Uma [coleção do postman](./docs/BoothApi.postman_collection.json) é fornecida 
como demonstrativo das funcionalidades da API. Junto destes arquivos há prints
com a cobertura de teste, analizada pelo Intellij.

## Agradecimentos
Muito obriado pela oportunidade e pelo tempo dedicado a avaliar este projeto!
:rocket::rocket:

