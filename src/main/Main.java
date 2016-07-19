package main;

import java.util.ArrayList;

import localsearch.Opt_2;
import path.TravelingPath;
import data.CitiesData;

enum FileType {
	Ca(0), Ja(1), Fi(2), Bm(3), Ch(4), Monaliza(5), ;

	private final int id;

	private FileType(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}

public class Main {

	public Main() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	private final static int[] noOfCitiesList = { 4663, 9847, 10639, 33708, 71009, 100000 };
	private final static String[] cityFileList = { "ca4663", "ja9847", "fi10639", "bm33708", "ch71009", "mona-lisa100K" };

	public static void main(String[] args) {
		int id = FileType.Monaliza.getId();
		int noOfCities = noOfCitiesList[id];
		ArrayList<Long> timeList = new ArrayList<>();
		ArrayList<Double> pathLengthList = new ArrayList<>();

		for (int i = 0; i < 11; i++) {
			CitiesData data = new CitiesData(cityFileList[id], noOfCities);
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
