DROP TABLE Cartelera;


CREATE DATABASE IF NOT EXISTS Cine_DB;
USE Cine_DB;

CREATE TABLE IF NOT EXISTS Cartelera (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL UNIQUE,
    director VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    duracion INT NOT NULL, --  INTEGER EN MINUTOS
    genero ENUM('Comedia', 'Drama', 'Acción', 'Ciencia Ficción', 'Terror', 'Aventura') NOT NULL  -- lo que hace el enum es que solo nos dejar ingresar los valores del enum, en cualquier otro caso nos dará error
);


-- datos de prueba

INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES
('Star Wars - The revenge of the Sith', 'George Lucas', 2005, 140,'Ciencia Ficción'),
('Star Wars - A New Hope', 'George Lucas', 1978, 121,'Ciencia Ficción');

SELECT * FROM Cartelera;





INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES
-- COMEDIA (5 películas)
('Superbad', 'Greg Mottola', 2007, 113, 'Comedia'),
('The Hangover', 'Todd Phillips', 2009, 100, 'Comedia'),
('Bridesmaids', 'Paul Feig', 2011, 125, 'Comedia'),
('Step Brothers', 'Adam McKay', 2008, 98, 'Comedia'),
('21 Jump Street', 'Phil Lord', 2012, 109, 'Comedia'),

-- DRAMA (5 películas)
('The Shawshank Redemption', 'Frank Darabont', 1994, 142, 'Drama'),
('Forrest Gump', 'Robert Zemeckis', 1994, 142, 'Drama'),
('The Godfather', 'Francis Ford Coppola', 1972, 175, 'Drama'),
('Fight Club', 'David Fincher', 1999, 139, 'Drama'),
('Good Will Hunting', 'Gus Van Sant', 1997, 126, 'Drama'),

-- ACCIÓN (5 películas)
('John Wick', 'Chad Stahelski', 2014, 101, 'Acción'),
('Mad Max: Fury Road', 'George Miller', 2015, 120, 'Acción'),
('The Dark Knight', 'Christopher Nolan', 2008, 152, 'Acción'),
('Die Hard', 'John McTiernan', 1988, 132, 'Acción'),
('Mission: Impossible', 'Brian De Palma', 1996, 110, 'Acción'),

-- CIENCIA FICCIÓN (5 películas)
('Inception', 'Christopher Nolan', 2010, 148, 'Ciencia Ficción'),
('The Matrix', 'Lana Wachowski', 1999, 136, 'Ciencia Ficción'),
('Interstellar', 'Christopher Nolan', 2014, 169, 'Ciencia Ficción'),
('Blade Runner 2049', 'Denis Villeneuve', 2017, 164, 'Ciencia Ficción'),
('Arrival', 'Denis Villeneuve', 2016, 116, 'Ciencia Ficción'),

-- TERROR (5 películas)
('The Conjuring', 'James Wan', 2013, 112, 'Terror'),
('Get Out', 'Jordan Peele', 2017, 104, 'Terror'),
('A Quiet Place', 'John Krasinski', 2018, 90, 'Terror'),
('Hereditary', 'Ari Aster', 2018, 127, 'Terror'),
('The Exorcist', 'William Friedkin', 1973, 122, 'Terror'),

-- AVENTURA (5 películas)
('Indiana Jones: Raiders of the Lost Ark', 'Steven Spielberg', 1981, 115, 'Aventura'),
('Jurassic Park', 'Steven Spielberg', 1993, 127, 'Aventura'),
('The Lord of the Rings: The Fellowship of the Ring', 'Peter Jackson', 2001, 178, 'Aventura'),
('Pirates of the Caribbean: The Curse of the Black Pearl', 'Gore Verbinski', 2003, 143, 'Aventura'),
('Jumanji: Welcome to the Jungle', 'Jake Kasdan', 2017, 119, 'Aventura');