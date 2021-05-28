package geneticalgorithm;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.Arrays;

public class SudokuFitnessFunction extends FitnessFunction {

    int[] sudoku; //we should change that name for sth that reminds it is only a template

    public SudokuFitnessFunction(int[] sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    protected double evaluate(IChromosome iChromosome) { //takes each chromosome and then needs to calculate fitness for that
        //so i need to change chromosome to sudoku and then do the functions i wrote

        //here i need to use the
        int[] readySudoku = chromosomeToSudoku(iChromosome);
        int columnConstraints = checkColumnConstraints(readySudoku);
        int squareConstraints = checkSquareConstraints(readySudoku);
        return columnConstraints + squareConstraints;
    }

    private int[] chromosomeToSudoku(IChromosome chrom){
        int[] readySudoku = new int[81];
        int placeInChromosome = 0;
        for (int i = 0; i < sudoku.length; i++) {
            if (sudoku[i] == 0) {
                readySudoku[i] = (Integer) chrom.getGene(placeInChromosome).getAllele();
                placeInChromosome += 1;
            }
            else{
                readySudoku[i] = sudoku[i];
            }
        }
        return readySudoku;
    }

    private int checkColumnConstraints(int[] readySudoku) {
        int fulfilledConstraints = 0;
        int cols = 9;
        int[] cells = new int[cols];

        for (int i = 0; i < cols; i++) { // instead of 9 the square root of len(readysudoku) ??
            Arrays.fill(cells, 0);

            int j = 0;
            while(j < cols) {
                int number = readySudoku[j * cols + i];
                int placeInTable = number - 1;
                if(cells[placeInTable] == 1) {
                    break;
                }

                cells[placeInTable] += 1;
                j++;
            }

            if (j == cols) {
                fulfilledConstraints += 1;
            }
        }
        return fulfilledConstraints;
    }


    private int checkSquareConstraints(int [] readySudoku) {
        int fulfilledConstraints = 0;
        int[] tableForNumbers = new int[9];
        int numOfCol = 9;
        int numOfSquaresInCol = 3;

        for (int i = 0; i < numOfCol; i++) {
            Arrays.fill(tableForNumbers, 0);
            for (int j = 0; j < numOfSquaresInCol; j++) {
                for (int k = 0; k < numOfSquaresInCol; k++) {
                    int position = 9 * (i / 3 * 3 + j) + i % 3 * 3 + k; // needs to be int !
                    int number = readySudoku[position];
                    int placeInTable = number - 1;
                    tableForNumbers[placeInTable] += 1;
                }
            }

            if (CheckIfFulfilled(tableForNumbers)) {
                fulfilledConstraints += 1;
            }
        }
        return fulfilledConstraints;
    }

    public boolean CheckIfFulfilled(int[] table) {
        return Arrays.stream(table).allMatch(v -> v == 1);
    }
}