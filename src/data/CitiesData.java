package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

class Data {
	public int ID;
	public int dist;
}

public class CitiesData {

	private static double[][] fCitiesPoint;
	private static int noOfCities;
	private static int[][] neighbor;
	private static ArrayList<Integer>[] optNeighber;

	public CitiesData(String path, int noOfCities) {
		File file = new File(path);
		BufferedReader bReader = null;
		fCitiesPoint = new double[2][noOfCities];
		this.noOfCities = noOfCities;
		neighbor = new int[50][noOfCities];
		optNeighber = new ArrayList[noOfCities];
		for (int i = 0; i < noOfCities; i++) {
			optNeighber[i] = new ArrayList<>();
		}

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
			Stream.of(data).sorted((a, b) -> a.dist - b.dist);
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
}
