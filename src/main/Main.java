package main;

import data.CitiesData;
import localsearch.Opt_2;
import path.TravelingPath;

public class Main {

	public Main() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {
		int noOfCities = 100000;
		CitiesData data = new CitiesData("mona-lisa100K", noOfCities);
		TravelingPath path = new TravelingPath(noOfCities);
		path.init();
		Opt_2 opt2 = new Opt_2(noOfCities, path);
		long start = System.currentTimeMillis();

		opt2.doOneIterator();

		long end = System.currentTimeMillis();

		System.out.println((end - start)  + "ms");
		System.out.println(path.getTourLength());
	}

}
