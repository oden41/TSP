package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CitiesData {

    private double[][] fCitiesPoint;
    private int[] fDistanceMatrix;
    private int noOfCol;

    public CitiesData(String path, int noOfCities) {
	File file = new File(path);
	BufferedReader bReader = null;
	fCitiesPoint = new double[2][noOfCities];
	noOfCol = noOfCities;
	fDistanceMatrix = new int[noOfCities * noOfCities];

	try {
	    bReader = new BufferedReader(new FileReader(file));
	    int i = 0;
	    int j = 0;

	    String string = null;
	    while((string = bReader.readLine()) != null){
		i++;
		if(i < 8)
		    continue;

		if(string.equals("EOF"))
		    break;

		String[] split = string.split(" ");
		fCitiesPoint[0][j] = Double.parseDouble(split[1]);
		fCitiesPoint[1][j] = Double.parseDouble(split[2]);
		j++;
	    }
	    bReader.close();

	    calcDistance();
	} catch (FileNotFoundException e) {

	} catch (IOException e) {
	    // TODO 自動生成された catch ブロック
	    e.printStackTrace();
	}
    }


    private void calcDistance() {
	for (int i = 0; i < noOfCol; i++) {
	    for (int j = 0; j < noOfCol; j++) {
		int distance = 0;
		double xd = 0;
		double yd = 0;

		xd = Math.abs(fCitiesPoint[0][j] - fCitiesPoint[0][i]);
		yd = Math.abs(fCitiesPoint[1][j] - fCitiesPoint[1][i]);

		fDistanceMatrix[i * noOfCol + j] = (int)Math.floor(Math.sqrt(xd * xd + yd * yd) + 0.5);
	    }
	}
    }

    public int get(int row, int col) {
	return fDistanceMatrix[row * noOfCol + col];
    }

}
