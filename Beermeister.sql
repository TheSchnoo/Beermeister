CREATE TABLE Brewery(
	BreweryName CHAR(30),
	PRIMARY KEY (BName),
);

CREATE TABLE Customer(
	CID INTEGER,		
	CName CHAR(40),
	PRIMARY KEY(CID)
);

CREATE TABLE BeerInfo (
	FName CHAR(20)  NOT NULL UNIQUE,		
	IBU INTEGER,	
	ABU INTEGER,
	BName CHAR(30),
	BType CHAR(30),
	Description CHAR(280),
	BreweryName CHAR(30),	
	PRIMARY KEY(BName),
	CANDIDATE KEY (FName),
	FOREIGN KEY(BreweryName) REFERENCES Brewery
	ON UPDATE CASCADE
	ON DELETE NO ACTION
);

CREATE TABLE BeerVendor (
	StoreID INTEGER,
	StoreName CHAR(30),
	PRIMARY KEY (StoreID)
	CANDIDATE KEY (StoreName, StoreID) UNIQUE,
	ON UPDATE: CASCADE
	ON DELETE: NO ACTION
);

CREATE TABLE BeerInStock (
	BName CHAR(30),
	StoreID INTEGER,
	PRIMARY KEY(BName, StoreID),
	FOREIGN KEY(BName) REFERENCES BeerInfo,
	FOREIGN KEY(StoreID) REFERNCES BeerVendor
);

CREATE TABLE StoreEmployeeHasA (
	EmpID	INTEGER,
	EmpName CHAR(40),
	PRIMARY KEY(EmpID)
	ON UPDATE CASCADE
	ON DELETE NO ACTION
	
);

CREATE TABLE Rates (
	CID INTEGER,
	BName CHAR(30),
	PRIMARY KEY (CID, BName)
	FOREIGN KEY (CID) REFERENCES Customer,
	FOREIGN KEY (BName) REFERENCES BeerInfo
);


CREATE TABLE Updates(
	BName CHAR(30), 
	StoreID INTEGER
	EmpID INTEGER
	PRIMARY KEY(BName, StoreID, EmpID),
	FOREIGN KEY (BName) REFERENCES BeerInfo,
	FOREIGN KEY(StoreID) REFERENCES BeerVendor,
	FOREIGN KEY(EmpID) REFERENCES StoreEmployee
);

CREATE TABLE SearchesFor(
	CID INTEGER,		
		StoreID INTEGER,
	PRIMARY KEY (CID, StoreID)
	FOREIGN KEY (CID) REFERENCES Customer,
	FOREIGN KEY (StoreID) REFERENCES BeerVendor
);

CREATE TABLE Searches(
	CID INTEGER,		
		BName CHAR(30),
	PRIMARY KEY (CID, BName),
	FOREIGN KEY (CID) REFERENCES Customer,
	FOREIGN KEY (BName) REFERENCES BeerInfo
);

insert into Brewery
values('Parallel 49');

insert into Brewery
values('Hoyne');

insert into Brewery
values('Moon Underwater');

insert into BeerInfo
values('Citrus Hefeweizen', 'Hefeweizen', 'MUH Profile', 30, 5.78, 'Delicious and citrusy', 'Moon Underwater');

insert into BeerInfo
values('Dark Matter', 'American Brown', 'DM Profile', 30, 6.21, 'Dark, full bodied', 'Hoyne');

insert into BeerInfo
values('Watermelo Witbier', 'Hefeweizen', 'WW Profile', 50, 5.22, 'Fruity, refreshing', 'Parallel 49');
