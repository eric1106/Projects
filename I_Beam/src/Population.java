import java.util.Random;

public class Population {
	// public static int generation = 1;
	public Chromosome[] individuals;
	public Chromosome[] afterRouletteWheelIndividuals;
	public int DefaultPopulationSize = 85;
	public double totalfitness = 0,totalF1=0,totalF2=0;
	public double[] SelectionProbability = new double[DefaultPopulationSize];
	public double[] CumulativeProbability = new double[DefaultPopulationSize];

	/*
	 * Constructors
	 */
	// Create a population
	public Population(int populationSize, boolean initialise) {
		DefaultPopulationSize = populationSize;
		// generation++;
		individuals = new Chromosome[populationSize];
		afterRouletteWheelIndividuals = new Chromosome[DefaultPopulationSize];
		// Initialise population
		if (initialise) {
			// Loop and create individuals
			for (int i = 0; i < size(); i++) {
				Chromosome newIndividual = new Chromosome();
				newIndividual.generateIndividual();
				saveIndividual(i, newIndividual);
			}
		}
	}

	// Create a population
	public Population(boolean initialise) {
		individuals = new Chromosome[DefaultPopulationSize];
		afterRouletteWheelIndividuals = new Chromosome[DefaultPopulationSize];
		// generation++;
		// Initialise population
		if (initialise) {
			// Loop and create individuals
			for (int i = 0; i < size(); i++) {
				Chromosome newIndividual = new Chromosome();
				newIndividual.generateIndividual();
				saveIndividual(i, newIndividual);
			}
		}
	}

	public void calculateEachChromosomeFitness() {
		for (Chromosome chromo : individuals) {
			chromo.calculateFitness();
		}
	}

	public void calculateTotalFitness() {
		double eachFitness = 0, temp = 0;
		for (int i = 0; i < DefaultPopulationSize; i++) {
			eachFitness = individuals[i].getFitness();
			temp = temp + eachFitness;
			// System.out.println(i+" "+eachFitness);
		}
		totalfitness = temp;
		// System.out.println("total: "+totalfitness+"\n");
	}
	
	public void calculateTotalF1() {
		double eachF1 = 0, temp = 0;
		for (int i = 0; i < DefaultPopulationSize; i++) {
			eachF1 = individuals[i].f1();
			temp = temp + eachF1;
			// System.out.println(i+" "+eachFitness);
		}
		totalF1 = temp;
		// System.out.println("total: "+totalfitness+"\n");
	}

	public void calculateTotalF2() {
		double eachF2 = 0, temp = 0;
		for (int i = 0; i < DefaultPopulationSize; i++) {
			eachF2 = individuals[i].f2();
			temp = temp + eachF2;
			// System.out.println(i+" "+eachFitness);
		}
		totalF2 = temp;
		// System.out.println("total: "+totalfitness+"\n");
	}
	public void calculateSelectionProbabilityAndCumulativeProbability() {
		calculateTotalFitness();
		for (int i = 0; i < DefaultPopulationSize; i++) {
			SelectionProbability[i] = individuals[i].getFitness() / totalfitness;
			// System.out.println(i+" select: "+SelectionProbability[i]);
		}
		// System.out.println("-------------");
		double tempFitness = 0;
		for (int i = 0; i < DefaultPopulationSize; i++) {
			tempFitness = 0;
			for (int j = 0; j <= i; j++) {
				tempFitness = tempFitness + SelectionProbability[j];
				// System.out.println(i + " temp: " + j + " " + tempFitness);
			}
			CumulativeProbability[i] = tempFitness;
			// System.out.println("CumulativeProbability " + i + " : " +
			// CumulativeProbability[i]+"\n");
		}

	}

	public void rouletteWheel() {
		calculateSelectionProbabilityAndCumulativeProbability();
		Random rand = new Random();
		double temp = 0;
		int j = 0;
		// for (int i = 0; i < DefaultPopulationSize; i++) {
		// System.out.println(i + " CumulativeProbability[i] " +
		// CumulativeProbability[i]);
		// }
		// System.out.println("---------------------------");
		// for (int k = 1; k < DefaultPopulationSize; k++) {
		// temp = rand.nextDouble();
		// for (int i = 1; i < DefaultPopulationSize; i++) {
		// System.out.println((i - 1) + " rand: " + temp);
		// System.out.println((i - 1) + " CumulativeProbability[i-1]: " +
		// CumulativeProbability[i - 1] + " " + i
		// + " CumulativeProbability[i]: " + CumulativeProbability[i] + "\n");
		// if (temp > CumulativeProbability[i - 1] && temp < CumulativeProbability[i]) {
		// afterRouletteWheelIndividuals[j++] = individuals[i];
		// System.out.println("i " + i);
		// }
		// }
		// }

		for (int i = 0; i < DefaultPopulationSize; i++) {
			temp = rand.nextDouble();
			// System.out.println(i + " rand: " + temp);
			if (temp <= CumulativeProbability[0]) {
				afterRouletteWheelIndividuals[i] = individuals[0];
			} else {
				// System.out.println(i +" in in ini\n");
				for (int k = 1; k < DefaultPopulationSize; k++) {

					// System.out.println((k - 1) + " CumulativeProbability[k-1]: " +
					// CumulativeProbability[k - 1] + " " + k
					// + " CumulativeProbability[k]: " + CumulativeProbability[k] + "\n");
					if (CumulativeProbability[k - 1] < temp && temp <= CumulativeProbability[k]) {
						afterRouletteWheelIndividuals[i] = individuals[k];
						// System.out.println(k+" select "+individuals[k]+" +++\n");
					}
				}
			}
		}
	}

	/* Getters */
	public Chromosome getIndividual(int index) {
		return individuals[index];
	}

	public Chromosome getFittest() {
		//calculateEachChromosomeFitness();
		Chromosome fittest = individuals[0];
		// Loop through individuals to find fittest
		for (int i = 0; i < size(); i++) {
			if (fittest.getFitness() <= getIndividual(i).getFitness()) {
				fittest = getIndividual(i);
			}
		}
		return fittest;
	}

	/* Public methods */
	// Get population size
	public int size() {
		return individuals.length;
	}

	// Save individual
	public void saveIndividual(int index, Chromosome indiv) {
		individuals[index] = indiv;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "\n";
		int i = 0;
		for (Chromosome chromosome : individuals) {
			s = s + chromosome.toString() + " num: " + i + "\n";
			i++;
		}
		s = s + "\n";
		return s;
	}
}
