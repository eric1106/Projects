import java.math.BigInteger;

public class Chromosome {
	static int defaultGeneLength = 71;
	public byte[] genes = new byte[defaultGeneLength];
	// Cache
	private double fitness = 0;
	double x1 = 0, x2 = 0, x3 = 0, x4 = 0;// 10<=x1<=80 10<=x2<=50 0.9<=x3<=5.0 0.9<=x4<=5.0
											// x1 20 bytes x2 19 bytes x3 16 bytes x4 16 bytes
	double x1Iint = 0, x2Iint = 0, x3Iint = 0, x4Iint = 0;
	double x1Max = 80, x1Min = 10;
	double x2Max = 50, x2Min = 10;
	double x3Max = 5, x3Min = 0.9;
	double x4Max = 5, x4Min = 0.9;

	// Create a random individual
	public void generateIndividual() {
		for (int i = 0; i < size(); i++) {
			byte gene = (byte) Math.round(Math.random());
			genes[i] = gene;
		}
	}

	public void decode() {
		separateBinary();
		x1 = x1Min + x1Iint * (x1Max - x1Min) / (Math.pow(2, 20) - 1);
		x2 = x2Min + x2Iint * (x2Max - x2Min) / (Math.pow(2, 19) - 1);
		x3 = x3Min + x3Iint * (x3Max - x3Min) / (Math.pow(2, 16) - 1);
		x4 = x4Min + x4Iint * (x4Max - x4Min) / (Math.pow(2, 16) - 1);

	}

	public void separateBinary() {

		byte[] separateX1 = new byte[20];
		byte[] separateX2 = new byte[19];
		byte[] separateX3 = new byte[16];
		byte[] separateX4 = new byte[16];

		for (int i = 0; i < 20; i++)
			separateX1[i] = getGene(i);
		for (int i = 0; i < 19; i++)
			separateX2[i] = getGene(20 + i);
		for (int i = 0; i < 16; i++)
			separateX3[i] = getGene(39 + i);
		for (int i = 0; i < 16; i++)
			separateX4[i] = getGene(55 + i);

		x1Iint = binaryToDecimal(separateX1);
		x2Iint = binaryToDecimal(separateX2);
		x3Iint = binaryToDecimal(separateX3);
		x4Iint = binaryToDecimal(separateX4);
	}

	public double f1() {
		return 2 * x2 * x4 + x3 * (x1 - 2 * x4);
	}

	public double f2() {
		return 60000 / (x3 * (x1 - 2 * x4) * (x1 - 2 * x4) * (x1 - 2 * x4)
				+ 2 * x2 * x4 * (4 * x4 * x4 + 3 * x1 * (x1 - 2 * x4)));
	}

	public double calculateFitness() {
		double A = 0.5, B = 1-A;
		decode();
		double fitness = A * f1() + B * f2();
		fitness = 1 / fitness;
		this.fitness = fitness;
		return fitness;
	}

	public static int binaryToDecimal(byte[] bytes) {
		String binarySource = "";
		for (byte b : bytes) {
			binarySource = binarySource + b;
		}
		BigInteger bi = new BigInteger(binarySource, 2); // 转换为BigInteger类型
		return Integer.parseInt(bi.toString()); // 转换成十进制
	}

	/* Getters and setters */
	// Use this if you want to create individuals with different gene lengths
	public static void setDefaultGeneLength(int length) {
		defaultGeneLength = length;
	}

	public byte getGene(int index) {
		return genes[index];
	}

	public void setGene(int index, byte value) {
		genes[index] = value;
		fitness = 0;
	}

	/* Public methods */
	public int size() {
		return genes.length;
	}

	public double getFitness() {
		if (fitness == 0) {
			fitness = calculateFitness();
		}
		return fitness;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Chromosome temp = null;
		if (obj instanceof Chromosome) {
			temp = (Chromosome) obj;
		}
		for (int i = 0; i < size(); i++) {
			if (this.genes[i] != temp.genes[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String geneString = "";
		for (int i = 0; i < size(); i++) {
			geneString += getGene(i);
		}
		return geneString;
	}
}
