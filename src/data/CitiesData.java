package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CitiesData {

    public CitiesData(String path) {
	File file = new File(path);
	BufferedReader bReader = null;
	try {
	    bReader = new BufferedReader(new FileReader(file));
	} catch (FileNotFoundException e) {

	}
    }

}
