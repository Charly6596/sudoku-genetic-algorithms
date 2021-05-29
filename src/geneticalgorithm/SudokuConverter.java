package geneticalgorithm;

import org.jgap.IChromosome;
import org.jgap.RandomGenerator;

import java.util.*;

public class SudokuConverter {
    private final int[] generatedSudoku;
    private final RandomGenerator random;

    private final ArrayList<Integer> rowBoundaries;

    public SudokuConverter(int[] generatedSudoku, RandomGenerator random) {
        this.generatedSudoku = generatedSudoku;
        rowBoundaries = new ArrayList<>();
        this.random = random;
        initMap();
    }

    public ArrayList<Integer> getBoundaries() {
        return rowBoundaries;
    }

    private void initMap() {
        int genId = 0;
        int last = -1;

        for (int i = 0; i < generatedSudoku.length; i++) {
            if (generatedSudoku[i] == 0) {
                if((i + 1) % 9 == 0) {
                    rowBoundaries.add(genId);
                    last = -1;
                } else {
                    last = genId;
                }
                genId++;
            } else {
                if(last != -1 && (i + 1) % 9 == 0) {
                    rowBoundaries.add(last);
                    last = -1;
                }
            }
        }
    }

    public int getRandomEmptyPosition() {
        int i = random.nextInt(rowBoundaries.size());
        return rowBoundaries.get(i);
    }

    public int[] chromosomeToSudoku(IChromosome chrom){
        int[] readySudoku = new int[81];
        int placeInChromosome = 0;
        for (int i = 0; i < generatedSudoku.length; i++) {
            if (generatedSudoku[i] == 0) {
                readySudoku[i] = (Integer) chrom.getGene(placeInChromosome).getAllele();
                placeInChromosome += 1;
            }
            else{
                readySudoku[i] = generatedSudoku[i];
            }
        }
        return readySudoku;
    }
}
