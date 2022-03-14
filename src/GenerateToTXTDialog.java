import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateToTXTDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField generateTextField;
    private JLabel jLabelDesc;
    private JComboBox comboBoxNumberOfLevels;
    private Pattern pattern;

    public static final  String[] comboBoxNumbers = {"10", "20", "30"};

    public GenerateToTXTDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        comboBoxNumberOfLevels = new JComboBox(comboBoxNumbers);

        jLabelDesc.setText("Enter the number of levels to generate of each level of difficulty.");

        //pattern to validate user input
        pattern = Pattern.compile("^\\d*$");

        buttonOK.setText("Generate");
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Matcher matcher = pattern.matcher(generateTextField.getText());
                //check if inserted value is valid
                if(matcher.matches()) {
                    //generate x levels of each difficulty
                    SolveAndGenerateSudoku.writeLevelsToJSON(Integer.valueOf(generateTextField.getText()));
                    onCancel();

                }
            }

        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GenerateToTXTDialog dialog = new GenerateToTXTDialog();
        dialog.pack();
        dialog.setSize(430,230);
        dialog.setVisible(true);
        System.exit(0);
    }

}
