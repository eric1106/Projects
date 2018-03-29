import java.util.Random;

import org.jenetics.internal.math.statistics;

import sun.print.DocumentPropertiesUI;

public class GeneticAlgorithm {
	public static double mutationRate = 0.001;
	public static double crossoverRate = 0.75;
	public Population population = null;
	Chromosome[] tempChromosomes = new Chromosome[population.DefaultPopulationSize];

	public static Population transferPop(Population pop) {
		return pop;
	}

	public static Population evolvePopulation(Population pop) {

		Population newPopulation = new Population(pop.size(), false);
		Random rand=new Random();
		for (int i = 0; i < pop.size();) {
			Double temp=rand.nextDouble();
			if (temp<crossoverRate) {
				pop.rouletteWheel();
				Chromosome chromosome = crossover(pop);
				newPopulation.saveIndividual(i, chromosome);
				i++;
			}
		}
		mutate(newPopulation);

		return newPopulation;
	}

	// Crossover individuals
	public static Chromosome crossover(Population pop) {
		Chromosome newSol = new Chromosome();
		Chromosome temp1 = selectChrome(pop, null);
		Chromosome temp2 = selectChrome(pop, temp1);
		Random rand = new Random();
		int tempNum = rand.nextInt(70) + 1;
		for (int i = 0; i < newSol.size(); i++) {
			if (i <= tempNum) {
				newSol.genes[i] = temp1.genes[i];
			} else {
				newSol.genes[i] = temp2.genes[i];
			}
		}
		newSol.calculateFitness();
		return newSol;
	}

	public static Chromosome crossoverMuti(Population pop) {
		Chromosome newSol = new Chromosome();
		Chromosome temp1 = selectChrome(pop, null);
		Chromosome temp2 = selectChrome(pop, temp1);
		// Random rand = new Random();
		// int tempNum = rand.nextInt(70) + 1;
		// int tempNum2=rand.nextInt(70)+1;
		// for (int i = 0; i < newSol.size(); i++) {
		// if (i <= tempNum) {
		// newSol.genes[i] = temp1.genes[i];
		// } else {
		// newSol.genes[i] = temp2.genes[i];
		// }
		// }

		Random rand = new Random();
		int tempIndex = rand.nextInt(70) + 1;
		int tempIndex2 = rand.nextInt(70) + 1;
		while (tempIndex2 == tempIndex) {
			tempIndex2 = rand.nextInt(70) + 1;
		}
		int temp = 0;
		if (tempIndex2 < tempIndex) {
			temp = tempIndex;
			tempIndex = tempIndex2;
			tempIndex2 = temp;
		}
		for (int i = 0; i < newSol.size(); i++) {
			if (i < tempIndex) {
				newSol.setGene(i, temp1.getGene(i));
			} else if (i >= tempIndex && i <= tempIndex2) {
				newSol.setGene(i, temp2.getGene(i));
			} else {
				newSol.setGene(i, temp1.getGene(i));
			}
		}
		newSol.calculateFitness();
		return newSol;
	}

	// public static void mutate(Population pop) {
	// int totalSize = pop.size() * pop.individuals[0].size();
	// int mutateSize = (int) (totalSize * mutationRate);
	// // System.out.println("size: " + mutateSize);
	// Random rand = new Random();
	// int indexRand = rand.nextInt(totalSize - mutateSize);// test num 69
	// // System.out.println("all index: " + indexRand);
	// // int whichChromo = indexRand / pop.individuals[0].size();
	// // System.out.println("whichChromo: " + whichChromo);
	// // int whichIndex = indexRand % pop.individuals[0].size();
	// // if (whichIndex >= pop.individuals[0].size() - mutateSize) {
	// // whichIndex = whichIndex - mutateSize;
	// // }
	// // System.out.println("which index: " + whichIndex);
	// double tempRand = rand.nextDouble();
	// if (tempRand < mutationRate) {
	// int whichChromo = indexRand / pop.individuals[0].size();
	// // System.out.println("whichChromo: " + whichChromo);
	// // System.out.println(pop.individuals[0].size());
	// int whichIndex = indexRand % pop.individuals[0].size();
	// // System.out.println("which index1: " + whichIndex);
	// // System.out.println("mutate size: " + mutateSize);
	// if (whichIndex >= pop.individuals[0].size() - mutateSize) {
	// whichIndex = whichIndex - mutateSize;
	// }
	// if (whichIndex<0) {
	// whichIndex=0;
	// }
	// // System.out.println("which index: " + whichIndex);
	// for (int i = whichIndex; i < mutateSize; i++) {
	// // System.out.println("ssss "+i);
	// if (i>=pop.individuals[0].size()) {
	// break;
	// }
	// if (pop.individuals[whichChromo].genes[i] == 0) {
	// pop.individuals[whichChromo].genes[i] = 1;
	// } else {
	// pop.individuals[whichChromo].genes[i] = 0;
	// }
	// }
	// }
	//
	// }

	public static void mutate(Population population) {
		int size = population.size() * population.individuals[0].size();
		int mutationSize = (int) (size * mutationRate);

		double random = Math.random();
		if (random < mutationRate) {
			for (int i = 0; i < mutationSize; i++) {
				int r = (int) Math.random() * size;
				int posChromosome = r / population.individuals[0].size();
				int indexChromosome = r % population.individuals[0].size();
				// System.out.println("hahahaha");
				if (population.individuals[posChromosome].genes[indexChromosome] == 1)
					population.individuals[posChromosome].genes[indexChromosome] = 0;
				else
					population.individuals[posChromosome].genes[indexChromosome] = 1;
			}
		}
	}

	public static Chromosome selectChrome(Population pop, Chromosome chromosome) {
		// pop.rouletteWheel();
		if (chromosome == null) {
			chromosome = new Chromosome();
		}
		Chromosome tempChromo = pop.getFittest();
		Random rand = new Random();
		double tempNum = 0;
		for (int i = 0; i < pop.DefaultPopulationSize; i++) {
			tempNum = rand.nextDouble();
			if (tempNum < crossoverRate && !chromosome.equals(pop.afterRouletteWheelIndividuals[i])) {
				tempChromo = pop.afterRouletteWheelIndividuals[i];
				break;
			}
		}
		return tempChromo;
	}
}
