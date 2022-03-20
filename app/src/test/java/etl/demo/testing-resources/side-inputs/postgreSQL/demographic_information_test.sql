-- -------------------------------------------------------------
-- TablePlus 3.10.0(348)
--
-- https://tableplus.com/
--
-- Database: postgres
-- Generation Time: 2020-11-25 17:17:52.6040
-- -------------------------------------------------------------

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';


-- This script only contains the table creation statements and does not fully represent the table in the database. It's still missing: indices, triggers. Do not use it as a backup.

-- Table Definition
CREATE TABLE public.demographic_information (
    device_id varchar(50),
    device_type varchar(10),
    data_source varchar(20),
    country varchar(5),
    gender varchar(150),
    age varchar(150),
    income varchar(150),
    last_time_updated int8,

    PRIMARY KEY (device_id, data_source)
);

INSERT INTO public.demographic_information (device_id, device_type, data_source, country, gender, age, income, last_time_updated) VALUES
('ABC', 'cookie' ,'factual' ,'us', '{"m":0,"f":0}', '{ "18-24":0, "25-34":0, "35-44":0}', NULL, 1607500480),
('ABC-04', 'cookie' ,'oracle' ,'be', '{"m":1,"f":0}', '{ "18-24":2}', NULL, 1607500480);


