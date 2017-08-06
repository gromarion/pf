CREATE TABLE endpoint_stats (
  id            SERIAL PRIMARY KEY,
  endpoint_url  varchar(255),
  statusCode    varchar(3),
  timeStamp     bigint
);

ALTER TABLE endpoint_stats OWNER TO finalproject;