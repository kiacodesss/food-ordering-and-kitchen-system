package my.foodorderingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorPanel1 extends JPanel {

    private JTextField display;
    private String operator = "";
    private double num1 = 0;

    public CalculatorPanel1() {
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 20));
        display.setHorizontalAlignment(JTextField.RIGHT);
        add(display, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(4, 4, 5, 5));

        String[] btns = {
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            "0","=","+","C"
        };

        for (String text : btns) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.addActionListener(e -> handleInput(text));
            buttons.add(btn);
        }

        add(buttons, BorderLayout.CENTER);
    }

    private void handleInput(String input) {
        if (input.matches("[0-9]")) {
            display.setText(display.getText() + input);
        } 
        else if (input.matches("[+\\-*/]")) {
            num1 = Double.parseDouble(display.getText());
            operator = input;
            display.setText("");
        } 
        else if (input.equals("=")) {
            double num2 = Double.parseDouble(display.getText());
            double result = 0;

            switch (operator) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/": result = num1 / num2; break;
            }

            display.setText(String.valueOf(result));
        } 
        else if (input.equals("C")) {
            display.setText("");
        }
    }
}