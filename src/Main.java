/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */

import geneticalgorithm.GeneticAlgorithm;
import org.jgap.*;
import org.jgap.impl.*;
import com.qqwing.QQWing;
import org.jgap.impl.job.MaxFunction;

import java.util.Arrays;

public class Main {
    /* String containing the CVS revision. Read out via reflection!/
    private static final String CVS_REVISION = "$Revision: 1.9 $";

    /**
     * Starts the example.
     * @param args if optional first argument provided, it represents the number
     * of bits to use, but no more than 32
     *
     * @author Neil Rotstan
     * @author Klaus Meffert
     * @since 2.0
     */
    public static void main(String[] args) {
        QQWing mySudoku = new QQWing();
        mySudoku.setPuzzle(new int[] {0, 5, 0, 7, 6, 3, 1, 9, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 1, 0, 4, 0, 8, 0, 0, 0, 3, 0, 0, 6, 0, 0, 4, 0, 7, 9, 0, 0, 0, 1, 0, 0, 0, 0, 5, 3, 0, 0, 0, 0, 9, 7, 0, 4, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0
        });

        int[] sudoku = mySudoku.getPuzzle();

        Arrays.stream(sudoku).forEach(c -> System.out.print(" " + c));
        System.out.println();
        mySudoku.printPuzzle();
        var algorithm = new GeneticAlgorithm(sudoku);
        System.out.println("Solution:");
        mySudoku.solve();
        mySudoku.printSolution();
        var sol = algorithm.solve();
        mySudoku.setPuzzle(sol);
        mySudoku.printPuzzle();
    }
}