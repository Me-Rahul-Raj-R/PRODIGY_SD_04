package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Main extends JFrame {
    private final JTextField[][] cells = new JTextField[9][9];
    private final Color BG_COLOR = new Color(240, 248, 255); // Alice Blue
    private final Color SOLVED_COLOR = new Color(34, 139, 34); // Forest Green
    private final Font GRID_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public Main() {
        setTitle("Prodigy InfoTech - Advanced Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 750);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BG_COLOR);

        // 1. Title Header
        JLabel title = new JLabel("SUDOKU SOLVER", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 30));
        title.setForeground(new Color(50, 50, 100));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // 2. The Grid Panel
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        initializeCells(gridPanel);
        add(gridPanel, BorderLayout.CENTER);

        // 3. Control Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        JButton solveBtn = createStyledButton("SOLVE", new Color(70, 130, 180));
        JButton clearBtn = createStyledButton("CLEAR", new Color(220, 20, 60));

        solveBtn.addActionListener(e -> startSolving());
        clearBtn.addActionListener(e -> clearGrid());

        buttonPanel.add(solveBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeCells(JPanel panel) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c] = new JTextField();
                cells[r][c].setHorizontalAlignment(JTextField.CENTER);
                cells[r][c].setFont(GRID_FONT);
                cells[r][c].setBackground(Color.WHITE);

                // Advancement: 3x3 Grid Distinction
                int top = (r % 3 == 0) ? 3 : 1;
                int left = (c % 3 == 0) ? 3 : 1;
                int bottom = (r == 8) ? 3 : 0;
                int right = (c == 8) ? 3 : 0;
                cells[r][c].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.DARK_GRAY));

                panel.add(cells[r][c]);
            }
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 50));
        return btn;
    }

    // --- LOGIC SECTION ---

    private void startSolving() {
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String val = cells[r][c].getText().trim();
                board[r][c] = val.isEmpty() ? 0 : Integer.parseInt(val);
                if (!val.isEmpty()) cells[r][c].setForeground(Color.BLACK);
            }
        }

        if (solve(board)) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (cells[r][c].getText().isEmpty()) {
                        cells[r][c].setText(String.valueOf(board[r][c]));
                        cells[r][c].setForeground(SOLVED_COLOR); // Blue/Green for solved numbers
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int x = 0; x < 9; x++) if (board[row][x] == num) return false;
        for (int x = 0; x < 9; x++) if (board[x][col] == num) return false;
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i + startRow][j + startCol] == num) return false;
        return true;
    }

    private boolean solve(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void clearGrid() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].setText("");
                cells[r][c].setForeground(Color.BLACK);
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
