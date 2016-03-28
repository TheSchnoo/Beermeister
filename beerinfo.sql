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
	BType CHAR(30),
	FName CHAR(20)  NOT NULL UNIQUE,		
	IBU FLOAT,
	ABV FLOAT,
	Description CHAR(255),
	BreweryName CHAR(30),	
	PRIMARY KEY(BName),
	-- CANDIDATE KEY (FName),
	FOREIGN KEY(BreweryName) REFERENCES Brewery (bname)
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

-- grant select on Searches to public;

insert into Brewery
values('Parallel 49');

insert into BeerInfo
values('Watermelo Witbier', 'Hefeweizen', 'WW Profile', 50, 5.22, 'Fruity, refreshing', 'Parallel 49');

insert into BeerInfo
values('Jerkface 9000', 'Wheat Ale', 'JF Profile', 37, 5.0, 'Citrus, floral, malt base, hop punch', 'Parallel 49');