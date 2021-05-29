package geneticalgorithm;

import com.qqwing.QQWing;
import org.jgap.*;
import org.jgap.audit.EvolutionMonitor;
import org.jgap.event.EventManager;
import org.jgap.impl.*;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm {

    private final static int POPULATION = 25;

    private final int[] sudoku;
    private final Configuration conf;
    private SudokuConverter converter;

    public GeneticAlgorithm(int[] sudoku) {
        this.sudoku = sudoku;
        Configuration.reset();
        conf = new Configuration();
        conf.setPreservFittestIndividual(true);

        FitnessFunction myFunc = new SudokuFitnessFunction(sudoku);

        try {
            conf.setBreeder(new GABreeder());
            conf.setRandomGenerator(new StockRandomGenerator());
            conf.setEventManager(new EventManager());
            BestChromosomesSelector rouletteSelector = new BestChromosomesSelector(conf, 0.8);
            conf.addNaturalSelector(rouletteSelector, false);
            conf.setMinimumPopSizePercent(100);
            conf.setSelectFromPrevGen(1.0D);
            conf.setRandomGenerator(new SeededRandomGenerator(1));
            conf.setKeepPopulationSizeConstant(true);
            conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
            conf.setChromosomePool(new ChromosomePool());
            // choose selection method
            // conf.addNaturalSelector(selectr, false);
            // var select = new TournamentSelector(conf, 2, 0.8);
            // conf.addNaturalSelector(select, false);
            conf.setFitnessFunction(myFunc);

            converter = new SudokuConverter(sudoku, conf.getRandomGenerator());
            RowConstrainedCrossoverOperator crossoverOperator = new RowConstrainedCrossoverOperator(conf, converter);
            RowConstrainedMutationOperator mutationOperator = new RowConstrainedMutationOperator(conf, converter);
            conf.addGeneticOperator(crossoverOperator);
            conf.addGeneticOperator(mutationOperator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] solve() {
        Genotype population = null;
        try {
            population = new Genotype(conf, generatePopulation());
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Evolve the population. Since we don't know what the best answer
        // is going to be, we just evolve the max number of times.
        // ---------------------------------------------------------------
        // search bout evolution monitor
        population.evolve(5000);

        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        System.out.println("It contained the following: ");

        return converter.chromosomeToSudoku(bestSolutionSoFar);
    }

    private Population generatePopulation() throws InvalidConfigurationException {
        int count = (int) Arrays.stream(sudoku).filter(c -> c == 0).count();
        var baseGenes = generateValidGenes();
        IChromosome[] chromosomes = new IChromosome[POPULATION];

        for (int i = 0; i < POPULATION; i++) {
            shuffleGenes(baseGenes);
            chromosomes[i] = new Chromosome(conf, baseGenes.stream().flatMap(List::stream).collect(Collectors.toList()).toArray(new Gene[count]));
        }

        conf.setSampleChromosome(chromosomes[0]);
        conf.setPopulationSize(POPULATION);
        return new Population(conf, chromosomes);
    }

    private void shuffleGenes(List<List<Gene>> genes) {
        for (var geneList : genes) {
            Collections.shuffle(geneList, new Random(1));
        }
    }

    private List<Integer> generateCandidates() {
        List<Integer> candidates = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            candidates.add(i);
        }
        return candidates;
    }

    private List<List<Gene>> generateValidGenes() throws InvalidConfigurationException {
        List<List<Gene>> res = new ArrayList<>();
        List<Integer> candidates = generateCandidates();

        for (int i = 0; i < sudoku.length; i++) {
            Integer val = sudoku[i];

            if (val != 0) {
                candidates.remove(val);
            }

            if ((i + 1) % 9 == 0) {
                List<Gene> genes = new ArrayList<>();
                for (int gen : candidates) {
                    IntegerGene gene = new IntegerGene(conf, 1, 9);
                    gene.setAllele(gen);
                    genes.add(gene);
                }
                res.add(genes);
                candidates = generateCandidates();
            }
        }
        return res;
    }
}