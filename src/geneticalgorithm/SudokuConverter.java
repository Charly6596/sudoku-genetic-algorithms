package geneticalgorithm;

import org.jgap.IChromosome;

import java.util.*;

public class SudokuConverter {
    private Map<Integer, Integer> emptyIndexes;
    private int[] generatedSudoku;
    private int freeCount;

    private ArrayList<Integer> rowBoundaries;

    public SudokuConverter(int[] generatedSudoku) {
        this.generatedSudoku = generatedSudoku;
        emptyIndexes = new HashMap<>();
        rowBoundaries = new ArrayList<>();
        freeCount = (int) Arrays.stream(generatedSudoku).filter(c -> c == 0).count();
        initMap();
    }

    public boolean isBoundary(int i) {
        return rowBoundaries.contains(i);
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
        Random rand = new Random();
        int i = rand.nextInt(rowBoundaries.size());
        return rowBoundaries.get(i);
    }

    private int[] chromosomeToSudoku(IChromosome chrom){
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
