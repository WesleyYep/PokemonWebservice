create table AuditEntry (_id bigint not null, _crudOperator varchar(255), _timestamp timestamp, _uri varchar(255), _user__id bigint, primary key (_id))
create table Battle (id bigint generated by default as identity, endTime timestamp, latitude double not null, longitude double not null, startTime timestamp, winnerId bigint not null, FIRST_TRAINER_ID bigint not null, SECOND_TRAINER_ID bigint not null, primary key (id))
create table MOVES (Pokemon_id bigint not null, accuracy integer not null, name varchar(255), power integer not null, type integer, primary key (Pokemon_id, accuracy, power))
create table POKEMON_TEAM (team_id bigint not null, POKEMON_ID bigint not null, primary key (POKEMON_ID))
create table POKEMON_TRAINER (trainer_id bigint not null, POKEMON_ID bigint not null, primary key (POKEMON_ID))
create table Pokemon (id bigint generated by default as identity, gender varchar(255), level integer not null, name varchar(255), nickname varchar(255), primary key (id))
create table TRAINER_CONTACTS (TRAINER_ID bigint not null, OTHER_ID bigint not null, primary key (TRAINER_ID, OTHER_ID))
create table Team (id bigint generated by default as identity, teamGrade varchar(255), teamName varchar(255), trainer_id bigint not null, primary key (id))
create table Trainer (id bigint generated by default as identity, dateOfBirth date, firstName varchar(255), gender varchar(255), lastName varchar(255), badgesWon integer not null, battlesPlayed integer not null, battlesWon integer not null, competitionWins integer not null, primary key (id))
create table User (_id bigint not null, _firstname varchar(255), _lastname varchar(255), _username varchar(255), passwordHash varchar(255) not null, primary key (_id))
alter table Team add constraint UK_a68uxcbfyjwk3pwhhwq9poe8d  unique (trainer_id)
alter table User add constraint UK_h326w87hehh9nrpwpn8pq9hea  unique (_username)
alter table AuditEntry add constraint FK_fvxe57t004d4t2pjlfvd8s3a0 foreign key (_user__id) references User
alter table Battle add constraint FK_m5ihugb88wcyb12xky62ek8fu foreign key (FIRST_TRAINER_ID) references Trainer
alter table Battle add constraint FK_mhvnfarjahne7ksuwjeo0vw1u foreign key (SECOND_TRAINER_ID) references Trainer
alter table MOVES add constraint FK_3nlk7svhkpu354ntewk1q5n1a foreign key (Pokemon_id) references Pokemon
alter table POKEMON_TEAM add constraint FK_r5jcwxxhq0c29kpat7td2xgh6 foreign key (team_id) references Team
alter table POKEMON_TEAM add constraint FK_7h90rn829m7q2rathnoltol2v foreign key (POKEMON_ID) references Pokemon
alter table POKEMON_TRAINER add constraint FK_7q3gc66tikeb030n0p578tpdq foreign key (trainer_id) references Trainer
alter table POKEMON_TRAINER add constraint FK_bvf41mtur0uvemrtsmsfsdmo9 foreign key (POKEMON_ID) references Pokemon
alter table TRAINER_CONTACTS add constraint FK_pq60g26d329k41s328lxlwyc0 foreign key (OTHER_ID) references Trainer
alter table TRAINER_CONTACTS add constraint FK_jqb45gbley5uc949gouh6w07a foreign key (TRAINER_ID) references Trainer
alter table Team add constraint FK_a68uxcbfyjwk3pwhhwq9poe8d foreign key (trainer_id) references Trainer
create sequence hibernate_sequence start with 1 increment by 1
