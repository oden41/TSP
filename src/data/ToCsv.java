package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ToCsv {

    static double[][] datalist;

    public ToCsv(String path, int noOfCities) {
	File file = new File(path);
	BufferedReader bReader = null;
	datalist = new double[noOfCities][3 + noOfCities];
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
		datalist[j][0] = Double.parseDouble(split[0]);
		datalist[j][1] = Double.parseDouble(split[1]);
		datalist[j][2] = Double.parseDouble(split[2]);
		j++;
	    }

	    bReader.close();
	} catch (FileNotFoundException e) {

	} catch (IOException e) {
	    // TODO 自動生成された catch ブロック
	    e.printStackTrace();
	}
    }

    private static void calcDistance(){
	for (int i = 0; i < datalist.length; i++) {
	    for (int j = 3; j < datalist[0].length; j++) {
		int distance = 0;
		double xd = 0;
		double yd = 0;

		xd = Math.abs(datalist[j - 3][1] - datalist[i][1]);
		yd = Math.abs(datalist[j - 3][2] - datalist[i][2]);

		datalist[i][j] = Math.floor(Math.sqrt(xd * xd + yd * yd) + 0.5);
	    }
	}
    }

    private static void outputFile(String fileName) {
	try {
	    PrintWriter bw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

	    for (int i = 0; i < datalist.length; i++) {
		if(i == datalist.length - 1)
		    System.out.println();
		String string = concat(',', i, 3);
		bw.println(string);
	    }
//		bw.write(concat(',', datalist.length - 1, 3));
//		bw.newLine();
	    bw.close();
	} catch (IOException e) {
	    // TODO 自動生成された catch ブロック
	    e.printStackTrace();
	}
    }

    private static String concat(char delim,int index,  int startIndex) {
	StringBuilder builder = new StringBuilder();
	for (int i = startIndex; i < datalist[index].length; i++) {
	    builder.append(String.valueOf(datalist[index][i]));
	    if(i != datalist[index].length - 1)
		builder.append(delim);
	}

	return builder.toString();
    }


    public static void main(String[] args) {
//	ToCsv csv = new ToCsv("./data/ca4663.tsp", 4663);
//	calcDistance();
//	outputFile("ca4663.csv");
	CitiesData data = new CitiesData("./data/ch71009.tsp", 71009);
    }

}
