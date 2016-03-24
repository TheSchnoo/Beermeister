drop table Brewery
drop table Customer;
drop table BeerInfo;
drop table BeerInStock;
drop table StoreEmployeeHasA;
drop table Rates;
drop table Updates;
drop table SearchesFor;
drop table Searches;

CREATE TABLE Brewery(
	BreweryName CHAR(30),
	PRIMARY KEY (BName),
);

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
	Description CHAR(280),
	BreweryName CHAR(30),	
	PRIMARY KEY(BName),
	CANDIDATE KEY (FName),
	FOREIGN KEY(BreweryName) REFERENCES Brewery
	ON UPDATE CASCADE
	ON DELETE NO ACTION
);

grant select on BeerInfo to public;

CREATE TABLE BeerVendor (
	StoreID INTEGER,
	StoreName CHAR(30),
	PRIMARY KEY (StoreID)
	CANDIDATE KEY (StoreName, StoreID) UNIQUE,
	ON UPDATE: CASCADE
	ON DELETE: NO ACTION
);

grant select on BeerVendor to public;

CREATE TABLE BeerInStock (
	BName CHAR(30),
	StoreID INTEGER,
	PRIMARY KEY(BName, StoreID),
	FOREIGN KEY(BName) REFERENCES BeerInfo,
	FOREIGN KEY(StoreID) REFERNCES BeerVendor
);

grant select on BeerInStock to public;

CREATE TABLE StoreEmployeeHasA (
	EmpID	INTEGER,
	EmpName CHAR(40),
	PRIMARY KEY(EmpID)
	ON UPDATE CASCADE
	ON DELETE NO ACTION
);

grant select on StoreEmployeeHasA to public;

CREATE TABLE Rates (
	CID INTEGER,
	BName CHAR(30),
	PRIMARY KEY (CID, BName)
	FOREIGN KEY (CID) REFERENCES Customer,
	FOREIGN KEY (BName) REFERENCES BeerInfo
);

grant select on Rates to public;


CREATE TABLE Updates(
	BName CHAR(30), 
	StoreID INTEGER
	EmpID INTEGER
	PRIMARY KEY(BName, StoreID, EmpID),
	FOREIGN KEY (BName) REFERENCES BeerInfo,
	FOREIGN KEY(StoreID) REFERENCES BeerVendor,
	FOREIGN KEY(EmpID) REFERENCES StoreEmployee
);

grant select on Updates to public;

CREATE TABLE SearchesFor(
	CID INTEGER,		
		StoreID INTEGER,
	PRIMARY KEY (CID, StoreID)
	FOREIGN KEY (CID) REFERENCES Customer,
	FOREIGN KEY (StoreID) REFERENCES BeerVendor
);

grant select on SearchesFor to public;

CREATE TABLE Searches(
	CID INTEGER,		
		BName CHAR(30),
	PRIMARY KEY (CID, BName),
	FOREIGN KEY (CID) REFERENCES Customer,
	FOREIGN KEY (BName) REFERENCES BeerInfo
);

grant select on Searches to public;

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
