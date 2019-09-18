CREATE TABLE vehicle (
	chassis_number INTEGER NOT NULL,
    chassis_series VARCHAR(50) NOT NULL,
    color VARCHAR(50),
	passengers_number TINYINT,
	type VARCHAR(20),
	PRIMARY KEY(chassis_number, chassis_series)
);