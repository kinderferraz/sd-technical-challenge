# Teste técnico

## Introdução
Seguindo a proposta enviada, este bff faz o intermédio e ntre um front-end e
um sistema de votação, impelementado em Java 11 com o framework Spring Boot 2.7.

### Proposta e entendimentos
O sistema de votação tem por princípio algumas regras assumidas:

Dado o formato de mensagens padrão, e o fato que o acesso a cada dado é
feito por uma requet do tipo `POST`, optei por desviar a API do padrão RESTful,
de modo que cada endpoint possa de fato ser usado junto a um site web ou app mobile
que leia o modelo de mensagens proposto.

## Instruçõess de execução
Este projeto não precisa de instruções especiais para a primeira execuçao, além do comando 
`./gradlew bootRun`. Foi usada a biblioteca H2 para instanciar um banco para os testes 
e execução em ambiente de desenvolvimento, e no momento de início do programa um arqvivo 
deve ser criado como path `./db/demo.mv.db`.

Entretanto, recomendo deletar este arquivo ou renomear o arquivo [data.sql](./src/main/resources/data.sql),
pois uma das colunas do banco possui uma `unique constraint`, de modo que um restart do programa 
irá ser mal sucedido.

## Documentação
Mais detalhes sobre o processo de implementação 
podem ser encontrados nas [notas de implementação](./docs/notes.md).
Uma [coleção do postman](./docs/BoothApi.postman_collection.json) é fornecida 
como demonstrativo das funcionalidades da API. Junto destes arquivos há prints
com a cobertura de teste, analizada pelo Intellij.

## Agradecimentos
Muito obriado pela oportunidade e pelo tempo dedicado a avaliar este projeto!
:rocket::rocket:

