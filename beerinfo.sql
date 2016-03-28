CREATE TABLE Brewery(
	bname CHAR(30),
	PRIMARY KEY (bname));

grant select on Brewery to public;

CREATE TABLE Customer(
	CID INTEGER,		
	CName CHAR(40),
	PRIMARY KEY(CID)
);

grant select on Customer to public;

CREATE TABLE BeerInfo (
	FName CHAR(20)  NOT NULL UNIQUE,		
	IBU INTEGER,	
	ABU INTEGER,
	BName CHAR(30),
	BType CHAR(30),
	Description CHAR(255),
	BreweryName CHAR(30),	
	PRIMARY KEY(BName),
	-- CANDIDATE KEY (FName),
	FOREIGN KEY(BreweryName) REFERENCES Brewery (bname)
	ON UPDATE CASCADE
	ON DELETE NO ACTION
);

grant select on BeerInfo to public;