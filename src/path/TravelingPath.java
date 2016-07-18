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
		segID = segIDFromSegIndex[segIndex];
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
	// 交換範囲を決定
	int start, end;
	if (i == 0) {
	    // a,b
	    if (segIndexFromSegID[segmentIDFromCityID[b]] < segIndexFromSegID[segmentIDFromCityID[c]]
		    || segmentIDFromCityID[b] == segmentIDFromCityID[c]
			    && (indexInSegmentFromCityID[b] < indexInSegmentFromCityID[c]
				    && RevFlagFromSegID[segmentIDFromCityID[b]] == false
				    || indexInSegmentFromCityID[b] > indexInSegmentFromCityID[c]
					    && RevFlagFromSegID[segmentIDFromCityID[b]] == true)) {
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
	    if (segIndexFromSegID[segmentIDFromCityID[a]] < segIndexFromSegID[segmentIDFromCityID[d]]
		    || segmentIDFromCityID[a] == segmentIDFromCityID[d]
			    && (indexInSegmentFromCityID[a] < indexInSegmentFromCityID[d]
				    && RevFlagFromSegID[segmentIDFromCityID[a]] == false
				    || indexInSegmentFromCityID[a] > indexInSegmentFromCityID[d]
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
	    // 反転経路が同一Segment
	    int id = segmentIDFromCityID[start];
	    int startIndex = indexInSegmentFromCityID[start];
	    int endIndex = indexInSegmentFromCityID[end];
	    if (startIndex > endIndex) {
		int temp = startIndex;
		startIndex = endIndex;
		endIndex = temp;
	    }
	    int swapCount = 0;
	    swapCount = (endIndex - startIndex + 1) / 2;
	    int i1 = 0;
	    int temp;
	    while (i1 < swapCount) {
		temp = TSegment[id][startIndex];
		TSegment[id][startIndex] = TSegment[id][endIndex];
		TSegment[id][endIndex] = temp;

		temp = indexInSegmentFromCityID[TSegment[id][startIndex]];
		indexInSegmentFromCityID[TSegment[id][startIndex]] = indexInSegmentFromCityID[TSegment[id][endIndex]];
		indexInSegmentFromCityID[TSegment[id][endIndex]] = temp;

		i1++;
		startIndex++;
		endIndex--;
	    }
	    return;
	}
	// else if ((indexInSegmentFromCityID[start] == 0 &&
	// !RevFlagFromSegID[segmentIDFromCityID[start]]
	// || indexInSegmentFromCityID[start] ==
	// TSegment[segmentIDFromCityID[start]].length - 1
	// && RevFlagFromSegID[segmentIDFromCityID[start]])
	// && (indexInSegmentFromCityID[end] ==
	// TSegment[segmentIDFromCityID[end]].length - 1
	// && !RevFlagFromSegID[segmentIDFromCityID[end]]
	// || indexInSegmentFromCityID[end] == 0 &&
	// RevFlagFromSegID[segmentIDFromCityID[end]])) {
	// // 分割必要なし
	//
	// int startIndex = segIndexFromSegID[segmentIDFromCityID[start]];
	// int endIndex = segIndexFromSegID[segmentIDFromCityID[end]];
	// int swapCount = 0;
	// if (startIndex == endIndex) {
	// RevFlagFromSegID[segIDFromSegIndex[startIndex]] =
	// !RevFlagFromSegID[segIDFromSegIndex[startIndex]];
	// return;
	// } else if (startIndex < endIndex) {
	// swapCount = (endIndex - startIndex + 1) / 2;
	// } else {
	// swapCount = (TSegment.length - (startIndex - 1) + endIndex) / 2;
	// }
	// int i1 = 0;
	// while (i1 < swapCount) {
	// RevFlagFromSegID[segIDFromSegIndex[startIndex]] =
	// !RevFlagFromSegID[segIDFromSegIndex[startIndex]];
	// RevFlagFromSegID[segIDFromSegIndex[endIndex]] =
	// !RevFlagFromSegID[segIDFromSegIndex[endIndex]];
	// swapSegment(startIndex, endIndex);
	// i1++;
	// startIndex++;
	// endIndex--;
	// }
	// if (startIndex == endIndex)
	// RevFlagFromSegID[segIDFromSegIndex[startIndex]] =
	// !RevFlagFromSegID[segIDFromSegIndex[startIndex]];
	// }
	else {
	    int startSplitSegIndex = segIndexFromSegID[segmentIDFromCityID[start]],
		    endSplitSegIndex = segIndexFromSegID[segmentIDFromCityID[end]];
	    int[] startSplit = null, endSplit = null;
	    // 分割
	    if (!(indexInSegmentFromCityID[start] == 0 && !RevFlagFromSegID[segmentIDFromCityID[start]]
		    || indexInSegmentFromCityID[start] == TSegment[segmentIDFromCityID[start]].length - 1
			    && RevFlagFromSegID[segmentIDFromCityID[start]])) {
		int plot = indexInSegmentFromCityID[start];
		int id = segmentIDFromCityID[start];
		if (RevFlagFromSegID[segmentIDFromCityID[start]]) {
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
	    }
	    if (!(indexInSegmentFromCityID[end] == TSegment[segmentIDFromCityID[end]].length - 1
		    && !RevFlagFromSegID[segmentIDFromCityID[end]]
		    || indexInSegmentFromCityID[end] == 0 && RevFlagFromSegID[segmentIDFromCityID[end]])) {
		int plot = indexInSegmentFromCityID[end];
		int id = segmentIDFromCityID[end];
		if (RevFlagFromSegID[segmentIDFromCityID[end]]) {
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
	    swapCount = (endSplitSegIndex - startSplitSegIndex + 1) / 2;

	    assert swapCount >= 0;

	    int i1 = 0;
	    while (i1 < swapCount) {
		RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]];
		RevFlagFromSegID[segIDFromSegIndex[endSplitSegIndex]] = !RevFlagFromSegID[segIDFromSegIndex[endSplitSegIndex]];
		swapSegment(startSplitSegIndex, endSplitSegIndex);
		i1++;
		startSplitSegIndex++;
		endSplitSegIndex--;
	    }
	    if (startSplitSegIndex == endSplitSegIndex)
		RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]] = !RevFlagFromSegID[segIDFromSegIndex[startSplitSegIndex]];

	    // 結合
	    startSplitSegIndex = temp1;
	    endSplitSegIndex = temp2;
	    if (startSplit != null) {
		int comp = startSplitSegIndex == 0 ? segIDFromSegIndex.length - 1 : startSplitSegIndex - 1;
		if (TSegment[segIDFromSegIndex[comp]].length < TSegment[segIDFromSegIndex[startSplitSegIndex]].length) {
		    // 1つ前につける→Segmentの後ろに追加
		    combineNext(segIDFromSegIndex[comp], startSplit);
		} else {
		    // 1つ後ろにつける→Segmentの前に追加
		    combinePrev(segIDFromSegIndex[startSplitSegIndex], startSplit);
		}
	    }

	    if (endSplit != null) {
		int comp = endSplitSegIndex == segIDFromSegIndex.length - 1 ? 0 : endSplitSegIndex + 1;
		if (TSegment[segIDFromSegIndex[comp]].length < TSegment[segIDFromSegIndex[endSplitSegIndex]].length) {
		    // 前追加
		    combinePrev(segIDFromSegIndex[comp], endSplit);
		} else {
		    // 後追加
		    combineNext(segIDFromSegIndex[endSplitSegIndex], endSplit);
		}
	    }

	}

    }

    private void calcTourLength() {
	int[] tour = new int[noOfCities];
	int count = 0;
	for (int i = 0; i < TSegment.length; i++) {
	    if (RevFlagFromSegID[segIDFromSegIndex[i]]) {
		for (int j = TSegment[segIDFromSegIndex[i]].length - 1; j >= 0; j--) {
		    tour[count] = TSegment[segIDFromSegIndex[i]][j];
		    count++;
		}
	    } else {
		for (int j = 0; j < TSegment[segIDFromSegIndex[i]].length; j++) {
		    tour[count] = TSegment[segIDFromSegIndex[i]][j];
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
	segIDFromSegIndex[index1] = temp2;
	segIDFromSegIndex[index2] = temp;

	int tIndex = segIndexFromSegID[temp];
	segIndexFromSegID[temp] = segIndexFromSegID[temp2];
	segIndexFromSegID[temp2] = tIndex;
    }

    /**
     * id番目のSegmentにelementsを結合
     *
     * @param id
     * @param elements
     */
    private final void combineNext(int id, int[] elements) {
	int[] newSegment = new int[elements.length + TSegment[id].length];
	if (RevFlagFromSegID[id]) {
	    for (int i = 0; i < elements.length; i++) {
		newSegment[i] = elements[elements.length - i - 1];
		indexInSegmentFromCityID[newSegment[i]] = i;
		segmentIDFromCityID[newSegment[i]] = id;
	    }
	    for (int i = 0; i < TSegment[id].length; i++) {
		newSegment[i + elements.length] = TSegment[id][i];
		indexInSegmentFromCityID[newSegment[i + elements.length]] = i + elements.length;
	    }

	} else {
	    for (int i = 0; i < TSegment[id].length; i++) {
		newSegment[i] = TSegment[id][i];
		indexInSegmentFromCityID[newSegment[i]] = i;
	    }
	    for (int i = 0; i < elements.length; i++) {
		newSegment[i + TSegment[id].length] = elements[i];
		indexInSegmentFromCityID[newSegment[i + TSegment[id].length]] = i + TSegment[id].length;
		segmentIDFromCityID[newSegment[i + TSegment[id].length]] = id;
	    }
	}
	TSegment[id] = newSegment;
    }

    private final void combinePrev(int id, int[] elements) {
	int[] newSegment = new int[elements.length + TSegment[id].length];
	if (RevFlagFromSegID[id]) {
	    for (int i = 0; i < TSegment[id].length; i++) {
		newSegment[i] = TSegment[id][i];
		indexInSegmentFromCityID[newSegment[i]] = i;
	    }
	    for (int i = 0; i < elements.length; i++) {
		newSegment[i + TSegment[id].length] = elements[elements.length - i - 1];
		indexInSegmentFromCityID[newSegment[i + TSegment[id].length]] = i + TSegment[id].length;
		segmentIDFromCityID[newSegment[i + TSegment[id].length]] = id;
	    }
	} else {
	    for (int i = 0; i < elements.length; i++) {
		newSegment[i] = elements[i];
		indexInSegmentFromCityID[newSegment[i]] = i;
		segmentIDFromCityID[newSegment[i]] = id;
	    }
	    for (int i = 0; i < TSegment[id].length; i++) {
		newSegment[i + elements.length] = TSegment[id][i];
		indexInSegmentFromCityID[newSegment[i + elements.length]] = i + elements.length;
	    }
	}
	TSegment[id] = newSegment;
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