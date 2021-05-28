package geneticalgorithm;

import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneticAlgorithm {

    private final int[] sudoku;
    private Configuration conf;

    public GeneticAlgorithm(int[] sudoku) {
        this.sudoku = sudoku;
        Configuration.reset();
        conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true);

        SudokuConverter converter = new SudokuConverter(sudoku);
        FitnessFunction myFunc = new SudokuFitnessFunction(sudoku);

        try {
            // choose selection method
            // conf.addNaturalSelector(selectr, false);
            // var select = new TournamentSelector(conf, 2, 0.8);
            // conf.addNaturalSelector(select, false);
            conf.setFitnessFunction(myFunc);
            RowConstrainedCrossoverOperator crossoverOperator = new RowConstrainedCrossoverOperator(conf, converter);
            conf.addGeneticOperator(crossoverOperator);

            int count = (int) Arrays.stream(sudoku).filter(c -> c == 0).count();
            Gene[] sampleGenes = new Gene[count];

            List<List<Gene>> candidates = generateValidGenes();

            // Do we need to check constraints here?
            // do we need to check only by rows?
            for (int i = 0; i < count; i++) {
//                sampleGenes[i] = new IntegerGene(conf, candidates.get(i), candidates.get(i));
                sampleGenes[i] = new IntegerGene(conf, 1, 9);
            }

            IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
            sampleChromosome.setGenes(sampleGenes);

            conf.setSampleChromosome(sampleChromosome);

            conf.setPopulationSize(25);

            Genotype population = Genotype.randomInitialGenotype(conf);

            // Evolve the population. Since we don't know what the best answer
            // is going to be, we just evolve the max number of times.
            // ---------------------------------------------------------------
            // search bout evolution monitor
            for (int i = 0; i < 1500; i++) {
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

    private List<Integer> generateCandidates() {
        List<Integer> candidates = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            candidates.add(i);
        }
        return candidates;
    }

    private List<List<Gene>> generateValidGenes() {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> candidates = generateCandidates();

        for (int i = 0; i < sudoku.length; i++) {
            int val = sudoku[i];

            if(val != 0) {
                candidates.remove(val);
            }

            if((i + 1) % 9 == 0) {
                var cand = generateCandidates();
                cand.removeAll(candidates);
                candidates = generateCandidates();
                res.add(cand);
            }
        }
        return res;
    }

    public int[] solve() {
        return sudoku;
    }
}