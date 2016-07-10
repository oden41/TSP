package localsearch;

import java.util.HashSet;
import java.util.Random;

import data.CitiesData;
import path.TravelingPath;

public class Opt_2 {
	private final int K = 50;
	private int i;
	private final Random random;
	private final TravelingPath path;

	//選択都市リスト
	private int fNoOfCandidates;
	private final boolean[] fIsCandidate;
	private final int[] fCandidates;
	int pos;

	private int step;
	int cityA = 1000000000;
	int cityB = 1000000000;
	int cityC = 1000000000;
	int cityD = 1000000000;

	public Opt_2(int noOfCities, TravelingPath path) {
		fIsCandidate = new boolean[noOfCities];
		fCandidates = new int[noOfCities];
		fNoOfCandidates = noOfCities;
		for (int i = 0; i < noOfCities; i++) {
			fIsCandidate[i] = true;
			fCandidates[i] = i;
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
				pos = random.nextInt(fNoOfCandidates);
				cityA = get(pos);
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
					path.filpPath(cityA, cityB, cityC, cityD);

					if(!isCandidate(cityB))
					    add(cityB);
					if(!isCandidate(cityC))
					    add(cityC);
					if(!isCandidate(cityD))
					    add(cityD);

					HashSet<Integer> cand = CitiesData.getRevNeighbor(j, cityA);
					cand.addAll(CitiesData.getRevNeighbor(j, cityB));
					cand.addAll(CitiesData.getRevNeighbor(j, cityC));
					cand.addAll(CitiesData.getRevNeighbor(j, cityD));
					cand.forEach(e ->{
					    if(!isCandidate(e))
						add(e);
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
					delete(cityA);
				}
			case 6:
				if (fNoOfCandidates == 0)
					return;
				step = 2;
				break;
			}
		}
	}

	private final int get(int index) {
	    return fCandidates[index];
	}

	private final void delete(int city){
	    fIsCandidate[city] = false;
	    fNoOfCandidates--;
	    fCandidates[pos] = fCandidates[fNoOfCandidates];
	}

	private final void add(int city) {
	    fIsCandidate[city] = true;
	    fCandidates[fNoOfCandidates++] = city;
	}

	private final boolean isCandidate(int city) {
	    return fIsCandidate[city];
	}
}
