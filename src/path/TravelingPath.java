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
		return next > path.length - 1 ? path[0] : path[next];
	}

	public void filpPath(int a,int b,int c,int d) {
	    //端点処理
	    if(cityToIndex[a] == 0 && cityToIndex[b] == path.length - 1 || cityToIndex[c] == 0 && cityToIndex[d] == path.length - 1){
		int temp = a;
		a = b;
		b = temp;
		temp = c;
		c = d;
		d = temp;

		int i = 100000;
		int j = 100000;
		if(cityToIndex[b] == 0){
		    if(cityToIndex[c] - cityToIndex[b] <= path.length / 2){
			i = cityToIndex[b];
			j = cityToIndex[c];
		    }
		    else{
			i = cityToIndex[d];
			j = cityToIndex[a];
		    }
		}
		else if(cityToIndex[d] == 0){
		    if(cityToIndex[a] - cityToIndex[d] <= path.length / 2){
			i = cityToIndex[d];
			j = cityToIndex[a];
		    }
		    else{
			i = cityToIndex[b];
			j = cityToIndex[c];
		    }
		}

		while (i < j) {
		    swap(i, j);
		    i++;
		    j--;
		}
		return;
	    }

	    //index:a < b < c < dにするための処理
	    if(cityToIndex[a] > cityToIndex[b]){
		int temp = a;
		a = b;
		b = temp;
		temp = c;
		c = d;
		d = temp;
	    }
	    if(cityToIndex[a] > cityToIndex[c]){
		int temp = a;
		a = c;
		c = temp;
		temp  = b;
		b = d;
		d = temp;
	    }

	    if(cityToIndex[c] - cityToIndex[a] <= path.length / 2 ){
		//内
		int i = cityToIndex[b];
		int j = cityToIndex[c];
		while (i < j) {
		    swap(i, j);
		    i++;
		    j--;
		}
	    }
	    else{
		//外
		int i = cityToIndex[a];
		int j = cityToIndex[d];
		while (j - i != 1 && j != i) {
		    swap(i, j);
		    i--;
		    j++;
		    if(i < 0)
			i = path.length - 1;
		    if(j > path.length - 1)
			j = 0;
		}
	    }
	}

	private void calcTourLength() {
		long distance = 0;
		for (int i = 0; i < path.length - 1; i++) {
			distance += CitiesData.calcDistance(path[i], path[i + 1]);
		}
		tourLength = distance + CitiesData.calcDistance(path[0], path[path.length - 1]);
	}

	public final long getTourLength() {
	    calcTourLength();
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