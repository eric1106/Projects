import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* ******************************************************************
************
Author¡¯s name(s):Youbin Huang
Course Title:Artificial Intelligence
Semester:2017 Fall
Assignment Number:homework 2
Submission Date:Oct 16th
Purpose: This program :solving TSP by simulationAnnealing algorithm
Input:
Output:
Help:worked alone.
*********************************************************************
********* */

//******************************************************
//*** Purpose: State of City
//*** Input: None
//*** Output: None
//******************************************************

class City {
	String name;
	Map<String, Integer> nearby = new HashMap<>();

	public City(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	public City() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object arg0) {
		City temp=null;
		if (arg0 instanceof City) {
			temp = (City) arg0;
		} else {
			try {
				throw new Exception("not a City");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.name.equals(temp.name)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}

class Tour {
	/* Holds our citiesList of cities */
	public ArrayList<City> citiesList; // Cache
	public int distance = 0;

	public Tour() {
		// TODO Auto-generated constructor stub
		citiesList = new ArrayList<City>();
	}

	/*
	 * Constructs a citiesList from another citiesList
	 */
	public Tour(ArrayList<City> tour) {
		citiesList = new ArrayList<City>();
		for (City city : tour) {
			this.citiesList.add(city);
		}
	}

	/** Returns citiesList information */
	public ArrayList<City> getCitiesList() {
		return citiesList;
	}

	/* Gets a city from the citiesList */
	public City getCity(int tourPosition) {
		return (City) citiesList.get(tourPosition);
	}

	/* Sets a city in a certain position within a citiesList */
	public void setCity(int tourPosition, City city) {
		citiesList.set(tourPosition, city);
		// If the tours been altered we need to reset the fitness and distance
		distance = 0;
	}

	public Tour addCity(City city) {
		citiesList.add(city);
		return this;
	}

	public ArrayList<City> getAllCities() {
		return citiesList;
	}

	/* Get number of cities on our citiesList */
	public int numberOfCities() {
		return citiesList.size();
	}
	//******************************************************
	//*** Purpose: compute the distance of city in a tour
	//*** Input: None
	//*** Output: None
	//******************************************************
	public int getDistance() {
		int tourDistance = 0;
		City fromCity =null;
		City destinationCity=null;

		for (int cityIndex = 0; cityIndex < numberOfCities(); cityIndex++) {
			fromCity = getCity(cityIndex);
			if (cityIndex + 1 < numberOfCities()) {
				destinationCity = getCity(cityIndex + 1);
			} else {
				destinationCity = getCity(0);
			}
			tourDistance = tourDistance + fromCity.nearby.get(destinationCity.name);
		}
		distance = tourDistance;
		return distance;
	}

	//******************************************************
	//*** Purpose: swap two order of city
	//*** Input: None
	//*** Output: None
	//******************************************************
	public Tour setNearbySolution() {
		Tour newSolution = new Tour(this.citiesList);
		int tourPos1 = (int) (4 * Math.random()+1);
		int tourPos2 = (int) (4 * Math.random()+1);
		// Get the cities at selected positions in the tour
		City citySwap1 = newSolution.getCity(tourPos1);
		City citySwap2 = newSolution.getCity(tourPos2);
		// Swap them
		newSolution.setCity(tourPos2, citySwap1);
		newSolution.setCity(tourPos1, citySwap2);
		return newSolution;
	}

	//******************************************************
	//*** Purpose: initial a tour
	//*** Input: None
	//*** Output: None
	//******************************************************
	public Tour initialSolution() {
		// Loop through all our destination cities and add them to our citiesList
		for (int cityIndex = 0; cityIndex < citiesList.size(); cityIndex++) {
			setCity(cityIndex, this.getCity(cityIndex));
		}
		return this;
	}

	@Override
	public String toString() {
		String geneString = "|";
		for (int i = 0; i < numberOfCities(); i++) {
			geneString += getCity(i) + "|";
		}
		return geneString;
	}
}

public class SA {
	/** Set initial temp */
	private double currentTemperature = 3000;
	private Tour currentSolution;

	City snellFarm = new City("SnellFarm");
	City movies = new City("Movies");
	City school = new City("School");
	City gym = new City("Gym");
	City planterFarm = new City("PlanterFarm");
	
	//******************************************************
	//*** Purpose: initial distance between each city and first tour
	//*** Input: None
	//*** Output: None
	//******************************************************

	public void initial() {

		snellFarm.nearby.put("Movies", 7);
		snellFarm.nearby.put("PlanterFarm", 20);
		snellFarm.nearby.put("Gym", 10000);
		snellFarm.nearby.put("School", 10000);

		movies.nearby.put("SnellFarm", 7);
		movies.nearby.put("PlanterFarm", 12);
		movies.nearby.put("Gym", 15);
		movies.nearby.put("School", 10);

		school.nearby.put("Movies", 10);
		school.nearby.put("SnellFarm", 10000);
		school.nearby.put("Gym", 10);
		school.nearby.put("PlanterFarm", 14);

		gym.nearby.put("School", 10);
		gym.nearby.put("Movies", 15);
		gym.nearby.put("SnellFarm", 10000);
		gym.nearby.put("PlanterFarm", 14);

		planterFarm.nearby.put("SnellFarm", 20);
		planterFarm.nearby.put("Movies", 12);
		planterFarm.nearby.put("School", 14);
		planterFarm.nearby.put("Gym", 8);
		

		Tour tour = new Tour();
		
		tour.addCity(snellFarm);
		tour.addCity(movies);
		tour.addCity(school);
		tour.addCity(gym);
		tour.addCity(planterFarm);

		currentSolution = tour.initialSolution();
	}

	//******************************************************
	//*** Purpose: calculate P
	//*** Input: None
	//*** Output: None
	//******************************************************
	private double acceptanceProbability(int energy, int newEnergy, double temperature) {
		// If the new solution is better, accept it
		if (newEnergy < energy) {
			return 1.0;
		}
		// If the new solution is worse, calculate an acceptance probability
		return Math.exp((energy - newEnergy) / temperature);
	}
	//******************************************************
	//*** Purpose: simulationAnnealing
	//*** Input: None
	//*** Output: None
	//******************************************************

	public void simulationAnnealing() {

		Tour bestSolution = new Tour(currentSolution.getCitiesList());
		Tour nearbySolution = null;
		int currentEnergy = 0;
		int nearbyEnergy = 0;
		int changeEnergy = 0;
		int count=0;
		System.out.println("Tour S 				  	    		  Tour S¡¯ 						E(S) 		E(S¡¯) 				¦¤E");
		while (currentTemperature > 0.001) {
			for (int i = 0; i < 1000; i++) {
				nearbySolution = currentSolution.setNearbySolution();
				currentEnergy = currentSolution.getDistance();
				nearbyEnergy = nearbySolution.getDistance();
				changeEnergy = currentEnergy - nearbyEnergy;
				//print out 10 tours randomly
				if(Math.random()<0.2&&count<10) {
					System.out.println(currentSolution.toString()+"		  "+nearbySolution.toString()+" 		"+currentEnergy+"		 "+nearbyEnergy+" 				"+changeEnergy);
					count++;
				}
				//if energy is low,then accept answer
				if (changeEnergy < 0) {
					currentSolution = nearbySolution;
					
				}
				//P>R ,then get answer.
				if (changeEnergy>0&&acceptanceProbability(currentEnergy, nearbyEnergy, currentTemperature) > Math.random()) {
					currentSolution = new Tour(nearbySolution.getCitiesList());
				}
				//get best answer
				if (currentSolution.getDistance() < bestSolution.getDistance()) {
					bestSolution = new Tour(currentSolution.getCitiesList());
				}
			}
			currentTemperature = currentTemperature * 0.999;
		}
		System.out.println("\nbest solution is: " + bestSolution.toString());
		System.out.println("best solution's distance is: " + bestSolution.distance);
	}

	public static void main(String[] args) {
		SA sa = new SA();
		sa.initial();
		sa.simulationAnnealing();
	}
}
