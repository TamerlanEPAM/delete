CREATE TABLE team(
	team VARCHAR(50) PRIMARY KEY,
	city VARCHAR(50) NOT NULL,
	coach VARCHAR(50) NOT NULL,
    captain INTEGER UNIQUE
);

CREATE TABLE player(
	player_id SERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	position VARCHAR(50) NOT NULL,
	skill_level VARCHAR(50) NOT NULL,
	team VARCHAR(50),
	FOREIGN KEY (team) REFERENCES team(team)
);

ALTER TABLE team
ADD FOREIGN KEY (captain) REFERENCES player(player_id);

CREATE TABLE game(
	game_id SERIAL PRIMARY KEY,
	date DATE NOT NULL,
	host_score INTEGER NOT NULL,
	guest_score INTEGER NOT NULL,
	host_team VARCHAR(50) NOT NULL,
	guest_team VARCHAR(50) NOT NULL,
	FOREIGN KEY (host_team) REFERENCES team(team),
	FOREIGN KEY (guest_team) REFERENCES team(team)
);

CREATE TABLE penalty(
	penalty VARCHAR(50) PRIMARY KEY,
	description VARCHAR(255) NOT NULL
);

CREATE TABLE player_penalty_game(
	player_penalty_game_id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL,
	player_name VARCHAR(50) NOT NULL,
	penalty VARCHAR(50) NOT NULL,
	game_id INTEGER NOT NULL,
	FOREIGN KEY (player_id) REFERENCES player(player_id),
	FOREIGN KEY (penalty) REFERENCES penalty(penalty),
	FOREIGN KEY (game_id) REFERENCES game(game_id)
);


