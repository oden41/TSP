package localsearch;

import java.util.ArrayList;
import java.util.Random;

import data.CitiesData;
import path.TravelingPath;

public class Opt_2 {
	private final int K = 50;
	private int i;
	private final ArrayList<Integer> HIndex;
	private final Random random;
	private final TravelingPath path;

	private int step;
	int cityA = 1000000000;
	int cityB = 1000000000;
	int cityC = 1000000000;
	int cityD = 1000000000;

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
			case 3:
				if (i == 0)
					cityB = path.nextCity(cityA);
				else if (i == 1)
					cityB = path.prevCity(cityA);
			case 4:
			    	boolean flag = false;
				for (int j = 0; j < K; j++) {
				    //a
				    cityC = CitiesData.getKneighbor(j, cityA);
				    //b
				    if(CitiesData.calcDistance(cityA, cityC) >= CitiesData.calcDistance(cityA, cityB)){
					step = 5;
					break;
				    }
				    //c
				    if (i == 0)
					cityD = path.nextCity(cityC);
				    else if (i == 1)
					cityD = path.prevCity(cityC);
				    //d
				    if(CitiesData.calcDistance(cityA, cityC) + CitiesData.calcDistance(cityB, cityD) < CitiesData.calcDistance(cityA, cityB) + CitiesData.calcDistance(cityC, cityD)){
					int subDist = CitiesData.calcDistance(cityA, cityC) + CitiesData.calcDistance(cityB, cityD) - CitiesData.calcDistance(cityA, cityB) - CitiesData.calcDistance(cityC, cityD);
					if(subDist > 0)
					    System.out.println(subDist);
					path.addTourLength(subDist);
					path.filpPath(cityA, cityB, cityC, cityD);
					if(!HIndex.contains(cityB))
					    HIndex.add(cityB);
					if(!HIndex.contains(cityC))
					    HIndex.add(cityC);
					if(!HIndex.contains(cityD))
					    HIndex.add(cityD);
					ArrayList<Integer> cand = CitiesData.getRevNeighbor(j, cityA);
					cand.forEach(e ->{
					if(!HIndex.contains(e))
					    HIndex.add(e);
					});
					cand = CitiesData.getRevNeighbor(j, cityB);
					cand.forEach(e ->{
					if(!HIndex.contains(e))
					    HIndex.add(e);
					});
					cand = CitiesData.getRevNeighbor(j, cityC);
					cand.forEach(e ->{
					if(!HIndex.contains(e))
					    HIndex.add(e);
					});
					cand = CitiesData.getRevNeighbor(j, cityD);
					cand.forEach(e ->{
					if(!HIndex.contains(e))
					    HIndex.add(e);
					});

					flag = true;
					break;
				    }
				}
				if(flag){
				    step = 2;
				    break;
				}
			case 5:
				i++;
				if (i == 1) {
					step = 3;
					break;
				}
				else if (i == 2){
					HIndex.remove((Object)cityA);
				}
			case 6:
				if (HIndex.isEmpty())
					return;
				step = 2;
				break;
			}
		}
	}
}
