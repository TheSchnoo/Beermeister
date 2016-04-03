
-- Breweries

insert into Brewery
values('Parallel 49');

insert into Brewery
VALUES ('33 Acres');

insert into Brewery
VALUES ('Bomber Brewing');

insert into Brewery
VALUES ('Brassneck Brewery');

insert into Brewery
VALUES ('Central City');

insert into Brewery
VALUES ('Granville Island Brewing');


-- Vendors
insert into BeerVendor(StoreName,Address,SPassword)
values('Legacy Liquor Store','24 W. Elm','admin');

insert into BeerVendor(StoreName,Address,SPassword)
values('Granville Island Brewing Retail Store','1441 Cartwright St','admin');

insert into BeerVendor(StoreName,Address,SPassword)
values('BC Liquor Store','99 E. 1st','admin');

insert into BeerVendor(StoreName,Address,SPassword)
values('UBC Liquor Store','300 St. Catherine','admin');

insert into BeerVendor(StoreName,Address,SPassword)
values('Darbys Liquor Store','Vancouver City Center','admin');

insert into BeerVendor(StoreName,Address,SPassword)
values('Liberty Liquor Store','1622 Commercial Drive','admin');

-- Beers
insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
values('Gypsy Tears', 'Amber Ale', 40, 6.0, 'Caramel', 'Parallel 49');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
values('Watermelon Witbier', 'Hefeweizen', 50, 5.22, 'Fruity, refreshing', 'Parallel 49');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
values('Jerkface 9000', 'Hefeweizen', 37, 5.0, 'Citrus, floral, malt base, hop punch', 'Parallel 49');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('33 Acres of Life','Lager',40,4.8,'This hybrid utilizes Mount Hood hops, which lend a spice infused aroma. A fruit-like quality is created by fermenting the lager at ale temperatures.','33 Acres');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('33 Acres of Ocean','Pale Ale',50,5.3,'A full flavoured beer integrated with a distinct floral hop which gives it a quality unique to our Pacific Northwest surroundings.','33 Acres');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('33 Acres of Sunshine','Hefeweizen',14,5,'Lightly hopped with Styrian Goldings and flavoured with orange peel, coriander seed and anise seed, brings out a fruity, spicy character.','33 Acres');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('33 Acres of Darkness','Lager',30,5,'If you’re the type who likes to judge a beer by it’s colour, 33 Acres of Darkness will surely surprise. While most equate heaviness with flavour this Schwarzbier strives to bring tantalizing taste beyond the usual weight.','33 Acres');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('33 Acres of Euphoria','Double/Tripel',30,9.2,'A fruity lemon rind finish conceals what is pound for pound our most dangerous, yet very drinkable, Belgian.','33 Acres');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('33 Acres of Nirvana','IPA',70,7,'Grapefruit and “woody” bitterness, warming finish','33 Acres');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
    VALUES ('Bomber ESB','ESB',45, 5.2,'Notes of roasted coffee, chocolate, and molasses, finished with a balanced hop bitterness.','Bomber Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Bomber Pilsner','Pilsner',28, 4.8,'Light bodied and refreshing. Crisp, dry malt profile with the lingering aroma of Czech Saaz hops and citrus zest.','Bomber Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Bomber IPA','IPA',65, 6.3,'Grapefruit and floral aromas with big hop flavour.  Notes of caramel, malt, and dried grass.','Bomber Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Bomber Blonde','Blonde Ale',17, 5.0,'The drinkability of an American Blonde Ale with the complexity of its Belgian counterpart!','Bomber Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Shout Out Stout','Stout',45, 6.0,'Dark chocolate and cocoa aromas. Medium-bodied with a tickle of carbonation; just enough to keep this stout refreshing! Oatmeal cookie & burnt chocolate notes. Lingering espresso finish.','Bomber Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Choqlette Porter','Porter',18, 5.5,'3 chocolate additions are done to build flavour and aroma, with French Aramis hops giving it a mild floral aroma beneath the roastiness and chocolate.','Bomber Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
    VALUES ('Massive Aggressive','IPA',80,9,'Massive Aggressive was brewed to commemorate the Alibi Room’s 600th beer list. It’s our flagship beer, Passive Aggressive on steroids.','Brassneck Brewery');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Magician’s Assistant','Blonde Ale',18,6,'Experimental Sour with Amarillo and Citra hops','Brassneck Brewery');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Passive Aggressive Pale Ale','IPA',60,7,'Single hop, dry hopped, extra pale ale.','Brassneck Brewery');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Umlaut!','Hefeweizen',5,5,'Wheaty','Brassneck Brewery');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Red Racer IPA','IPA',80,6.5,'Hops, hops, and more hops! Red Racer IPA has an intense aroma and a long lingering finish.','Central City');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Red Racer Copper Ale','Copper Ale',23,5,'A light bodied, easy drinking ale with a pleasantly dry finish. Toasted Munich malt gives this beer both it''s colour and malty flavour that is balanced with subtle hop aromatics.','Central City');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Red Racer Pale Ale','Pale Ale',50,5,'This big bold pale ale has a citrus aroma and a full malt body giving it a distinctive character designed to inspire the beer connoisseur in all of us.','Central City');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Red Racer Stout','Stout',55,4.2,'Red Racer Oatmeal Stout is deep, dark, smooth, and creamy. The use of oatmeal in the mashtun adds silkiness to the texture and body of this ale.','Central City');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Red Racer Belgian Style Wheat Ale','Wheat Beer',16,5,'Traditionally cloudy, our Belgian style Red Racer White Ale is brewed with coriander seed and dried orange peel producing a beer light in body with a refreshing citrus-spice aroma.','Central City');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('Island Lager','Lager',19,5,'Light and malty with hints of grassy hop aromas and a crisp hoppy bitterness.','Granville Island Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('English Bay Pale Ale','Pale Ale',18,5,'Caramel malt flavour and light hop bitterness, balanced with a dry roasted finish.','Granville Island Brewing');

insert into BeerInfo(BName,	BType, IBU, ABV, Description, BreweryName)
VALUES ('West Coast Pale Ale','Pale Ale',38,5.5,'Upfront bitter hops balance with a malt base, finishing slightly fruity with notes of tangerine and pineapple.','Granville Island Brewing');

-- Beers in stock

insert into BeerInStock
values('English Bay Pale Ale',2);

insert into BeerInStock
values('West Coast Pale Ale',2);

insert into BeerInStock
values('Island Lager',2);

insert into BeerInStock
values('Red Racer Belgian Style Wheat Ale',6);

insert into BeerInStock
values('Red Racer Stout',6);

insert into BeerInStock
values('Red Racer Copper Ale',6);

insert into BeerInStock
values('Red Racer IPA',6);

insert into BeerInStock
    select bname,4
    from beerinfo
    where breweryname like "%brass%";


insert into beerinstock(bname,storeid)
    select bname, 1
    from beerinfo;

insert into beerinstock(bname,storeid)
    select bname, 3
    from beerinfo;

insert into beerinstock(bname,storeid)
    select bname,5
    from beerinfo
    where bname like "%ale%";

-- delete from beerinstock
-- where storeid = 3 and bname like "%doan%";

-- delete from beerinstock
-- where storeid = 1 and bname like "%doan%";


-- Customers

insert into customer(CName,CPassword) values('Jim','theendofthings2');

insert into customer(CName,CPassword) values('Alvin','LimOfAllLims');

insert into customer(CName,CPassword) values('Wes','notInTheCountryIllegally');

insert into customer(CName,CPassword) values('Renee','engineerd');

insert into customer(CName,CPassword) values('Paul','worstLaptopEva!');




-- Ratings
INSERT into rates values(2,'Gypsy Tears', 2, 'Great!');
INsert into rates values(1,'Gypsy Tears', 3, 'Suboptimal');
INSERT into rates values(2,'Watermelon Witbier', 3, 'Bad');
INSERT into rates values(1,'Jerkface 9000', 5, 'Excellent');
INSERT into rates values(2,'Jerkface 9000', 4, 'Good');
