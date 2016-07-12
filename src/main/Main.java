package main;

import java.util.ArrayList;

import localsearch.Opt_2;
import path.TravelingPath;
import data.CitiesData;

public class Main {

	public Main() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {
		int noOfCities = 100000;
		ArrayList<Long> timeList = new ArrayList<>();
		ArrayList<Double> pathLengthList = new ArrayList<>();

		for (int i = 0; i < 11; i++) {
			CitiesData data = new CitiesData("mona-lisa100K", noOfCities);
			TravelingPath path = new TravelingPath(noOfCities);
			path.init();
			Opt_2 opt2 = new Opt_2(noOfCities, path);
			long start = System.currentTimeMillis();

			opt2.doOneIterator();

			long end = System.currentTimeMillis();

			System.out.println((end - start) + "ms");
			System.out.println(path.getTourLength());

			if (i != 0) {
				timeList.add(end - start);
				pathLengthList.add(path.getTourLength());
			}
		}

		System.out.println("-------------");
		double ave_time = timeList.stream().mapToLong(e -> e).average().getAsDouble();
		double ave_length = pathLengthList.stream().mapToDouble(e -> e).average().getAsDouble();
		double sd_time = Math.sqrt(timeList.stream().mapToDouble(e -> (e - ave_time) * (e - ave_time)).average().getAsDouble());
		double sd_length = Math.sqrt(pathLengthList.stream().mapToDouble(e -> (e - ave_length) * (e - ave_length)).average().getAsDouble());
		System.out.println("time:" + ave_time + "(" + sd_time + ")");
		System.out.println("length:" + ave_length + "(" + sd_length + ")");
	}

}
