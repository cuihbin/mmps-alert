DELETE FROM alert_config;

INSERT INTO alert_config(config_key, config_value) VALUES ('systemName', 'XX系统');
INSERT INTO alert_config(config_key, config_value) VALUES ('minutesBeforePlayerFault', '30');
INSERT INTO alert_config(config_key, config_value) VALUES ('minutesBeforePlayerLastingFault', '2880');
INSERT INTO alert_config(config_key, config_value) VALUES ('minutesBeforeServerFault', '10');
INSERT INTO alert_config(config_key, config_value) VALUES ('minutesBeforeJmsBrokerFault', '5');
