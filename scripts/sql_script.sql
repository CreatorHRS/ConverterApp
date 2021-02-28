CREATE SCHEMA converterappschema;

CREATE TABLE converterappschema.converterappuser
(
 id            SERIAL PRIMARY KEY NOT NULL,
 username      VARCHAR(255)          NOT NULL,
 usermail      VARCHAR(255)          NOT NULL,
 userpass      VARCHAR(255)          NOT NULL
);

INSERT INTO converterappschema.converterappuser (username, usermail, userpass)
VALUES('TestUser', 'TestUserMail@mail.ru', 'TestUser');



CREATE TABLE converterappschema.valute(
   numCode  INT PRIMARY KEY NOT NULL,
   charCode VARCHAR(3)      NOT NULL
);

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (036, 'AUD');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (944, 'AZN');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (826, 'GBP');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (051, 'AMD');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (933, 'BYN');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (975, 'BGN');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (986, 'BRL');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (348, 'HUF');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (344, 'HKD');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (208, 'DKK');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (840, 'USD');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (978, 'EUR');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (356, 'INR');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (398, 'KZT');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (124, 'CAD');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (417, 'KGS');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (156, 'CNY');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (498, 'MDL');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (578, 'NOK');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (985, 'PLN');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (946, 'RON');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (960, 'XDR');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (702, 'SGD');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (972, 'TJS');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (949, 'TRY');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (934, 'TMT');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (860, 'UZS');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (980, 'UAH');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (203, 'CZK');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (752, 'SEK');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (756, 'CHF');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (710, 'ZAR');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (410, 'KRW');

INSERT INTO converterappschema.valute (numcode, charcode)
VALUES (392, 'JPY');

CREATE TABLE converterappschema.valute_value(
	id            SERIAL PRIMARY KEY NOT NULL,
	valute_numcode     INT                NOT NULL,
	valute_value  REAL               NOT NULL,
	nominal       INT                NOT NULL,
	date          DATE               NOT NULL
);

CREATE TABLE converterappschema.valute_info(
	id             SERIAL PRIMARY KEY NOT NULL,
	valute_numcode INT                NOT NULL,
	info           VARCHAR(255)       NOT NULL,
	locale         VARCHAR(5)         NOT NULL
);

INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (036, 'Австралийский доллар', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (944, 'Азербайджанский манат', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (826, 'Фунт стерлингов Соединенного королевства', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (051, 'Армянских драмов', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (933, 'Белорусский рубль', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (975, 'Болгарский лев', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (986, 'Бразильский реал', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (348, 'Венгерских форинтов', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (344, 'Гонконгских долларов', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (208, 'Датская крона', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (840, 'Доллар США', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (978, 'Евро', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (356, 'Индийских рупий', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (398, 'Казахстанских тенге', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (124, 'Канадский доллар', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (417, 'Киргизских сомов', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (156, 'Китайский юань', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (498, 'Молдавских леев', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (578, 'Норвежских крон', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (985, 'Польский злотый', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (946, 'Румынский лей', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (960, 'СДР (специальные права заимствования)', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (702, 'Сингапурский доллар', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (972, 'Таджикских сомони', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (949, 'Турецких лир', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (934, 'Новый туркменский манат', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (860, 'Узбекских сумов', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (980, 'Украинских гривен', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (203, 'Чешских крон', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (752, 'Шведских крон', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (756, 'Швейцарский франк', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (710, 'Южноафриканских рэндов', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (410, 'Вон Республики Корея', 'ru');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (392, 'Японских иен', 'ru');

INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (036, 'Australian dollar', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (944, 'Azerbaijani manat', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (826, 'Pound sterling', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (051, 'Armenian drams', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (933, 'Belarusian ruble', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (975, 'Bulgarian lev', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (986, 'Brazilian real', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (348, 'Hungarian forints', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (344, 'Hong Kong dollars', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (208, 'Danish krone', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (840, 'U.S. dollar', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (978, 'Euro', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (356, 'Indian rupees', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (398, 'Kazakhstani tenge', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (124, 'Canadian dollar', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (417, 'Kyrgyz soms', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (156, 'CNY', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (498, 'Moldovan lei', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (578, 'Norwegian kroner', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (985, 'Polish zloty', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (946, 'Romanian leu', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (960, 'SDR (Special Drawing Rights)', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (702, 'Singapore dollar', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (972, 'Tajik somoni', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (949, 'Turkish lira', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (934, 'New Turkmen manatт', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (860, 'Uzbek soums', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (980, 'Ukrainian hryvnia', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (203, 'Czech crowns', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (752, 'Swedish crowns', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (756, 'Swiss frank', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (710, 'South African rand', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (410, 'Won Republic of Korea', 'def');
INSERT INTO converterappschema.valute_info (valute_numcode, info, locale)
VALUES (392, 'Japanese yen', 'def');


CREATE TABLE converterappschema.historyentries
(
	id                   SERIAL PRIMARY KEY NOT NULL,
	from_valute_value_id INT                NOT NULL,
	to_valute_value_id   INT                NOT NULL,
	number               REAL               NOT NULL,
	user_id              INT                NOT NULL,
	date_of_convertion   DATE               NOT NULL,
	time_of_convertion   TIME               NOT NULL
);