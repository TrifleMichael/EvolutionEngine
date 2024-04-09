public class Animal {

    Genome genome;

    double satiety = 1.0;
    double aggresion;

    public Animal(Genome genome) {
        this.genome = genome;
        this.aggresion = genome.independantGeneticCode.get(IndependantGenomeCode.AGGRESSION);
    }

    public double getGene(GenomCode geneName) {
        return genome.geneticCode.get(geneName);
    }

    public void getHungry(double hunger) {
        satiety -= hunger;
    }
}
