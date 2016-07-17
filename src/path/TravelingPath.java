package path;

import java.util.Random;

import data.CitiesData;

public class TravelingPath {
    private int[] path;
    private int[] segID;
    private final int noOfCities;
    private double tourLength;

    private Random random;

    // 2LevelTree用
    private final int[] segmentIDFromCityID;
    private final int[] indexInSegmentFromCityID;
    private final int[][] TSegment;
    private final int[] segIDFromSegIndex;
    private final int[] segIndexFromSegID;
    private final boolean[] RevFlagFromSegID;

    public TravelingPath(int noOfCities) {
	this.noOfCities = noOfCities;
	tourLength = 0;
	random = new Random();
	path = new int[noOfCities];
	segID = new int[(int) Math.sqrt(noOfCities)];

	segmentIDFromCityID = new int[noOfCities];
	indexInSegmentFromCityID = new int[noOfCities];
	segIDFromSegIndex = new int[(int) Math.sqrt(noOfCities)];
	segIndexFromSegID = new int[(int) Math.sqrt(noOfCities)];
	// [ID][element]
	TSegment = new int[(int) Math.sqrt(noOfCities)][];
	RevFlagFromSegID = new boolean[(int) Math.sqrt(noOfCities)];
    }

    public void init() {
	for (int i = 0; i < path.length; i++) {
	    path[i] = i;
	}
	for (int i = 0; i < TSegment.length; i++) {
	    segID[i] = i;
	}
	shuffle();

	int count = 0;
	for (int i = 0; i < TSegment.length; i++) {
	    segIDFromSegIndex[i] = segID[i];
	    segIndexFromSegID[segID[i]] = i;

	    if (i == TSegment.length - 1) {
		TSegment[i] = new int[path.length - count];
		for (int j = 0; j < TSegment[i].length; j++) {
		    TSegment[i][j] = path[count];
		    segmentIDFromCityID[path[count]] = i;
		    indexInSegmentFromCityID[path[count]] = j;
		    count++;
		}
		break;
	    }

	    RevFlagFromSegID[i] = false;
	    TSegment[i] = new int[(int) Math.sqrt(noOfCities)];
	    for (int j = 0; j < TSegment[i].length; j++) {
		TSegment[i][j] = path[count];
		segmentIDFromCityID[path[count]] = i;
		indexInSegmentFromCityID[path[count]] = j;
		count++;
	    }
	}
    }

    public int prevCity(int city) {
	int segID = segmentIDFromCityID[city];
	int segIndex = segIndexFromSegID[segID];
	int indexInSegment = indexInSegmentFromCityID[city];

	if (RevFlagFromSegID[segID]) {
	    // 当該Segmentが反転
	    if (indexInSegment == TSegment[segID].length - 1) {
		segIndex--;
		if (segIndex == -1)
		    segIndex = TSegment.length - 1;
		int id = segIDFromSegIndex[segIndex];
		return RevFlagFromSegID[id] ? TSegment[id][0] : TSegment[id][TSegment[id].length - 1];
	    } else {
		indexInSegment++;
		return TSegment[segID][indexInSegment];
	    }
	} else {
	    // 通常
	    if (indexInSegment == 0) {
		segIndex--;
		if (segIndex == -1)
		    segIndex = TSegment.length - 1;
		return RevFlagFromSegID[segID] ? TSegment[segID][0] : TSegment[segID][TSegment[segID].length - 1];
	    } else {
		indexInSegment--;
		return TSegment[segID][indexInSegment];
	    }
	}
    }

    public int nextCity(int city) {
	int segID = segmentIDFromCityID[city];
	int segIndex = segIndexFromSegID[segID];
	int indexInSegment = indexInSegmentFromCityID[city];

	if (RevFlagFromSegID[segID]) {
	    // 当該Segmentが反転
	    if (indexInSegment == 0) {
		segIndex++;
		if (segIndex == TSegment.length)
		    segIndex = 0;
		int id = segIDFromSegIndex[segIndex];
		return RevFlagFromSegID[id] ? TSegment[id][TSegment[id].length - 1] : TSegment[id][0];
	    } else {
		indexInSegment--;
		return TSegment[segID][indexInSegment];
	    }
	} else {
	    // 通常
	    if (indexInSegment == TSegment[segID].length - 1) {
		segIndex++;
		if (segIndex == TSegment.length)
		    segIndex = 0;
		int id = segIDFromSegIndex[segIndex];
		return RevFlagFromSegID[id] ? TSegment[id][TSegment[id].length - 1] : TSegment[id][0];
	    } else {
		indexInSegment++;
		return TSegment[segID][indexInSegment];
	    }
	}
    }

