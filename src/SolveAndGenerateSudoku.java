import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SolveAndGenerateSudoku {


    // main solving function
    static String[][] solving (String[][] board){

        // copying grid entered by user to working table
//        solutionBoard = gameBoard;
//        sudokuLevel.setSudokuLevelSolved(sudokuLevel.getSudokuLevelUnsolved().clone());
        // initialisation of table which will hold counted and marked empty spaces
        String[][] countBoard = new String[9][9];
        //variable counting empty spaces in the sudoku grid
        Integer emptySpaces = 0;

        // counting empty spaces in the grid
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                if(board[row][column].matches("")){
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
        Integer mainIterator = 1;
        // variable that counts the rotation of the loop
        int testIterator = 1;
        Integer quitLoopCounter = 0;
        //main loop, counting until all empty spaces are filled
        while(0 <= mainIterator && mainIterator <= emptySpaces){
//            int row = 0;
//            int column = 0;
            int[] foundPlace = findPlace(countBoard, mainIterator);
//            row = foundRow;
//            column = foundColumn;

            testIterator++;
            if(mainIterator == 1){
                quitLoopCounter++;
            }
            if(testIterator > 120000){
                board[0][0] = "";
                break;
            }

            board = findNumber(foundPlace[0], foundPlace[1], board);
            if(!board[foundPlace[0]][foundPlace[1]].matches("")){
                mainIterator++;
            }
            else{
                mainIterator--;
            }
        }

        return board;
    }

    // function which search for place in grid of current consider empty space based on "mainIterator" variable
    static int[] findPlace(String[][] countBoard, int mainIterator){
        int foundRow = 0;
        int foundColumn = 0;
        for(int j = 0 ; j < 9 ; j++){
            for(int k = 0 ; k < 9 ; k++){
                if(countBoard[j][k].matches(String.valueOf(mainIterator)))
                {
                    foundRow = j;
                    foundColumn = k;
                    j = 9;
                    k = 9;
                    //return new int[]{j, k};
                }
            }
        }
        return new int[]{foundRow, foundColumn};
    }

    // function which searching for the number to put in given cell
    static String[][] findNumber(int row, int column, String[][] board){

        String number = "";
        boolean rowFlag;
        boolean columnFlag;
        boolean squareFlag ;

        for(int i = 1 ; i <= 9 ; i++) {
            rowFlag = true;
            columnFlag = true;
            squareFlag = true;
            //check only bigger values if there was a value already
            if (board[row][column].matches("") || Integer.parseInt(board[row][column]) < i) {
                //check if this number is absent in given row and column
                for (int j = 0; j < 9; j++) {
                    if (board[row][j].matches(String.valueOf(i)) || board[j][column].matches(String.valueOf(i))) {
                        rowFlag = false;
                        columnFlag = false;
                    }
                }
                //searching boundaries of square in which is this number to check if it is absent in given 3x3 square
                int rowStart = 0;
                int rowEnd;
                int columnStart = 0;
                int columnEnd;

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
                        if (board[rowStart][columnIterator].matches(String.valueOf(i))) {
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
        board[row][column] = number;

        return board;
    }

    static String[][] clearBoard(String[][] board){
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                board[row][column] = "";
            }
        }
        return board;
    }

    static String findNumberForMultipleSolutions(String[][] board, Integer boardRow, Integer boardColumn, Integer numberFromKnownSolution){
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

    static String[][] generate(String[][] board){

        board = clearBoard(board);

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
                board[row][column] = String.valueOf(numbersToFill[number]);
                numbersCounter--;
                while(number < 8 ){
                    numbersToFill[number] = numbersToFill[number+1];
                    number++;
                }
            }
            //solve rest of the board
//                    savedSolutionBoard = solving(gameBoard);
        }
        else{
            //choosing which column to fill first
            Integer column = ThreadLocalRandom.current().nextInt(0,9);
            //filling first column
            for(int row = 0 ; row < 9 ; row++){
                int number = ThreadLocalRandom.current().nextInt(0, numbersCounter);
                board[row][column] = String.valueOf(numbersToFill[number]);
                numbersCounter--;
                while(number < 8 ){
                    numbersToFill[number] = numbersToFill[number+1];
                    number++;
                }
            }
        }

        //solving the rest of the board
        board = solving(board);
        //savedSolutionBoard = gameBoard;
//        System.out.println("---------------x---------------------------------------------------------------------");
//        for(int r = 0 ; r < 9 ; r++){
//            for(int j = 0 ; j < 9 ; j++){
//                System.out.print(savedSolutionBoard[r][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("---------------x---------------------------------------------------------------------");

        return board;
    }

    static String[][] prepareBoardForUser(String[][] board, int levelOfDifficulty){
        //numbers to erase from the board
        Integer numbersToErase = 44;
        switch (levelOfDifficulty){
            case 1:
                numbersToErase = 44;
                break;
            case 2:
                numbersToErase = 33;
                break;
            case 3:
                numbersToErase = 22;
                break;
            default:
                numbersToErase = 44;
        }

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

        return board;
    }

    static Boolean searchForMultiSolutions(String[][] board, String[][] boardSolved){
        String[][] testBoard = new String[9][9];
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                testBoard[row][column] = board[row][column];
            }
        }
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

    static String[][] fillUpSudokuTable(String[][] board){

        // initialisation of the table
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                board[row][column] = "";
            }
        }

        return board;
    }

    //generate levels and write them to JSON file
    static void writeLevelsToJSON(int numberOfLevels){
        String levelsJSON = "\"levels\" : { \n\t\"";
        for(int levelOfDifficulty = 1 ; levelOfDifficulty <= 3 ; levelOfDifficulty++){
            for(int j = 0 ; j < numberOfLevels ; j++){
                String[][] level = new String[9][9];
                level = generate(level);
                String levelSolved = arrayToString(level);
                level = prepareBoardForUser(level, levelOfDifficulty);
                levelsJSON += arrayToString(level) + "\" : {\n\t\t\"difficulty\" : \"" + levelOfDifficulty + "\",\n\t\t\"level\" : \""
                        + arrayToString(level) + "\",\n\t\t\"solved\" : \"" + levelSolved + "\"\n\t\t}";
                if(!(j == (numberOfLevels - 1)) || !(levelOfDifficulty == 3)){
                    levelsJSON += ",\n\t\"";
                }
            }
        }
        levelsJSON += "\n\t}";
        try (PrintWriter out = new PrintWriter("sudokuBoards.json")) {
            out.print(levelsJSON);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

    }

    static String arrayToString(String[][] array){
        String string = "";
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                if(array[row][column].matches("")){
                    string += " ";
                }
                else{
                    string +=array[row][column];
                }
            }
        }
        return string;
    }

}
