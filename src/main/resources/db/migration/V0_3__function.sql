CREATE OR REPLACE FUNCTION inc(player_position VARCHAR, delimiter VARCHAR(2)) 
RETURNS VARCHAR
AS
$$
declare results VARCHAR;
BEGIN
results = array_to_string(array(
	SELECT name
	FROM player
	WHERE position = player_position), delimiter);
RETURN results;
END;
$$
LANGUAGE PLPGSQL;