    public void filpPath(int a, int b, int c, int d, int i) {
	if (segmentIDFromCityID[b] == segmentIDFromCityID[c]) {
	    return;
	} else if ((indexInSegmentFromCityID[b] == 0 && !RevFlagFromSegID[segmentIDFromCityID[b]]
		|| indexInSegmentFromCityID[b] == TSegment[segmentIDFromCityID[b]].length - 1
			&& RevFlagFromSegID[segmentIDFromCityID[b]])
		&& (indexInSegmentFromCityID[c] == TSegment[segmentIDFromCityID[c]].length - 1
			&& !RevFlagFromSegID[segmentIDFromCityID[c]]
			|| indexInSegmentFromCityID[c] == 0 && RevFlagFromSegID[segmentIDFromCityID[c]])) {

	    int startIndex = segIndexFromSegID[segmentIDFromCityID[b]];
	    int endIndex = segIndexFromSegID[segmentIDFromCityID[c]];
	    int swapCount = 0;
	    if (startIndex == endIndex) {
		RevFlagFromSegID[segIDFromSegIndex[startIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startIndex]];
		return;
	    } else if (startIndex < endIndex) {
		swapCount = (endIndex - startIndex + 1) / 2;
	    } else {
		swapCount = (TSegment.length - (startIndex - 1) + endIndex) / 2;
	    }
	    int i1 = 0;
	    while (i1 < swapCount) {
		RevFlagFromSegID[segIDFromSegIndex[startIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startIndex]];
		RevFlagFromSegID[segIDFromSegIndex[endIndex]] = !RevFlagFromSegID[segIDFromSegIndex[endIndex]];
		swapSegment(startIndex, endIndex);
		i1++;
		startIndex++;
		if (startIndex == TSegment.length)
		    startIndex = 0;
		endIndex--;
		if (endIndex == -1)
		    endIndex = TSegment.length - 1;
	    }
	    if (startIndex == endIndex)
		RevFlagFromSegID[segIDFromSegIndex[startIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startIndex]];
	} else {
	    int startSplitSegIndex = segIndexFromSegID[segmentIDFromCityID[b]],
		    endSplitSegIndex = segIndexFromSegID[segmentIDFromCityID[c]];
	    int[] startSplit, endSplit;
	    // 分割
	    if (!(indexInSegmentFromCityID[b] == 0 && !RevFlagFromSegID[segmentIDFromCityID[b]]
		    || indexInSegmentFromCityID[b] == TSegment[segmentIDFromCityID[b]].length - 1
			    && RevFlagFromSegID[segmentIDFromCityID[b]])) {
		int plot = indexInSegmentFromCityID[b];
		int id = segmentIDFromCityID[b];
		if (RevFlagFromSegID[segmentIDFromCityID[b]]) {
		    startSplit = new int[TSegment[id].length - plot - 1];
		    for (int j = TSegment[id].length - 1; j > plot; j--) {
			startSplit[TSegment[id].length - j - 1] = TSegment[id][j];
		    }
		    int[] newSegment = new int[plot + 1];
		    for (int k = 0; k < plot + 1; k++) {
			newSegment[k] = TSegment[id][k];
			indexInSegmentFromCityID[newSegment[k]] = k;
		    }
		    TSegment[id] = newSegment;
		} else {
		    startSplit = new int[plot];
		    for (int j = 0; j < plot; j++) {
			startSplit[j] = TSegment[id][j];
		    }
		    int[] newSegment = new int[TSegment[id].length - plot];
		    for (int k = plot; k < TSegment[id].length; k++) {
			newSegment[k - plot] = TSegment[id][k];
			indexInSegmentFromCityID[newSegment[k - plot]] = k - plot;
		    }
		    TSegment[id] = newSegment;
		}
		int nop = 0;
	    }
	    if (!(indexInSegmentFromCityID[c] == TSegment[segmentIDFromCityID[c]].length - 1
		    && !RevFlagFromSegID[segmentIDFromCityID[c]]
		    || indexInSegmentFromCityID[c] == 0 && RevFlagFromSegID[segmentIDFromCityID[c]])) {
		int plot = indexInSegmentFromCityID[c];
		int id = segmentIDFromCityID[c];
		if (RevFlagFromSegID[segmentIDFromCityID[c]]) {
		    endSplit = new int[plot];
		    for (int j = endSplit.length - 1; j >= 0; j--) {
			endSplit[endSplit.length - j - 1] = TSegment[id][j];
		    }
		    int[] newSegment = new int[TSegment[id].length - endSplit.length];
		    for (int k = 0; k < newSegment.length; k++) {
			newSegment[k] = TSegment[id][plot + k];
			indexInSegmentFromCityID[newSegment[k]] = k;
		    }
		    TSegment[id] = newSegment;
		} else {
		    endSplit = new int[TSegment[id].length - plot - 1];
		    for (int j = plot + 1; j < TSegment[id].length; j++) {
			endSplit[j - (plot + 1)] = TSegment[id][j];
		    }
		    int[] newSegment = new int[plot + 1];
		    for (int k = 0; k < newSegment.length; k++) {
			newSegment[k] = TSegment[id][k];
			indexInSegmentFromCityID[newSegment[k]] = k;
		    }
		    TSegment[id] = newSegment;
		}
	    }

	    // 反転
	    int swapCount = 0;
	    int temp1 = startSplitSegIndex;
	    int temp2 = endSplitSegIndex;
	    if (startSplitSegIndex < endSplitSegIndex) {
		swapCount = (endSplitSegIndex - startSplitSegIndex + 1) / 2;
	    } else {
		swapCount = (TSegment.length - (startSplitSegIndex - 1) + endSplitSegIndex) / 2;
	    }
	    int i1 = 0;
	    while (i1 < swapCount) {
		RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]];
		RevFlagFromSegID[segIDFromSegIndex[endSplitSegIndex]] = !RevFlagFromSegID[segIDFromSegIndex[endSplitSegIndex]];
		swapSegment(startSplitSegIndex, endSplitSegIndex);
		i1++;
		startSplitSegIndex++;
		if (startSplitSegIndex == TSegment.length)
		    startSplitSegIndex = 0;
		endSplitSegIndex--;
		if (endSplitSegIndex == -1)
		    endSplitSegIndex = TSegment.length - 1;
	    }
	    if (startSplitSegIndex == endSplitSegIndex)
		RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]];

	    // 結合
	    startSplitSegIndex = temp1;
	    endSplitSegIndex = temp2;
	}

    }

    private void calcTourLength() {
	int[] tour = new int[noOfCities];
	int count = 0;
	for (int i = 0; i < TSegment.length; i++) {
	    if (RevFlagFromSegID[i]) {
		for (int j = TSegment[i].length - 1; j >= 0; j--) {
		    tour[count] = TSegment[i][j];
		    count++;
		}
	    } else {
		for (int j = 0; j < TSegment[i].length; j++) {
		    tour[count] = TSegment[i][j];
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
	    swapPath(i, index);
	}
	for (int i = 0; i < TSegment.length; i++) {
	    int index = random.nextInt(TSegment.length - i) + i;
	    swapSegID(i, index);
	}
    }

    public void swapPath(int index1, int index2) {
	int temp = path[index1];
	int temp2 = path[index2];
	path[index1] = temp2;
	path[index2] = temp;
    }

    private void swapSegID(int index1, int index2) {
	int temp = segID[index1];
	segID[index1] = segID[index2];
	segID[index2] = temp;
    }

    private final void swapSegment(int index1, int index2) {
	int temp = segIDFromSegIndex[index1];
	int temp2 = segIDFromSegIndex[index2];
	segIDFromSegIndex[index1] = segIDFromSegIndex[index2];
	segIDFromSegIndex[index2] = temp;

	int tIndex = segIndexFromSegID[temp];
	segIndexFromSegID[temp] = segIndexFromSegID[temp2];
	segIndexFromSegID[temp2] = tIndex;
    }

    public static void main(String[] args) {
	while (true) {
	    TravelingPath path = new TravelingPath(50);
	    path.init();
	    path.RevFlagFromSegID[0] = true;
	    path.RevFlagFromSegID[2] = true;
	    Random random = new Random();
	    path.filpPath(0, path.nextCity(0), 1, path.nextCity(1), 0);
	}
    }
}