package geneticalgorithm;

import org.jgap.*;
import org.jgap.impl.CrossoverOperator;

import java.util.List;
import java.util.Vector;

public class RowConstrainedCrossoverOperator extends CrossoverOperator {
    private final SudokuConverter converter;

    public RowConstrainedCrossoverOperator(Configuration a_configuration, SudokuConverter converter) throws InvalidConfigurationException {
        super(a_configuration, 1 );
        this.converter = converter;
    }

    @Override
    protected void doCrossover(IChromosome firstMate, IChromosome secondMate, List a_candidateChromosomes, RandomGenerator generator) {
        Gene[] firstGenes = firstMate.getGenes();
        Gene[] secondGenes = secondMate.getGenes();
        int locus = converter.getRandomEmptyPosition() + 1;

        for(int j = locus; j < firstGenes.length; ++j) {
            Gene gene1;
            gene1 = firstGenes[j];

            Gene gene2;
            gene2 = secondGenes[j];

            if (this.m_monitorActive) {
                gene1.setUniqueIDTemplate(gene2.getUniqueID(), 1);
                gene2.setUniqueIDTemplate(gene1.getUniqueID(), 1);
            }

            Object firstAllele = gene1.getAllele();
            gene1.setAllele(gene2.getAllele());
            gene2.setAllele(firstAllele);
        }

        a_candidateChromosomes.add(firstMate);
        a_candidateChromosomes.add(secondMate);
    }
}
