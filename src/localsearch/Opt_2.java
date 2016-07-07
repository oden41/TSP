package localsearch;

import java.util.ArrayList;
import java.util.Random;

import path.TravelingPath;

public class Opt_2 {
	private final int K = 50;
	private int i;
	private final ArrayList<Integer> HIndex;
	private final Random random;
	private final TravelingPath path;

	private int step;
	int cityA = 0;
	int cityB = 0;

	public Opt_2(int noOfCities, TravelingPath path) {
		HIndex = new ArrayList<>();
		for (int i = 0; i < noOfCities; i++) {
			HIndex.add(i);
		}
		random = new Random();
		this.path = path;
		step = 2;
	}

	public void doOneIterator() {
		while (true) {
			switch (step) {
			case 2:
				i = 0;
				cityA = HIndex.get(random.nextInt(HIndex.size()));
				step++;
			case 3:
				if (i == 0)
					cityB = path.nextCity(cityA);
				else if (i == 1)
					cityB = path.prevCity(cityA);
				step++;
			case 4:
				for (int i = 0; i < K; i++) {

				}
			case 5:
				i++;
				if (i == 1) {
					step = 3;
					break;
				}
				else if (i == 2)
					HIndex.remove(cityA);
			case 6:
				if (HIndex.isEmpty())
					return;
				step = 2;
			}
		}
	}
}
