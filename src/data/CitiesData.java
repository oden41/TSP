package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

class Data {
	public int ID;
	public int dist;
}

public class CitiesData {

	private static double[][] fCitiesPoint;
	private static int noOfCities;
	private static int[][] neighbor;
	private static ArrayList<Integer>[][] revNeighbor;

	public CitiesData(String path, int noOfCities) {
		File file = new File(path);
		BufferedReader bReader = null;
		fCitiesPoint = new double[2][noOfCities];
		this.noOfCities = noOfCities;
		neighbor = new int[50][noOfCities];
		revNeighbor = new ArrayList[50][noOfCities];

		try {
			bReader = new BufferedReader(new FileReader(file));
			int i = 0;
			int j = 0;

			String string = null;
			while ((string = bReader.readLine()) != null) {
				i++;
				if (i < 8)
					continue;

				if (string.equals("EOF"))
					break;

				String[] split = string.split(" ");
				fCitiesPoint[0][j] = Double.parseDouble(split[1]);
				fCitiesPoint[1][j] = Double.parseDouble(split[2]);
				j++;
			}
			bReader.close();
		}
		catch (FileNotFoundException e) {

		}
		catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		createNeighbor();
	}

	private void createNeighbor() {
		// neighbor
		for (int i = 0; i < noOfCities; i++) {
			Data[] data = new Data[noOfCities];
			for (int j = 0; j < data.length; j++) {
				data[j] = new Data();
				data[j].ID = j;
				data[j].dist = calcDistance(i, j);
			}
			Arrays.sort(data, (a, b) -> a.dist - b.dist);
			for (int j = 1; j < 51; j++) {
			    neighbor[j - 1][i] = data[j].ID;
			}
		}
		//rev_neighbor
		for (int i = 0; i < 50; i++) {
		    int[] k_neighbor = neighbor[i];
		    ArrayList<Integer>[] index = new ArrayList[noOfCities];
		    for (int j = 0; j < noOfCities; j++) {
			index[j] = new ArrayList<>();
		    }
		    for (int j = 0; j < k_neighbor.length; j++) {
			index[k_neighbor[j]].add(j);
		    }
		    for (int j = 0; j < index.length; j++) {
			revNeighbor[i][j] = index[j];
		    }
		}
	}

	/**
	 * 都市iとjの距離を計算する
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	public static int calcDistance(int i, int j) {
		double xd = 0;
		double yd = 0;

		xd = Math.abs(fCitiesPoint[0][j] - fCitiesPoint[0][i]);
		yd = Math.abs(fCitiesPoint[1][j] - fCitiesPoint[1][i]);

		return (int) Math.floor(Math.sqrt(xd * xd + yd * yd) + 0.5);
	}

	    private static String concat(char delim,int index) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < neighbor[index].length; i++) {
		    builder.append(String.valueOf(neighbor[index][i]));
		    if(i != neighbor[index].length - 1)
			builder.append(delim);
		}

		return builder.toString();
	    }

	    public static void outputFile(String fileName) {
		try {
		    PrintWriter bw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

		    for (int i = 0; i < neighbor.length; i++) {
			if(i == neighbor.length - 1)
			    System.out.println();
			String string = concat(',', i);
			bw.println(string);
		    }
		    bw.close();
		} catch (IOException e) {
		    // TODO 自動生成された catch ブロック
		    e.printStackTrace();
		}
	    }

	    public static int getKneighbor(int k,int city){
		return neighbor[k][city];
	    }

	    public static ArrayList<Integer> getRevNeighbor(int k, int city) {
		return revNeighbor[k][city];
	    }
}
