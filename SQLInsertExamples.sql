insert into Brewery(BName)
values('brewery name as a string');

insert into BeerVendor(StoreName,Address)
values('Store name as string','store address as string');

insert into BeerInfo(BName,	BType, FName, IBU, ABV,	Description, BreweryName)
values('Beer name', 'Beer Type', 'Flavor Profile Name', IBU double, ABV double, 'Description', 'Brewery Name');

insert into BeerInStock(BName, StoreID)
values('Beer Name',storeID int);

insert into customer(CName,CPassword)
values('Customer name','customer password');

INSERT into rates(CID, BName,BRate) values(customerID int,'Beer Name',BeerRating int);