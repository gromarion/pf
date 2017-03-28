-- DROP TABLE campaign;

CREATE TABLE campaign
(
  id serial NOT NULL,
  cendpoint character varying(255),
  cgraphs character varying(255),
  cname character varying(255),
  copened integer,
  CONSTRAINT campaign_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE campaign
  OWNER TO finalproject;

-- DROP TABLE users;

CREATE TABLE users
(
  id serial NOT NULL,
  googleid character varying(255),
  name character varying(255),
  picture character varying(255),
  profile character varying(255),
  statd integer,
  statr integer,
  statt integer,
  CONSTRAINT users_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO finalproject;

  -- DROP TABLE classes;

  CREATE TABLE classes
  (
    id serial NOT NULL,
    count_cache numeric(19,2),
    is_leaf boolean,
    cname character varying(255),
    cparent numeric(19,2),
    curi character varying(255),
    CONSTRAINT classes_pkey PRIMARY KEY (id)
  )
  WITH (
    OIDS=FALSE
  );
  ALTER TABLE classes
    OWNER TO finalproject;

    -- DROP TABLE errors;

    CREATE TABLE errors
    (
      id serial NOT NULL,
      description character varying(255),
      error_parent numeric(19,2),
      example_n3 character varying(255),
      example_uri character varying(255),
      is_leaf boolean,
      error_title character varying(255),
      CONSTRAINT errors_pkey PRIMARY KEY (id)
    )
    WITH (
      OIDS=FALSE
    );
    ALTER TABLE errors
      OWNER TO finalproject;

-- DROP TABLE evaluation_session;

CREATE TABLE evaluation_session
(
  id serial NOT NULL,
  "timestamp" bytea,
  campaign_id integer,
  user_id integer,
  CONSTRAINT evaluation_session_pkey PRIMARY KEY (id),
  CONSTRAINT fke91f34937bce73b2 FOREIGN KEY (campaign_id)
      REFERENCES campaign (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fke91f3493f49ff1d2 FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE evaluation_session
  OWNER TO finalproject;

-- DROP TABLE evaluated_resource;

CREATE TABLE evaluated_resource
(
  id serial NOT NULL,
  class character varying(255),
  comments character varying(255),
  correct boolean,
  resource character varying(255),
  session_id integer,
  CONSTRAINT evaluated_resource_pkey PRIMARY KEY (id),
  CONSTRAINT fk4f2da6e2fa763226 FOREIGN KEY (session_id)
      REFERENCES evaluation_session (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE evaluated_resource
  OWNER TO finalproject;

-- DROP TABLE evaluated_resource_details;

CREATE TABLE evaluated_resource_details
(
  id serial NOT NULL,
  comment character varying(255),
  error_id numeric(19,2),
  object character varying(255),
  predicate character varying(255),
  resource_id integer,
  CONSTRAINT evaluated_resource_details_pkey PRIMARY KEY (id),
  CONSTRAINT fkfffa3de524231a0d FOREIGN KEY (resource_id)
      REFERENCES evaluated_resource (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE evaluated_resource_details
  OWNER TO finalproject;