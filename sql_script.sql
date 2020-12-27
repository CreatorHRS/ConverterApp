CREATE SCHEMA my_schema;

CREATE TABLE my_schema.converterappuser
{
 id            SERIAL PRIMARY KEY NOT NULL,
 username      CHAR(255)          NOT NULL,
 usermail      CHAR(255)          NOT NULL,
 userpass      CHAR(255)          NOT NULL
};

INSERT INTO y_schema.converterappuser (username, usermail, userpass)
VALUES('TestUser', 'TestUserMail@mail.ru', 'TestUser');

CREATE TABLE my_schema.valute(
   id            SERIAL PRIMARY KEY NOT NULL,
   name          CHAR(255)          NOT NULL,
   numCode       CHAR(6)            NOT NULL,
   charCode      CHAR(3)            NOT NULL,
   valute_value  REAL               NOT NULL,
   nominal       INT                NOT NULL,
	date          DATE               NOT NULL
);



CREATE TABLE my_schema.valute(
   id            SERIAL PRIMARY KEY NOT NULL,
   name          CHAR(255)          NOT NULL,
   numCode       CHAR(6)            NOT NULL,
   charCode      CHAR(3)            NOT NULL,
   valute_value  REAL               NOT NULL,
   nominal       INT                NOT NULL,
	date          DATE               NOT NULL
);

INSERT INTO  my_schema.valute (name, numCode, charCode, valute_value, nominal, date) 
VALUES ('Российский рубль', '643', 'RUB', 1.0, 1, '2020-01-01');



CREATE TABLE my_schema.historyentries
(
	ID                 SERIAL PRIMARY KEY NOT NULL,
	fromValute_id      INT                NOT NULL,
	toValute_id        INT                NOT NULL,
	valueToConvert     REAL               NOT NULL,
	user_id            INT                NOT NULL,
   date_of_convertion DATE               NOT NULL,
   time_of_convertion TIME               NOT NULL
);