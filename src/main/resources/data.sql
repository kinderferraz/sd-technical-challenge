-- estes usuarios são colocados apenas para iniciar a base,
-- pretendo adicionar um endpoint ao profile de desenvolvimento para isso, visando
-- automatizar os testes de carga

insert into USERS(dsc_cpf, dsc_name) values ('19279699059', 'Kinder Rodrigues');
insert into USERS(dsc_cpf, dsc_name) values ('63918296083', 'Ana Paula Padrão');
insert into USERS(dsc_cpf, dsc_name) values ('76925997000', 'Paolla Carosella');
insert into USERS(dsc_cpf, dsc_name) values ('67659269097', 'Erick Jacquin');
insert into USERS(dsc_cpf, dsc_name) values ('03508430073', 'Henrique Fogaça');
insert into USERS(dsc_cpf, dsc_name) values ('38552393042', 'Helena Rizzo');
insert into USERS(dsc_cpf, dsc_name) values ('11493026070', 'Renata Lo Prete');
insert into USERS(dsc_cpf, dsc_name) values ('90463445090', 'William Bonner');
insert into USERS(dsc_cpf, dsc_name) values ('75464234063', 'Renata Vasconcelos');
insert into USERS(dsc_cpf, dsc_name) values ('17847387000', 'Fatima Bernardes');

-- yet to open by user
insert into POLL(dsc_title, dsc_proposal, dat_created, idt_owner)
values ('Primeira proposta', 'comer bolo?', now(), 1);

-- opened by user
insert into POLL(dsc_title, dsc_proposal, dat_created, idt_owner, dat_opened, dat_ended, num_total_votes, num_accepted_votes)
values ('Segunda proposta',  'comer pipoca?', now(), 1, dateadd('DAY', -1, now()),  dateadd('MONTH', 1, now()), 4, 3);

-- closed by user
insert into POLL(dsc_title, dsc_proposal, dat_created, idt_owner, dat_opened, dat_ended, num_total_votes, num_accepted_votes)
values ('Terceira proposta', 'tomar café?', now(), 1, dateadd('MONTH', -1, now()),  dateadd('DAY', -1, now()), 4, 2);

-- opened by another user - logged in user voted
insert into POLL(dsc_title, dsc_proposal, dat_created, idt_owner, dat_opened, dat_ended, num_total_votes, num_accepted_votes)
values ('Quarta proposta',   'tomar sorvete?', now(), 2, dateadd('MONTH', -1, now()),  dateadd('DAY', 10, now()), 4, 1);

-- opened by another user - logged in user did not vote
insert into POLL(dsc_title, dsc_proposal, dat_created, idt_owner, dat_opened, dat_ended)
values ('Quinta proposta',   'comer pastel?', now(), 2, dateadd('MONTH', -1, now()),  dateadd('DAY', 10, now()));


-- user poll open
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (2, 2, 0);
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (3, 2, 1);
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (4, 2, 1);
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (5, 2, 1);

-- user poll closed
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (2, 3, 0);
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (3, 3, 0);
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (4, 3, 1);
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (5, 3, 1);

-- other user poll - vote by logged in user
insert into USER_VOTE(idt_user, idt_poll, flg_accepted)
values (1, 4, 1);
