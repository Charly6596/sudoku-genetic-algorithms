package geneticalgorithm;

import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.data.config.Configurable;
import org.jgap.impl.*;

import java.util.Arrays;

public class GeneticAlgorithm {

    private final int[] sudoku;

    public GeneticAlgorithm(int[] sudoku) {
        this.sudoku = sudoku;
        Configuration.reset();
        Configuration conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true);

        FitnessFunction myFunc = new SudokuFitnessFunction(sudoku);

        try {
            // choose selection method
            // conf.addNaturalSelector(selectr, false);
            conf.setFitnessFunction(myFunc);

            int count = (int) Arrays.stream(sudoku).filter(c -> c == 0).count();
            Gene[] sampleGenes = new Gene[count];


            // Do we need to check constraints here?
            // do we need to check only by rows?
            for (int i = 0; i < count; i++) {
                sampleGenes[i] = new IntegerGene(conf, 1,
                        9);
                // mutations
            }

            IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
            conf.setSampleChromosome(sampleChromosome);
            conf.setPopulationSize(25);

            Genotype population = Genotype.randomInitialGenotype(conf);

            // Evolve the population. Since we don't know what the best answer
            // is going to be, we just evolve the max number of times.
            // ---------------------------------------------------------------
            // search bout evolution monitor
            for (int i = 0; i < 50; i++) {
                population.evolve();
            }

            IChromosome bestSolutionSoFar = population.getFittestChromosome();
            System.out.println("The best solution has a fitness value of " +
                    bestSolutionSoFar.getFitnessValue());
            System.out.println("It contained the following: ");

            int[] list = new int[bestSolutionSoFar.size()];

            Gene[] genes = bestSolutionSoFar.getGenes();

            for (int i = 0, genesLength = genes.length; i < genesLength; i++) {
                Gene g = genes[i];
                int allele = (int) g.getAllele();
                list[i] = allele;
            }

            QQWing solution = new QQWing();
            int[] s = new int[sudoku.length];

            int sc = 0;
            for(int i = 0; i < sudoku.length; i++)
            {
                int el = sudoku[i];
                if(el == 0) {
                    s[i] = list[sc];
                    sc++;
                }
                else {
                    s[i] = el;
                }
            }

            solution.setPuzzle(s);
            solution.printPuzzle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] solve() {

        return sudoku;
    }
}