package path;

import java.util.Random;

import data.CitiesData;

public class TravelingPath {
	private int[] path;
	private final int noOfCities;
	private long tourLength;
	private int[] cityToIndex;

	private Random random;

	public TravelingPath(int noOfCities) {
		path = new int[noOfCities];
		this.noOfCities = noOfCities;
		tourLength = 0;
		cityToIndex = new int[noOfCities];
		random = new Random();
	}

	public void init() {
		for (int i = 0; i < path.length; i++) {
			path[i] = i;
			cityToIndex[i] = i;
		}
		shuffle();
		calcTourLength();
	}

	public int prevCity(int city) {
		int prev = cityToIndex[city] - 1;
		return prev < 0 ? path[path.length - 1] : path[prev];
	}

	public int nextCity(int city) {
		int next = cityToIndex[city] + 1;
		return next > path.length - 1 ? path[path.length - 1] : path[next];
	}

	public void calcTourLength() {
		long distance = 0;
		for (int i = 0; i < path.length - 1; i++) {
			distance += CitiesData.calcDistance(path[i], path[i + 1]);
		}
		tourLength = distance;
	}

	public final long getTourLength() {
		return tourLength;
	}

	public final void addTourLength(int value) {
		tourLength += value;
	}

	public final void shuffle() {
		for (int i = 0; i < noOfCities; i++) {
			int index = random.nextInt(noOfCities - i) + i;
			swap(i, index);
		}
	}

	public void swap(int index1, int index2) {
		int temp = path[index1];
		int temp2 = path[index2];
		path[index1] = temp2;
		path[index2] = temp;

		int tIndex = cityToIndex[temp];
		cityToIndex[temp] = cityToIndex[temp2];
		cityToIndex[temp2] = tIndex;
	}
}