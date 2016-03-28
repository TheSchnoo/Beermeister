package beermeister;

import beermeister.BeerBroker;

public class Main {
	public static void main(String[] args) throws Exception {
		BeerBroker broker = new BeerBroker();
		broker.start();
	}
}