import jahuwaldt.plot.*;
import java.awt.*;
import javax.swing.*;

public class I_beamMain {
	public static void main(String[] args) {
		int numGenerations = 200;
		double[] xArr = new double[numGenerations];
		double[] yArr = new double[numGenerations];
		double[] xArr2 = new double[numGenerations];
		double[] yArr2 = new double[numGenerations];
		double[] xArr3 = new double[numGenerations];
		double[] yArr3 = new double[numGenerations];
		double[] xArr4 = new double[numGenerations];
		double[] yArr4 = new double[numGenerations];
		Population population = new Population(true);
		Chromosome[] bests=new Chromosome[numGenerations];
 		population.calculateEachChromosomeFitness();
		population.rouletteWheel();
		int generationCount = 0;
		for (; generationCount < numGenerations; generationCount++) {
			Chromosome best=population.getFittest();
			bests[generationCount]=best;
			System.out.println("Generation: " + generationCount + "\n---------------------------\nthe x1:  " + best.x1+ "  the x2:  " + best.x2+ "  the x3:  " + best.x3+ "  the x4:  " + best.x4
					+ "\nfitness " + best.getFitness()+"\n");
			population.calculateTotalFitness();
			population.calculateTotalF1();
			population.calculateTotalF2();
			double aveFit = population.totalfitness / population.size();
			double aveF1 = population.totalF1 / population.size();
			double aveF2 = population.totalF2 / population.size();
			population = GeneticAlgorithm.evolvePopulation(population);
			xArr[generationCount] = generationCount + 1;
			yArr[generationCount] = aveF1;
			xArr2[generationCount] = generationCount + 1;
			yArr2[generationCount] = best.f1();
			xArr3[generationCount] = generationCount + 1;
			yArr3[generationCount] =best.f2();
			xArr4[generationCount] = generationCount + 1;
			yArr4[generationCount] =aveF2;
		}
		Chromosome last=population.getFittest();
		System.out.println("last genes: " + last);
		System.out.println(
				"x1: " + last.x1 + " x2: " + last.x2 + " x3: " + last.x3 + " x4: " + last.x4);
		System.out.println("best fitness: " + last.getFitness());
		System.out.println("f1(): " + last.f1() + " f2(): " + last.f2()+ "\n");

		
		Chromosome chromosome = findBest(bests);
		System.out.println("best genes: " + chromosome);
		System.out.println(
				"x1: " + chromosome.x1 + " x2: " + chromosome.x2 + " x3: " + chromosome.x3 + " x4: " + chromosome.x4);
		System.out.println("best fitness: " + chromosome.getFitness());
		System.out.println("f1(): " + chromosome.f1() + " f2(): " + chromosome.f2());

		Plot2D aPlot = new SimplePlotXY(xArr, yArr, "F1 Static Deflection", "Generation No.", "Avg. Population Fitness",
				null, null, null);
		Plot2D aPlot2 = new SimplePlotXY(xArr2, yArr2, "F1 Static Deflection", "Generation No.",
				"Best. Population Fitness", null, null, null);
		// Make the horizontal axis a log axis.
		PlotAxis xAxis = aPlot.getHorizontalAxis();
		xAxis.setScale(new LinearAxisScale());
		PlotPanel panel = new PlotPanel(aPlot);
		panel.setBackground(Color.white);
		PlotWindow window = new PlotWindow("SimplePlotXY Plot Window", panel);
		window.setSize(500, 300);
		window.setLocation(250, 250); // location on screen
		window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		window.show();

		PlotAxis xAxis2 = aPlot.getHorizontalAxis();
		xAxis.setScale(new LinearAxisScale());
		PlotPanel panel2 = new PlotPanel(aPlot2);
		panel.setBackground(Color.white);
		PlotWindow window2 = new PlotWindow("SimplePlotXY Plot Window", panel2);
		window2.setSize(500, 300);
		window2.setLocation(750, 250); // location on screen
		window2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		window2.show();
		
		Plot2D aPlot3 = new SimplePlotXY(xArr3, yArr3, " F2 Static Deflection", "Generation No.",
				"Best. Population Fitness", null, null, null);
		
		PlotAxis xAxis3 = aPlot.getHorizontalAxis();
		xAxis.setScale(new LinearAxisScale());
		PlotPanel panel3 = new PlotPanel(aPlot3);
		panel.setBackground(Color.white);
		PlotWindow window3 = new PlotWindow("SimplePlotXY Plot Window", panel3);
		window3.setSize(500, 300);
		window3.setLocation(750, 550); // location on screen//250, 550)
		window3.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		window3.show();
		
		Plot2D aPlot4 = new SimplePlotXY(xArr4, yArr4, "F2 Static Deflection", "Generation No.",
				"Ave. Population Fitness", null, null, null);
		
		PlotAxis xAxis4 = aPlot.getHorizontalAxis();
		xAxis.setScale(new LinearAxisScale());
		PlotPanel panel4 = new PlotPanel(aPlot4);
		panel.setBackground(Color.white);
		PlotWindow window4 = new PlotWindow("SimplePlotXY Plot Window", panel4);
		window4.setSize(500, 300);
		window4.setLocation(250, 550); // location on screen//750, 550
		window4.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		window4.show();
		
		
	}
	
	public static Chromosome findBest(Chromosome[] bests) {
		Chromosome best=bests[0];
		for (int i = 0; i < bests.length; i++) {
			if (best.getFitness() <= bests[i].getFitness()) {
				best = bests[i];
			}
		}
		return best;	
	}
}
