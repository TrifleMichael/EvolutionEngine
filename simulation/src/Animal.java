public class Animal {

    Genome genome;

    double satiety = 1.0;
    double aggresion = 0.5;

    public Animal(Genome genome, double aggresion) {
        this.genome = genome;
        this.aggresion = aggresion;
    }

    public double getGene(GenomCode geneName) {
        return genome.geneticCode.get(geneName);
    }

    public void getHungry(double hunger) {
        satiety -= hunger;
    }
}
