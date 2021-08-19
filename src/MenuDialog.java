import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MenuDialog extends JDialog {
    private JPanel contentPane;
    private JButton solveButton;
    private JButton generateButton;
    private JScrollPane scrollPane;

    private JTable jTable;

    //table for variables from sudoku board
    private String [][] gameBoard = new String[9][9];
    private String [][] solutionBoard;
    private int emptySpaces = 0;
    private int mainIterator = 1;
    private String [][] countBoard;

    private int foundRow = 0;
    private int foundColumn = 0;

    //headers of the board
    private String [] gameBoardHeaders = {"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1", "I1"};
    public MenuDialog() {
        setContentPane(contentPane);
        setModal(true);

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

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solving(gameBoard);
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generate();
                gameBoard = prepareBoardForUser(gameBoard);
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

    // main solving function
    String[][] solving (String[][] board){

        // copying grid entered by user to working table
        solutionBoard = gameBoard;
        // initialisation of table which will hold counted and marked empty spaces
        countBoard = new String[9][9];
        //variable counting empty spaces in the sudoku grid
        emptySpaces = 0;

        // counting empty spaces in the grid
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                if(gameBoard[row][column].matches("")){
                    emptySpaces++;
                    countBoard[row][column] = String.valueOf(emptySpaces);
                }
                else {
                    countBoard[row][column] = "";
                }
            }
        }
        System.out.println(emptySpaces);

        // solving the grid

        // variable indicating which empty space is examined now
        mainIterator = 1;
        // variable that counts the rotation of the loop
        int testIterator = 1;
        //main loop, counting until all empty spaces are filled
        while(0 <= mainIterator && mainIterator <= emptySpaces){
            int row = 0;
            int column = 0;
            findPlace();
            row = foundRow;
            column = foundColumn;

            testIterator++;

            findNumber(row, column);

            gameBoard = solutionBoard;
        }
        System.out.println("LOOOP: " + testIterator + "  MAIN " + mainIterator+ "  --------------------------------");

        jTable.updateUI();

        return gameBoard;
    }

    // function which search for place in grid of current consider empty space based on "mainIterator" variable
    void findPlace(){
        for(int j = 0 ; j < 9 ; j++){
            for(int k = 0 ; k < 9 ; k++){
                if(countBoard[j][k].matches(String.valueOf(mainIterator)))
                {
                    foundRow = j;
                    foundColumn = k;
                    j = 9;
                    k = 9;
                    //System.out.println("test j " + foundRow + " K " + foundColumn );
                }
            }
            //System.out.println("row " + foundRow + " column " + foundColumn);
        }
        //System.out.println("row " + foundRow + " column " + foundColumn);
    }


    // function which searching for the number to put in given cell
    void findNumber(int row, int column){

        String number = "";
        boolean rowFlag = true;
        boolean columnFlag = true;
        boolean squareFlag = true;

        for(int i = 1 ; i <= 9 ; i++) {
            rowFlag = true;
            columnFlag = true;
            squareFlag = true;
            //check only bigger values if there was a value already
            if (solutionBoard[row][column].matches("") || Integer.valueOf(solutionBoard[row][column]) < i) {
                //check if this number is absent in given row and column
                for (int j = 0; j < 9; j++) {
                    if (solutionBoard[row][j].matches(String.valueOf(i)) || solutionBoard[j][column].matches(String.valueOf(i))) {
                        rowFlag = false;
                        columnFlag = false;
                    }
                }
                //searching boundaries of square in which is this number to check if it is absent in given 3x3 square
                int rowStart = 0;
                int rowEnd = 0;
                int columnStart = 0;
                int columnEnd = 0;

                switch (row) {
                    case 0:
                    case 1:
                    case 2:
                        rowStart = 0;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        rowStart = 3;
                        break;
                    case 6:
                    case 7:
                    case 8:
                        rowStart = 6;
                        break;
                }

                rowEnd = rowStart + 2;

                switch (column) {
                    case 0:
                    case 1:
                    case 2:
                        columnStart = 0;
                        break;
                    case 3:
                    case 4:
                    case 5:
                        columnStart = 3;
                        break;
                    case 6:
                    case 7:
                    case 8:
                        columnStart = 6;
                        break;
                }

                columnEnd = columnStart + 2;

                while (rowStart <= rowEnd) {
                    for (int columnIterator = columnStart; columnIterator <= columnEnd ; columnIterator++) {
                        //System.out.print(solutionBoard[rowStart][columnIterator] + " ");
                        if (solutionBoard[rowStart][columnIterator].matches(String.valueOf(i))) {
                            squareFlag = false;
                        }
                    }
                    rowStart++;
                }

                // check if all conditions are fulfilled, if so save number and exit from loop
                if (rowFlag && columnFlag && squareFlag) {
                    number = String.valueOf(i);
                    i = 10;

                }
            }
        }

        // insert found number in to the table
        solutionBoard[row][column] = number;

        //if none of the numbers fulfill conditions, decrement "mainIterator" to check another value for preceding
        // empty place, else increment mainIterator to examine next empty space
        if(solutionBoard[row][column].matches("")){
            mainIterator--;
            solutionBoard[row][column] = "";
        }
        else{
            mainIterator++;
        }

    }

    void generate(){

        //table with numbers to choose from while filling first row or column
        Integer[] numbersToFill = {1,2,3,4,5,6,7,8,9};
        Integer numbersCounter = 9;
        // choosing column or row to fill first
                if(ThreadLocalRandom.current().nextInt(0, 2) == 0){
                    //choosing which row to fill first
                    Integer row = ThreadLocalRandom.current().nextInt(0, 9);
                    //filling firt row
                    for(int column = 0 ; column < 9 ; column++){
                        int number = ThreadLocalRandom.current().nextInt(0, numbersCounter);
                        gameBoard[row][column] = String.valueOf(numbersToFill[number]);
                        numbersCounter--;
                        while(number < 8 ){
                            numbersToFill[number] = numbersToFill[number+1];
                            number++;
                        }
                    }
                    //solve rest of the board
                    solving(gameBoard);
                }
                else{
                    //choosing which column to fill first
                    Integer column = ThreadLocalRandom.current().nextInt(0,9);
                    //filling first column
                    for(int row = 0 ; row < 9 ; row++){
                        int number = ThreadLocalRandom.current().nextInt(0, numbersCounter);
                        gameBoard[row][column] = String.valueOf(numbersToFill[number]);
                        numbersCounter--;
                        while(number < 8 ){
                            numbersToFill[number] = numbersToFill[number+1];
                            number++;
                        }
                    }
                    //solving the rest of the board
                    solving(gameBoard);
                }

    }

    String[][] prepareBoardForUser(String[][] board){
        //numbers to erase from the board
        Integer numbersToErase = 43;
        List<Integer> listOfAvaibleNumbers = new ArrayList<>();
        for(int i = 0 ; i < 81 ; i++){
            listOfAvaibleNumbers.add(i);
        }
        List<List<Integer>> listOfNumbers = new ArrayList<>();
        Integer numberToFill = 0;
        for(int row = 0 ; row < 9 ; row++){
            List<Integer> helpList = new ArrayList<>();
            for(int column = 0 ; column < 9 ; column++){
                helpList.add(numberToFill);
                numberToFill++;
            }
            listOfNumbers.add(helpList);
        }

        Integer avaibleNumbersCounter = 81;
        //erase numbers from board
        for(int i = 0 ; i < numbersToErase ; i++){
            //find value of number to erase
            Integer numberToErase = ThreadLocalRandom.current().nextInt(0, avaibleNumbersCounter);
            Integer valueToErase = listOfAvaibleNumbers.get(numberToErase);
            //find found number on the board, and erase it
            for(int row = 0 ; row < 9 ; row++){
                for(int column = 0 ; column < 9 ; column++){
                    if(listOfNumbers.get(row).get(column) == valueToErase){
                        board[row][column] = "";
                    }
                }
            }
            listOfAvaibleNumbers.remove(numberToErase);
            avaibleNumbersCounter--;

        }

        return board;
    }

    public static void main(String[] args) {
        MenuDialog dialog = new MenuDialog();
        dialog.pack();
        dialog.setSize(210,257);
        dialog.setVisible(true);
        System.exit(0);
    }
}
