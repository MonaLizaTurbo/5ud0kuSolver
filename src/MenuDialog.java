import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MenuDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonSolve;
    private JButton buttonGenerate;
    private JScrollPane scrollPane;
    private JButton buttonGenerateMultiple;
    private JComboBox comboBoxDifficulty;
    private JPanel jPanelInner;

    private JTable jTable;

    JFrame jframe;

    //table for variables from sudoku board
    private String [][] gameBoard = new String[9][9];
    private String [][] solutionBoard;
    private int emptySpaces = 0;
    private int mainIterator = 1;
    private String [][] countBoard;
    private String [][] savedSolutionBoard;

    private int foundRow = 0;
    private int foundColumn = 0;

    int iteratorTest = 0;
    List<Integer> iteratorList = new ArrayList<>();

    private SudokuLevel sudokuLevel = new SudokuLevel();

    //headers of the board
    private String [] gameBoardHeaders = {"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1", "I1"};

    private String[] comboBoxFiller = {"easy", "medium", "hard"};

    public MenuDialog() {
        setContentPane(contentPane);
        setModal(true);

        comboBoxDifficulty.addItem("easy");
        comboBoxDifficulty.addItem("medium");
        comboBoxDifficulty.addItem("hard");


        // initialisation of the table
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                gameBoard[row][column] = "";
            }
        }

        // initialisation of the table in gui window
        jTable = new JTable(gameBoard, gameBoardHeaders);
        //setting table to the view
        scrollPane.setViewportView(jTable);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        buttonSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //solve level
                gameBoard = SolveAndGenerateSudoku.solving(gameBoard);
                jTable.updateUI();
            }
        });

        buttonGenerateMultiple.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateToTXTDialog dialog = new GenerateToTXTDialog();
                dialog.setSize(400,150);
                dialog.setVisible(true);
            }
        });

        buttonGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //generate level
                gameBoard = SolveAndGenerateSudoku.generate(gameBoard);
                //prepare level for game
                if(comboBoxDifficulty.getSelectedItem().toString().matches("easy"))
                    gameBoard = SolveAndGenerateSudoku.prepareBoardForUser(gameBoard, 3);
                else if(comboBoxDifficulty.getSelectedItem().toString().matches("medium"))
                    gameBoard = SolveAndGenerateSudoku.prepareBoardForUser(gameBoard, 2);
                else
                    gameBoard = SolveAndGenerateSudoku.prepareBoardForUser(gameBoard, 1);
                //update view
                jTable.updateUI();

            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public static void main(String[] args) {
        MenuDialog dialog = new MenuDialog();
        dialog.pack();
        dialog.setSize(286,288);
        dialog.setVisible(true);
        System.exit(0);
    }

}
