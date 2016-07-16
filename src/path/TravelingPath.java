package path;

import java.util.Random;

import data.CitiesData;

public class TravelingPath {
    private int[] path;
    private final int noOfCities;
    private double tourLength;

    private Random random;

    // 2LevelTree用
    private final int[] segmentIDFromCityID;
    private final int[] indexInSegmentFromCityID;
    private final int[][] se;
    private final int[] segmentTourFromSegID;
    private final int[] segIDFromSegIndex;
    private final boolean[] RevFlagFromSegID;

    public TravelingPath(int noOfCities) {
	this.noOfCities = noOfCities;
	tourLength = 0;
	random = new Random();
	path = new int[noOfCities];

	segmentIDFromCityID = new int[noOfCities];
	indexInSegmentFromCityID = new int[noOfCities];
	segmentTourFromSegID = new int[(int) Math.sqrt(noOfCities)];
	segIDFromSegIndex = new int[(int) Math.sqrt(noOfCities)];
	// [ID][element]
	se = new int[(int) Math.sqrt(noOfCities)][];
	RevFlagFromSegID = new boolean[(int) Math.sqrt(noOfCities)];
    }

    public void init() {
	for (int i = 0; i < path.length; i++) {
	    path[i] = i;
	}
	shuffle();

	int count = 0;
	for (int i = 0; i < se.length; i++) {
	    segmentTourFromSegID[i] = i;
	    segIDFromSegIndex[i] = i;

	    if (i == se.length - 1) {
		se[i] = new int[path.length - count];
		for (int j = 0; j < se[i].length; j++) {
		    se[i][j] = path[count];
		    segmentIDFromCityID[path[count]] = i;
		    indexInSegmentFromCityID[path[count]] = j;
		    count++;
		}
		break;
	    }

	    RevFlagFromSegID[i] = false;
	    se[i] = new int[(int) Math.sqrt(noOfCities)];
	    for (int j = 0; j < se[i].length; j++) {
		se[i][j] = path[count];
		segmentIDFromCityID[path[count]] = i;
		indexInSegmentFromCityID[path[count]] = j;
		count++;
	    }
	}
    }

    public int prevCity(int city) {
	int segID = segmentIDFromCityID[city];
	int segIndex = segmentTourFromSegID[segID];
	int indexInSegment = indexInSegmentFromCityID[city];

	if (RevFlagFromSegID[segID]) {
	    // 当該Segmentが反転
	    if (indexInSegment == se[segID].length - 1) {
		segIndex--;
		if (segIndex == -1)
		    segIndex = se.length - 1;
		int id = segIDFromSegIndex[segIndex];
		return RevFlagFromSegID[id] ? se[id][0] : se[id][se[id].length - 1];
	    } else {
		indexInSegment++;
		return se[segID][indexInSegment];
	    }
	} else {
	    // 通常
	    if (indexInSegment == 0) {
		segIndex--;
		if (segIndex == -1)
		    segIndex = se.length - 1;
		return RevFlagFromSegID[segID] ? se[segID][0] : se[segID][se[segID].length - 1];
	    } else {
		indexInSegment--;
		return se[segID][indexInSegment];
	    }
	}
    }

    public int nextCity(int city) {
	int segID = segmentIDFromCityID[city];
	int segIndex = segmentTourFromSegID[segID];
	int indexInSegment = indexInSegmentFromCityID[city];

	if (RevFlagFromSegID[segID]) {
	    // 当該Segmentが反転
	    if (indexInSegment == 0) {
		segIndex++;
		if (segIndex == se.length)
		    segIndex = 0;
		int id = segIDFromSegIndex[segIndex];
		return RevFlagFromSegID[id] ? se[id][se[id].length - 1] : se[id][0];
	    } else {
		indexInSegment--;
		return se[segID][indexInSegment];
	    }
	} else {
	    // 通常
	    if (indexInSegment == se[segID].length - 1) {
		segIndex++;
		if (segIndex == se.length)
		    segIndex = 0;
		int id = segIDFromSegIndex[segIndex];
		return RevFlagFromSegID[id] ? se[id][se[id].length - 1] : se[id][0];
	    } else {
		indexInSegment++;
		return se[segID][indexInSegment];
	    }
	}
    }

