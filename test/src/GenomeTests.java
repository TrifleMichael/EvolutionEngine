public class GenomeTests {

    public static void main(String[] args) {
//        test1();
        test2();
    }

    static void test1() {
        Genome genome = new Genome(0.16, 0.16, 0.16, 0.16, 0.16, 0.16);
        Genome newGenome = genome.mutateGeneticCode();

        newGenome.printGenome();
    }

    static void test2() {
        Genome g1 = new Genome(0, 0.8, 0.2, 0, 0, 0);
        Genome g2 = new Genome(0, 0, 0.2, 0.8, 0, 0);

        Genome result = g1.combineGenomes(g2);
        result.printGenome();
    }
}
