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
    private String [][] savedSolutionBoard;

    private int foundRow = 0;
    private int foundColumn = 0;

    int iteratorTest = 0;
    List<Integer> iteratorList = new ArrayList<>();

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
//                generate();
//                gameBoard = prepareBoardForUser(gameBoard);
//                jTable.updateUI();

                for(int i = 0 ; i < 100 ; i++){
                    generate();
                jTable.updateUI();
                    System.out.println("_________________TEST_________________________________");
                    //savedSolutionBoard = gameBoard;
                    gameBoard = prepareBoardForUser(gameBoard);
                    for(int r = 0 ; r < 9 ; r++){
                        for(int j = 0 ; j < 9 ; j++){
                            System.out.print(savedSolutionBoard[r][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("------------------------------------------------------------------------------------");
                    if(!searchForMultiSolutions(gameBoard, savedSolutionBoard)){
                        for(int r = 0 ; r < 9 ; r++){
                            for(int j = 0 ; j < 9 ; j++){
                                System.out.print(gameBoard[r][j] + " ");
                            }
                            System.out.println();
                        }
                        System.out.println("------------------------------------------------------------------------------------");
                    }
                }
                for(int i = 0 ; i < 100 ; i++){
                    System.out.println(iteratorList.get(i));
                }
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
        Integer quitLoopCounter = 0;
        //main loop, counting until all empty spaces are filled
        while(0 <= mainIterator && mainIterator <= emptySpaces){
            int row = 0;
            int column = 0;
            findPlace();
            row = foundRow;
            column = foundColumn;

            testIterator++;
            if(mainIterator == 1){
                quitLoopCounter++;
            }
            if(testIterator > 100000){
                solutionBoard[0][0] = "";
                break;
            }

            findNumber(row, column);

            gameBoard = solutionBoard;

//            for(int i = 0 ; i < 9 ; i++){
//                for(int j = 0 ; j < 9 ; j++){
//                    System.out.print(gameBoard[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println("------------------------------------------------------------------------------------");
        }
        System.out.println("LOOOP: " + testIterator + "  MAIN " + mainIterator+ "  --------------------------------");

        iteratorList.add(testIterator);

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

    String findNumberForMultipleSolutions(String[][] board, Integer boardRow, Integer boardColumn, Integer numberFromKnownSolution){
        String foundNumber = "";
        String number = "";
        boolean rowFlag = true;
        boolean columnFlag = true;
        boolean squareFlag = true;

        for(int i = 1 ; i <= 9 ; i++) {
            rowFlag = true;
            columnFlag = true;
            squareFlag = true;
            //check only bigger values if there was a value already
            if (board[boardRow][boardColumn].matches("") || Integer.parseInt(board[boardRow][boardColumn]) < i
                    && i != numberFromKnownSolution) {
                //check if this number is absent in given row and column
                for (int j = 0; j < 9; j++) {
                    if (board[boardRow][j].matches(String.valueOf(i)) || board[j][boardColumn].matches(String.valueOf(i))) {
                        rowFlag = false;
                        columnFlag = false;
                    }
                }
                //searching boundaries of square in which is this number to check if it is absent in given 3x3 square
                int rowStart = 0;
                int rowEnd = 0;
                int columnStart = 0;
                int columnEnd = 0;

                switch (boardRow) {
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

                switch (boardColumn) {
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
                        if (board[rowStart][columnIterator].matches(String.valueOf(i))) {
                            squareFlag = false;
                        }
                    }
                    rowStart++;
                }

                // check if all conditions are fulfilled, if so save number and exit from loop
                if (rowFlag && columnFlag && squareFlag) {
                    foundNumber = String.valueOf(i);
                    i = 10;

                }
            }
        }

        return  foundNumber;
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
            if (solutionBoard[row][column].matches("") || Integer.parseInt(solutionBoard[row][column]) < i) {
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

    String[][] clearBoard(String[][] board){
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                board[row][column] = "";
            }
        }
        return board;
    }

    void generate(){

        gameBoard = clearBoard(gameBoard);

        //table with numbers to choose from while filling first row or column
        Integer[] numbersToFill = {1,2,3,4,5,6,7,8,9};
        Integer numbersCounter = 9;
        // choosing column or row to fill first
                if(ThreadLocalRandom.current().nextInt(0, 2) == 0){
                    //choosing which row to fill first
                    Integer row = ThreadLocalRandom.current().nextInt(0, 9);
                    //filling first row
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
                    savedSolutionBoard = solving(gameBoard);
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
                    savedSolutionBoard = solving(gameBoard);
                }

                //savedSolutionBoard = gameBoard;
        System.out.println("---------------x---------------------------------------------------------------------");
        for(int r = 0 ; r < 9 ; r++){
            for(int j = 0 ; j < 9 ; j++){
                System.out.print(savedSolutionBoard[r][j] + " ");
            }
            System.out.println();
        }
        System.out.println("---------------x---------------------------------------------------------------------");

    }

    String[][] prepareBoardForUser(String[][] board){
        //numbers to erase from the board
        Integer numbersToErase = 43;

//        Integer numbersToErase = 58;
        List<Integer> listOfAvaibleNumbers = new ArrayList<>();
        for(int i = 0 ; i < 81 ; i++){
            listOfAvaibleNumbers.add(i);
        }

        Integer avaibleNumbersCounter = 81;
        //erase numbers from board
        for(int i = 0 ; i < numbersToErase ; i++){
            //find value of number to erase
            Integer numberToErase = ThreadLocalRandom.current().nextInt(0, listOfAvaibleNumbers.size());
            Integer valueToErase = listOfAvaibleNumbers.get(numberToErase);
            //find found number on the board, and erase it
            Integer numberValueIterator = 0;
            for(int row = 0 ; row < 9 ; row++){
                for(int column = 0 ; column < 9 ; column++){
                    if(numberValueIterator == valueToErase){
                        board[row][column] = "";
                    }
                    numberValueIterator++;
                }
            }
            listOfAvaibleNumbers.remove(listOfAvaibleNumbers.get(numberToErase));
            avaibleNumbersCounter--;

        }

        System.out.println("TEST" + avaibleNumbersCounter + " " + listOfAvaibleNumbers.size());
        return board;
    }

    Boolean searchForMultiSolutions(String[][] board, String[][] boardSolved){
        String[][] testBoard = board;
        Boolean multipleSolutionFlag = false;
        String numberToTest = "";
        for(int row = 0 ; row < 9 && !multipleSolutionFlag ; row++){
            for(int column = 0 ; column < 9 && !multipleSolutionFlag ; column++){
                //if empty place is found
                if(board[row][column].matches("")){
                    //check if on this place can be diffrent number than in known solution
                    numberToTest = findNumberForMultipleSolutions(board, row, column, Integer.parseInt(boardSolved[row][column]));
                    //if so, check if there exists a solution with this number at this place
                    if(!numberToTest.matches("")){
                        testBoard[row][column] = numberToTest;
                        testBoard = solving(testBoard);
                        //if so, change flag to ends searching for other solutions
                        if(!testBoard[0][0].matches("")){
                            multipleSolutionFlag = true;
                        }
                    }
                }
            }
        }
        return multipleSolutionFlag;
    }

    public static void main(String[] args) {
        MenuDialog dialog = new MenuDialog();
        dialog.pack();
        dialog.setSize(210,257);
        dialog.setVisible(true);
        System.exit(0);
    }
}
