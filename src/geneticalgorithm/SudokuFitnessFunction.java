package geneticalgorithm;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

public class SudokuFitnessFunction extends FitnessFunction {

    public SudokuFitnessFunction(int[] suko) {

    }
    @Override
    protected double evaluate(IChromosome iChromosome) {
        Gene[] genes = iChromosome.getGenes();
        int count = 0;
        for (var g :
               genes ) {
            count += (int) g.getAllele();
        }
        return count;
    }
}
