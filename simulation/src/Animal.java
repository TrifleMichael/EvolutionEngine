public class Animal {

    Genome genome;

    double satiety = 1.0;

    public Animal(Genome genome) {
        this.genome = genome;
    }

    public double getGene(GenomCode geneName) {
        return genome.geneticCode.get(geneName);
    }

    public void getHungry(double hunger) {
        satiety -= hunger;
    }
}
