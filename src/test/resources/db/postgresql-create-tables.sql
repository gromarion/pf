DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
  id serial NOT NULL,
  fullname character varying(255),
  username character varying(255),
  password character varying(255),
--  picture character varying(255),
  CONSTRAINT users_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS campaign CASCADE;
CREATE TABLE campaign (
  id serial NOT NULL,
  name character varying(255),
  endpoint character varying(255),
  graphs character varying(255),
  CONSTRAINT campaign_pkey PRIMARY KEY (id),
  UNIQUE(name)
);

INSERT INTO campaign (id, name, endpoint, graphs) VALUES (1, 'DBpedia Evaluation Campaign', 'http://live.dbpedia.org/sparql', 'http://live.dbpedia.org');

ALTER SEQUENCE campaign_id_seq RESTART WITH 2;

DROP TABLE IF EXISTS error CASCADE;
CREATE TABLE error (
  id serial NOT NULL,
  name character varying(255),
  example character varying(255),
  description character varying(10000),
  CONSTRAINT error_pkey PRIMARY KEY (id)
);

INSERT INTO error (id, name, example, description) VALUES
	(1, 'Tipo de dato incorrectamente extraído', '"foaf:description, Questa è una descrizione di una risorsa in Italiano@es" es incorrecto ya que el tipo de dato no se encuentra en español', 'Tipo de dato de un literal que está incorrectamente mapeado.'),
	(2, 'Valor del objeto extraído de forma incompleta', 'dbpprop:dateOfBirth “3”', 'Parte de los datos no han podido ser extraídos de forma completa, debido a algún tipo de error durante el proceso.'),
	(3, 'Objeto semánticamente incorrecto', 'Un dato no confiable podría ser "El atentado al World Trade Center ocurrió el 12 de Septiembre de 2001.", pues si se verifica en otras fuentes se concluirá que efectivamente el atentado fue el 11 de Septiembre del 2001, y no el 12.', 'Un valor es semánticamente correcto cuando representa el estado correcto de un objeto.'),
	(4, 'Enlace externo incorrecto', 'En el recurso "http://dbpedia.org/page/Canaan_Valley", la propiedad "dbo:wikiPageExternalLink" contiene enlaces caducados, o que no son relevantes al recurso en cuestión.', 'El enlace a un sitio web externo no contiene información relacionada al recurso en cuestión, o ha expirado.');

ALTER SEQUENCE error_id_seq RESTART WITH 5;

DROP TABLE IF EXISTS evaluation_session CASCADE;
CREATE TABLE evaluation_session (
  id SERIAL,
  campaign_id integer,
  user_id integer,
  -- 'timestamp' timestamp without time zone, PODRIA SER UN LONG QUE REPRESENTA LOS MILISEGUNDOS
    CONSTRAINT evaluation_session_pkey PRIMARY KEY (id),
    CONSTRAINT fke91f34937bce73b2 FOREIGN KEY (campaign_id)
        REFERENCES campaign (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT fke91f3493f49ff1d2 FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS evaluated_resource CASCADE;
CREATE TABLE evaluated_resource (
  id SERIAL,
  session_id integer,
  resource character varying(255),
  comments character varying(255),
  class character varying(255),
  correct boolean,
    CONSTRAINT evaluated_resource_pkey PRIMARY KEY (id),
    CONSTRAINT fk4f2da6e2fa763226 FOREIGN KEY (session_id)
        REFERENCES evaluation_session (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS evaluated_resource_detail CASCADE;
CREATE TABLE evaluated_resource_detail (
  id SERIAL,
  resource_id integer,
  predicate character varying(255),
  object character varying(10000),
  error_id integer,
  comment character varying(10000),
    CONSTRAINT evaluated_resource_detail_pkey PRIMARY KEY (id),
    CONSTRAINT fkfffa3de524231a0d FOREIGN KEY (resource_id)
        REFERENCES evaluated_resource (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE users OWNER TO finalproject;
ALTER TABLE campaign OWNER TO finalproject;
ALTER TABLE error OWNER TO finalproject;
ALTER TABLE evaluation_session OWNER TO finalproject;
ALTER TABLE evaluated_resource OWNER TO finalproject;
ALTER TABLE evaluated_resource_detail OWNER TO finalproject;