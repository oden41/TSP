package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

class Data {
	public int ID;
	public double dist;
}

public class CitiesData {

	private static double[] fCitiesPoint;
	private static int noOfCities;
	private static int[] neighbor;
	private static HashSet<Integer>[] revNeighbor;

	public CitiesData(String path, int noOfCities) {
		File file = new File("./data/" + path + ".tsp");
		BufferedReader bReader = null;
		fCitiesPoint = new double[2 * noOfCities];
		this.noOfCities = noOfCities;
		neighbor = new int[50 * noOfCities];
		revNeighbor = new HashSet[50 * noOfCities];

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
				setCitiesPoint(j, 0, Double.parseDouble(split[1]));
				setCitiesPoint(j, 1, Double.parseDouble(split[2]));
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

		createNeighbor(path);
	}

	private void createNeighbor(String path) {
		// neighbor
		inputFile("./data/neighbor_" + path + "_1.csv");
		// neighbor
//		for (int i = 0; i < noOfCities; i++) {
//			Data[] data = new Data[noOfCities];
//			for (int j = 0; j < data.length; j++) {
//				data[j] = new Data();
//				data[j].ID = j;
//				data[j].dist = calcDistance(i, j);
//			}
//			Arrays.sort(data, (a, b) -> ((Double) a.dist).compareTo((Double) b.dist));
//			for (int j = 1; j < 51; j++) {
//			    setNeighbor(j - 1, i, data[j].ID);
//			}
//		}
		//rev_neighbor
		for (int i = 0; i < 50; i++) {
		    int[] k_neighbor = getNeighbor(i);
		    HashSet<Integer>[] index = new HashSet[noOfCities];
		    for (int j = 0; j < noOfCities; j++) {
			index[j] = new HashSet<>();
		    }
		    for (int j = 0; j < k_neighbor.length; j++) {
			index[k_neighbor[j]].add(j);
		    }
		    for (int j = 0; j < index.length; j++) {
			setRevNeighbor(i, j, index[j]);
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
	public static double calcDistance(int i, int j) {
		double xd = 0;
		double yd = 0;

		xd = Math.abs(getCitiesPoint(j, 0) - getCitiesPoint(i, 0));
		yd = Math.abs(getCitiesPoint(j, 1) - getCitiesPoint(i, 1));

		return Math.sqrt(xd * xd + yd * yd);
	}

        private static String concat(char delim,int index) {
            	StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 50; i++) {
		    builder.append(String.valueOf(getNeighbor(i, index)));
		    if(i != 50 - 1)
			builder.append(delim);
		}

		return builder.toString();
	}

        public static void inputFile(String filename) {
            BufferedReader br = null;
		try {
		    br = new BufferedReader(new FileReader(filename));
		    String str = "";
		    int city = 0;
		        while ((str = br.readLine()) != null) {
		            String[] split = str.split(",");
		            for (int i = 0; i < split.length; i++) {
				setNeighbor(i, city, Integer.parseInt(split[i]));
			    }
		            city++;
		        }
		    br.close();
		} catch (IOException e) {
		    // TODO 自動生成された catch ブロック
		    e.printStackTrace();
		}
	}

	public static void outputFile(String fileName) {
		try {
		    PrintWriter bw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));

		    for (int i = 0; i < noOfCities; i++) {
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
		return getNeighbor(k, city);
	    }

	    public static double getCitiesPoint(int row, int col) {
		return fCitiesPoint[row * 2 + col];
	    }

	    public static void setCitiesPoint(int row,int col, double element) {
		fCitiesPoint[row * 2 + col] = element;
	    }

	    public static int getNeighbor(int k, int city) {
		return neighbor[k * noOfCities + city];
	    }

	    public static int[] getNeighbor(int k) {
		int[] newArray = new int[noOfCities];
		System.arraycopy(neighbor, k * noOfCities, newArray, 0, noOfCities);
		return newArray;
	    }

	    public static void setNeighbor(int k, int city, int element) {
		neighbor[k * noOfCities + city] = element;
	    }

	    public static HashSet<Integer> getRevNeighbor(int k,int city) {
		return revNeighbor[k * noOfCities + city];
	    }


	private void setRevNeighbor(int k, int city, HashSet<Integer> hashList) {
	    revNeighbor[k * noOfCities + city] = hashList;
	}

}
