package path;

import java.util.Random;

import data.CitiesData;

public class TravelingPath {
    private int[] path;
    private final int noOfCities;
    private double tourLength;

    private Random random;

    // 2LevelTree用
    private final int[] segmentIndexFromCityID;
    private final int[] indexInSegmentFromCityID;
    private final int[][] Segment;
    private final boolean[] RevFlagFromSegIndex;

    public TravelingPath(int noOfCities) {
	this.noOfCities = noOfCities;
	tourLength = 0;
	random = new Random();
	path = new int[noOfCities];

	segmentIndexFromCityID = new int[noOfCities];
	indexInSegmentFromCityID = new int[noOfCities];
	Segment = new int[(int) Math.sqrt(noOfCities)][];
	RevFlagFromSegIndex = new boolean[(int) Math.sqrt(noOfCities)];
    }

    public void init() {
	for (int i = 0; i < path.length; i++) {
	    path[i] = i;
	}
	shuffle();

	int count = 0;
	for (int i = 0; i < Segment.length; i++) {
	    if (i == Segment.length - 1) {
		Segment[i] = new int[path.length - count];
		for (int j = 0; j < Segment[i].length; j++) {
		    Segment[i][j] = path[count];
		    segmentIndexFromCityID[path[count]] = i;
		    indexInSegmentFromCityID[path[count]] = j;
		    count++;
		}
		break;
	    }

	    RevFlagFromSegIndex[i] = false;
	    Segment[i] = new int[(int) Math.sqrt(noOfCities)];
	    for (int j = 0; j < Segment[i].length; j++) {
		Segment[i][j] = path[count];
		segmentIndexFromCityID[path[count]] = i;
		indexInSegmentFromCityID[path[count]] = j;
		count++;
	    }
	}
    }

    public int prevCity(int city) {
	int segIndex = segmentIndexFromCityID[city];
	int indexInSegment = indexInSegmentFromCityID[city];

	if (RevFlagFromSegIndex[segIndex]) {
	    // 当該Segmentが反転
	    if (indexInSegment == Segment[segIndex].length - 1) {
		segIndex--;
		if (segIndex == -1)
		    segIndex = Segment.length - 1;
		return RevFlagFromSegIndex[segIndex] ? Segment[segIndex][0]
			: Segment[segIndex][Segment[segIndex].length - 1];
	    } else {
		indexInSegment++;
		return Segment[segIndex][indexInSegment];
	    }
	} else {
	    // 通常
	    if (indexInSegment == 0) {
		segIndex--;
		if (segIndex == -1)
		    segIndex = Segment.length - 1;
		return RevFlagFromSegIndex[segIndex] ? Segment[segIndex][0]
			: Segment[segIndex][Segment[segIndex].length - 1];
	    } else {
		indexInSegment--;
		return Segment[segIndex][indexInSegment];
	    }
	}
    }

    public int nextCity(int city) {
	int segIndex = segmentIndexFromCityID[city];
	int indexInSegment = indexInSegmentFromCityID[city];

	if (RevFlagFromSegIndex[segIndex]) {
	    // 当該Segmentが反転
	    if (indexInSegment == 0) {
		segIndex++;
		if (segIndex == Segment.length)
		    segIndex = 0;
		return RevFlagFromSegIndex[segIndex] ? Segment[segIndex][Segment[segIndex].length - 1]
			: Segment[segIndex][0];
	    } else {
		indexInSegment--;
		return Segment[segIndex][indexInSegment];
	    }
	} else {
	    // 通常
	    if (indexInSegment == Segment[segIndex].length - 1) {
		segIndex++;
		if (segIndex == Segment.length)
		    segIndex = 0;
		return RevFlagFromSegIndex[segIndex] ? Segment[segIndex][Segment[segIndex].length - 1]
			: Segment[segIndex][0];
	    } else {
		indexInSegment++;
		return Segment[segIndex][indexInSegment];
	    }
	}
    }

    public void filpPath(int a, int b, int c, int d) {
    }

    private void calcTourLength() {
	int[] tour = new int[noOfCities];
	int count = 0;
	for (int i = 0; i < Segment.length; i++) {
	    if (RevFlagFromSegIndex[i]) {
		for (int j = Segment[i].length - 1; j >= 0; j--) {
		    tour[count] = Segment[i][j];
		    count++;
		}
	    } else {
		for (int j = 0; j < Segment[i].length; j++) {
		    tour[count] = Segment[i][j];
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

    public static void main(String[] args) {
	TravelingPath path = new TravelingPath(10);
	path.init();
	path.RevFlagFromSegIndex[0] = true;
	path.RevFlagFromSegIndex[2] = true;
	path.calcTourLength();
	System.out.println();
    }
}