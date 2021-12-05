public class SudokuLevel {

    private Integer difficultyLevel;
    private String[][] sudokuLevelUnsolved;
    private String[][] sudokuLevelSolved;

    public SudokuLevel(){
        //difficultyLevel =
    }

    public SudokuLevel(Integer difficultyLevel, String[][] sudokuLevelUnsolved, String[][] sudokuLevelSolved) {
        this.difficultyLevel = difficultyLevel;
        this.sudokuLevelUnsolved = sudokuLevelUnsolved;
        this.sudokuLevelSolved = sudokuLevelSolved;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String[][] getSudokuLevelUnsolved() {
        return sudokuLevelUnsolved;
    }

    public void setSudokuLevelUnsolved(String[][] sudokuLevelUnsolved) {
        this.sudokuLevelUnsolved = sudokuLevelUnsolved;
    }

    public String[][] getSudokuLevelSolved() {
        return sudokuLevelSolved;
    }

    public void setSudokuLevelSolved(String[][] sudokuLevelSolved) {
        this.sudokuLevelSolved = sudokuLevelSolved;
    }

    public String getLevelToFile(){
        String preparedLevel = "";
        String preparedSolved = "";
        preparedLevel = difficultyLevel + "\n";
        System.out.println("___________________TEST333333333333333________________");

        for(int r = 0 ; r < 9 ; r++){
            for(int j = 0 ; j < 9 ; j++){
                System.out.print(sudokuLevelSolved[r][j] + " ");
            }
            System.out.println();
        }                    System.out.println("___________________TEST44444444444444________________");

        for(int r = 0 ; r < 9 ; r++){
            for(int j = 0 ; j < 9 ; j++){
                System.out.print(sudokuLevelUnsolved[r][j] + " ");
            }
            System.out.println();
        }
        for(int row = 0 ; row < 9 ; row++){
            for(int column = 0 ; column < 9 ; column++){
                preparedSolved += sudokuLevelSolved[row][column];
                switch (sudokuLevelUnsolved[row][column]){
                    case "":
                        preparedLevel += " ";
                        break;
                    default:
                        preparedLevel += sudokuLevelUnsolved[row][column];
                        break;
                }
            }
            preparedSolved += "\n";
            preparedLevel += "\n";
        }
        preparedLevel += preparedSolved;
        return preparedLevel;
    }
}
