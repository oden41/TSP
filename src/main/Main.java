package main;

import localsearch.Opt_2;
import path.TravelingPath;
import data.CitiesData;

public class Main {

	public Main() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {
		int noOfCities = 4663;
		CitiesData data = new CitiesData("./data/ca4663.tsp", noOfCities);
		TravelingPath path = new TravelingPath(noOfCities);
		path.init();
		Opt_2 opt2 = new Opt_2(noOfCities, path);
		opt2.doOneIterator();
	}

}