    public void filpPath(int a, int b, int c, int d, int i) {
	// 交換範囲を決定
	int start, end;
	if (i == 0) {
	    // a,b
	    if (segmentTourFromSegID[segmentIDFromCityID[a]] < segmentTourFromSegID[segmentIDFromCityID[c]]
		    || segmentIDFromCityID[a] == segmentIDFromCityID[c]
			    && (indexInSegmentFromCityID[a] < indexInSegmentFromCityID[c]
				    && RevFlagFromSegID[segmentIDFromCityID[a]] == false
				    || indexInSegmentFromCityID[a] > indexInSegmentFromCityID[c]
					    && RevFlagFromSegID[segmentIDFromCityID[a]] == true)) {
		// a,b,c,d
		start = b;
		end = c;
	    } else {
		// c,d,a,b
		start = d;
		end = a;
	    }
	} else {
	    // b,a
	    if (segmentTourFromSegID[segmentIDFromCityID[a]] < segmentTourFromSegID[segmentIDFromCityID[c]]
		    || segmentIDFromCityID[a] == segmentIDFromCityID[c]
			    && (indexInSegmentFromCityID[a] < indexInSegmentFromCityID[c]
				    && RevFlagFromSegID[segmentIDFromCityID[a]] == false
				    || indexInSegmentFromCityID[a] > indexInSegmentFromCityID[c]
					    && RevFlagFromSegID[segmentIDFromCityID[a]] == true)) {
		// b,a,d,c
		start = a;
		end = d;
	    } else {
		// d,c,b,a
		start = c;
		end = b;
	    }
	}

	if (segmentIDFromCityID[start] == segmentIDFromCityID[end]) {

	} else {
	    if ((indexInSegmentFromCityID[start] == 0 && !RevFlagFromSegID[segmentIDFromCityID[start]]
		    || indexInSegmentFromCityID[start] == se[segmentIDFromCityID[start]].length - 1
			    && RevFlagFromSegID[segmentIDFromCityID[start]])
		    && (indexInSegmentFromCityID[end] == se[segmentIDFromCityID[end]].length - 1
			    && !RevFlagFromSegID[segmentIDFromCityID[end]]
			    || indexInSegmentFromCityID[end] == 0 && RevFlagFromSegID[segmentIDFromCityID[end]])) {
		// 分割する必要ないパターン
		int startIndex = segmentTourFromSegID[segmentIDFromCityID[start]];
		int endIndex = segmentTourFromSegID[segmentIDFromCityID[end]];
		while (startIndex < endIndex) {
		    swapSegment(startIndex, endIndex);
		    RevFlagFromSegID[segIDFromSegIndex[startIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startIndex]];
		    RevFlagFromSegID[segIDFromSegIndex[endIndex]] = !RevFlagFromSegID[segIDFromSegIndex[endIndex]];

		    startIndex++;
		    endIndex++;
		}
		if (startIndex == endIndex)
		    RevFlagFromSegID[segIDFromSegIndex[startIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startIndex]];

	    } else {
		// 分割必要あり
	    }
	}
    }

    private void calcTourLength() {
	int[] tour = new int[noOfCities];
	int count = 0;
	for (int i = 0; i < se.length; i++) {
	    if (RevFlagFromSegID[i]) {
		for (int j = se[i].length - 1; j >= 0; j--) {
		    tour[count] = se[i][j];
		    count++;
		}
	    } else {
		for (int j = 0; j < se[i].length; j++) {
		    tour[count] = se[i][j];
		    count++;
		}
	    }
	}
	double distance = 0;
	for (int i = 0; i < tour.length - 1; i++) {
	    distance += CitiesData.calcDistance(tour[i], tour[i + 1]);
	}
	tourLength = distance + CitiesData.calcDistance(tour[0], tour[tour.length - 1]);
    }

    public final double getTourLength() {
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
    }

    private final void swapSegment(int index1, int index2) {
    }

    public static void main(String[] args) {
	while (true) {
	    TravelingPath path = new TravelingPath(100);
	    path.init();
	    path.RevFlagFromSegID[0] = true;
	    path.RevFlagFromSegID[2] = true;
	    path.filpPath(0, path.prevCity(0), 1, path.prevCity(1), 1);
	}
    }
}