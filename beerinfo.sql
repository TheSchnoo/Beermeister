CREATE TABLE Brewery(
	BName CHAR(30),
	PRIMARY KEY (BName));

-- grant select on Brewery to public;

CREATE TABLE Customer(
	CID int NOT NULL AUTO_INCREMENT,
	CName CHAR(40),
	CPassword CHAR(40),
	PRIMARY Key (CID));

-- grant select on Customer to public;

CREATE TABLE CustomerSession(
	CID int NOT NULL,
	SID CHAR(40) NOT NULL,
	PRIMARY KEY (CID),
	FOREIGN KEY (CID) REFERENCES Customer (CID)
		ON DELETE CASCADE);

-- grant select on CustomerSession to public;

CREATE TABLE BeerInfo (
	BName CHAR(30),
	BType CHAR(30),
	FName CHAR(20)  NOT NULL UNIQUE,
	IBU double(5,2),
	ABV double(5,2),
	Description CHAR(255),
	BreweryName CHAR(30),
	Brewed BOOLEAN
		DEFAULT 1,
	AvgRating Double(4,2)
		DEFAULT 0,
	PRIMARY KEY(BName),
	-- CANDIDATE KEY (FName),
	FOREIGN KEY(BreweryName) REFERENCES Brewery (BName)
		ON UPDATE CASCADE
		ON DELETE NO ACTION
);

-- grant select on BeerInfo to public;

CREATE TABLE BeerVendor (
	StoreID int NOT NULL AUTO_INCREMENT,
	StoreName CHAR(30),
	Address CHAR(255),
	PRIMARY KEY (StoreID)
	-- CANDIDATE KEY (StoreName, StoreID) UNIQUE,
	-- ON UPDATE CASCADE
	-- ON DELETE NO ACTION
);

-- grant select on BeerVendor to public;

CREATE TABLE BeerInStock (
	BName CHAR(30),
	StoreID INTEGER,
	PRIMARY KEY(BName, StoreID),
	FOREIGN KEY(BName) REFERENCES BeerInfo (BName),
	FOREIGN KEY(StoreID) REFERENCES BeerVendor (StoreID)
);

-- grant select on BeerInStock to public;

CREATE TABLE StoreEmployeeHasA (
	EmpID	INTEGER,
	EmpName CHAR(40),
	PRIMARY KEY(EmpID)
	-- ON UPDATE CASCADE
	-- ON DELETE NO ACTION
);

-- grant select on StoreEmployeeHasA to public;

CREATE TABLE Rates (
	CID INTEGER,
	BName CHAR(30),
	BRate INTEGER,
	Review CHAR(255)
		DEFAULT '',
	PRIMARY KEY (CID, BName),
	FOREIGN KEY (CID) REFERENCES Customer (CID),
	FOREIGN KEY (BName) REFERENCES BeerInfo (BName)
);

-- grant select on Rates to public;


CREATE TABLE Updates(
	BName CHAR(30),
	StoreID INTEGER,
	EmpID INTEGER,
	PRIMARY KEY(BName, StoreID, EmpID),
	FOREIGN KEY (BName) REFERENCES BeerInfo (BName),
	FOREIGN KEY(StoreID) REFERENCES BeerVendor (StoreID),
	FOREIGN KEY(EmpID) REFERENCES StoreEmployeeHasA(EmpID)
);

-- grant select on Updates to public;

CREATE TABLE SearchesFor(
	CID INTEGER,
	StoreID INTEGER,
	PRIMARY KEY (CID, StoreID),
	FOREIGN KEY (CID) REFERENCES Customer (CID),
	FOREIGN KEY (StoreID) REFERENCES BeerVendor (StoreID)
);

-- grant select on SearchesFor to public;

CREATE TABLE Searches(
	CID INTEGER,
	BName CHAR(30),
	PRIMARY KEY (CID, BName),
	FOREIGN KEY (CID) REFERENCES Customer (CID),
	FOREIGN KEY (BName) REFERENCES BeerInfo (BName)
);
DELIMITER $$

CREATE PROCEDURE update_avg_ratings_table
	(IN beername CHAR(30))
MODIFIES SQL DATA
	BEGIN
		UPDATE BeerInfo
		SET AvgRating = (SELECT AVG(Rates.BRate)
										 FROM Rates
										 WHERE BName=beername)
		WHERE beername = BName;
	END $$
DELIMITER ;

CREATE TRIGGER the_average_insert AFTER INSERT ON Rates
FOR EACH ROW CALL update_avg_ratings_table(New.BName);

CREATE TRIGGER the_average_update AFTER UPDATE ON Rates
FOR EACH ROW CALL update_avg_ratings_table(New.BName);