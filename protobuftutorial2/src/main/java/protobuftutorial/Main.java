package protobuftutorial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import protobuftutorial.CountryProto.Country;
import protobuftutorial.CountryProto.Country.Builder;
import protobuftutorial.CountryProto.Country.HDI;

public class Main {

	public static void main(String[] args) {
		writeData();
		readData();
	}

	private static void writeData() {
		Builder countryBuilder = CountryProto.Country.newBuilder();
		countryBuilder.setName("United States of America");
		countryBuilder.setCapital("Washington, D.C.");
		countryBuilder.setHdi(HDI.VERY_HIGH);
		countryBuilder.setPopulation(309349689);

		countryBuilder.addCity("Houston");
		countryBuilder.addCity("Los Angeles");
		countryBuilder.addCity("Tucson");

		Country usa = countryBuilder.build();
		try {
			FileOutputStream output = new FileOutputStream("c:\\java\\temp\\usa.data");
			usa.writeTo(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readData() {
		try {
			Country usa = CountryProto.Country.parseFrom(new FileInputStream("c:\\java\\temp\\usa.data"));
			System.out.println(usa.getName());
			System.out.println(usa.getCapital());
			System.out.println(usa.getPopulation());
			System.out.println(usa.getHdi().name());
			for (int i = 0; i < usa.getCityCount(); i++) {
				System.out.println("> " + usa.getCity(i));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
