insert into Brewery
values('Parallel 49');

insert into BeerVendor(StoreName,Address)
values('Legacy Liquor Store','24 W. Elm');

insert into BeerVendor(StoreName,Address)
values('BC Liquor Store','99 E. 1st');

insert into BeerVendor(StoreName,Address)
values('UBC Liquor Store','300 St. Catherine');

insert into BeerVendor(StoreName,Address)
values('Darby\'s Liquor Store','Vancouver City Center');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
values('Gypsy Tears', 'Ruby Ale', 40, 6.0, 'Caramel', 'Parallel 49');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
values('Watermelon Witbier', 'Hefeweizen', 50, 5.22, 'Fruity, refreshing', 'Parallel 49');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
values('Jerkface 9000', 'Wheat Ale', 37, 5.0, 'Citrus, floral, malt base, hop punch', 'Parallel 49');

insert into BeerInStock
values('Gypsy Tears',1);

insert into customer(CName,CPassword) values('Paul','theendofthings');

insert into customer(CName,CPassword) values('Jim','theendofthings2');

INSERT into rates values(2,'Gypsy Tears', 7, 'Great!');
INsert into rates values(1,'Gypsy Tears', 3, 'Suboptimal');