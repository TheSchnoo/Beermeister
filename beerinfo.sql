CREATE TABLE Brewery(
	bname CHAR(30),
	PRIMARY KEY (bname));

-- grant select on Brewery to public;

CREATE TABLE Customer(
	CID INTEGER,		
	CName CHAR(40),
	PRIMARY KEY(CID));

-- grant select on Customer to public;

CREATE TABLE BeerInfo (
	BName CHAR(30),
	Type CHAR(30),
	FName CHAR(20)  NOT NULL UNIQUE,		
	IBU FLOAT,
	ABV FLOAT,
	Description CHAR(255),
	BreweryName CHAR(30),
	Avg FLOAT NOT NULL ,
	PRIMARY KEY(BName),
	-- CANDIDATE KEY (FName),
	FOREIGN KEY(BreweryName) REFERENCES Brewery (bname)
		ON UPDATE CASCADE
		ON DELETE NO ACTION,
	FOREIGN KEY(Avg) REFERENCES Ratings (Avg_rating)
		ON UPDATE CASCADE
		ON DELETE NO ACTION
);

-- grant select on BeerInfo to public;

CREATE TABLE BeerVendor (
	StoreID INTEGER,
	StoreName CHAR(30),
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

CREATE TABLE Ratings(
	Bname CHAR(30),
	Avg_Rating FLOAT,
	PRIMARY KEY(Bname),
	FOREIGN KEY (BName) REFERENCES BeerInfo (BName)
);

CREATE TRIGGER the_averages AFTER INSERT ON Rates
FOR EACH ROW CALL update_or_insert(BName,BRate);

-- grant select on Searches to public;


insert into BeerInStock
values('Gypsy Tears',0);

insert into BeerVendor
values(0, 'Legacy Liquor Store');

insert into BeerVendor
values(1, 'BC Liquor Store');

insert into BeerVendor
values(2, 'UBC Liquor Store');

insert into BeerVendor
values(3, 'Darby\'s Liquor Store');

insert into BeerInfo
values('Gypsy Tears', 'Ruby Ale', 'GT Profile', 40, 6.0, 'Caramel', 'Parallel 49',0);

insert into Brewery
values('Parallel 49');

insert into BeerInfo
values('Watermelon Witbier', 'Hefeweizen', 'WW Profile', 50, 5.22, 'Fruity, refreshing', 'Parallel 49',0);

insert into BeerInfo
values('Jerkface 9000', 'Wheat Ale', 'JF Profile', 37, 5.0, 'Citrus, floral, malt base, hop punch', 'Parallel 49',0);

-- Procedure;
DELIMITER $$

Create PROCEDURE update_or_insert
	(IN beername CHAR(30), IN rate INTEGER)
	MODIFIES SQL DATA
	BEGIN
		DECLARE duplicate_key INT DEFAULT 0;
		BEGIN
			DECLARE EXIT HANDLER FOR 1062 SET duplicate_key = 1;
			INSERT into Ratings (Bname, Avg_Rating) VALUES (beername,rate);

		END;

	IF duplicate_key=1 THEN
		CALL update_avg_ratings_table(beername,rate);
	END IF;
END $$



CREATE PROCEDURE update_avg_ratings_table
	(IN beername CHAR(30))
	BEGIN
		UPDATE Ratings
		SET Avg_Rating = (SELECT AVG(Rates.BRate)
									 FROM Ratings
									 WHERE BName=beername)
		WHERE beername = BName;
	END $$


-- example of the above 2 procedures in use update_average(update_avg_ratings_table("Parallel 49"));