/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package my.foodorderingsystem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kiana M. Yeo
 * @data April 18, 2026
 */
public class FoodSystemUI extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FoodSystemUI.class.getName());

    DefaultTableModel tableModel;
    double grandTotal = 0;
    
    // ✅ ADD THIS (if not yet added)
    ArrayList<OrderItem> currentOrder = new ArrayList<>();

    // =========================
    // ✅ ADD THIS CLASS HERE
    // =========================
    static class OrderItem {
        String name;
        int qty;

        OrderItem(String name, int qty) {
            this.name = name;
            this.qty = qty;
        }
    }

    //Array to hold meal information
    Object[][] meals = {
        {"Ramen", 120},
        {"Jjangmyeon", 130},
        {"Jjeon", 100},
        {"Kimbap", 90},
        {"Coke", 50},
        {"Iced Tea", 60}
    };

    private double total = 0.0;
    int currentRow = 0; // ✅ NEW

    /**
     * Creates new form FoodSystemUI
     */
    public FoodSystemUI() {
        initComponents();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(253, 194, 194));

        //Spacing and padding
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Fonts
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jButton8.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        jButton8.setBorder(new javax.swing.border.LineBorder(Color.BLACK, 2, true)); //shadows

        //Table design
        orderTable.setBackground(new Color(253, 194, 194)); // light pink
        orderTable.setForeground(Color.BLACK); // text color
        orderTable.setRowHeight(25);

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null}
                },
                new String[]{
                    "Qty", "Item", "Total Price"
                }
        ));

        //Alternate row colors
        orderTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Zebra stripes
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(255, 230, 230));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }

                // Alignment per column
                if (column == 0) { // Qty
                    setHorizontalAlignment(CENTER);
                } else if (column == 1) { // Item
                    setHorizontalAlignment(CENTER);
                } else if (column == 2) { // Total Price
                    setHorizontalAlignment(RIGHT);
                }

                return c;
            }
        });

        //Column header
        javax.swing.table.JTableHeader header = orderTable.getTableHeader();

        header.setBackground(new Color(220, 20, 60)); // dark red
        header.setForeground(Color.BLACK); // text color
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

        //Remove grid lines
        orderTable.setShowGrid(false);
        orderTable.setIntercellSpacing(new java.awt.Dimension(0, 0));

        //Selection color
        orderTable.setSelectionBackground(new Color(255, 102, 102));
        orderTable.setSelectionForeground(Color.WHITE);
        tableModel = (DefaultTableModel) orderTable.getModel();

        //Assign buttons to corresponding meal items  
        jButton1.setActionCommand("Ramen");
        jButton2.setActionCommand("Jjangmyeon");
        jButton3.setActionCommand("Jjeon");
        jButton4.setActionCommand("Kimbap");
        jButton5.setActionCommand("Coke");
        jButton6.setActionCommand("Iced Tea");

        // Add action listeners to buttons
        jButton1.addActionListener(this::addButtonActionPerformed);
        jButton2.addActionListener(this::addButtonActionPerformed);
        jButton3.addActionListener(this::addButtonActionPerformed);
        jButton4.addActionListener(this::addButtonActionPerformed);
        jButton5.addActionListener(this::addButtonActionPerformed);
        jButton6.addActionListener(this::addButtonActionPerformed);

        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(new CalculatorPanel1(), java.awt.BorderLayout.CENTER);

        // Double-click delete
        orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = orderTable.getSelectedRow();
                    if (row != -1) {
                        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
                        model.removeRow(row);

                        grandTotal = 0;
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            grandTotal += Double.parseDouble(
                                    tableModel.getValueAt(i, 2).toString()
                            );
                        }
                        updateTotalDisplay();
                    }
                }
            }
        });
    }

    private void addButtonActionPerformed(ActionEvent evt) {

        String buttonLabel = evt.getActionCommand();
        Object[] selectedMeal = null;

        // Find meal
        for (Object[] meal : meals) {
            if (meal[0].equals(buttonLabel)) {
                selectedMeal = meal;
                break;
            }
        }

        if (selectedMeal == null) {
            return;
        }

        // Spinner for quantity
        SpinnerNumberModel sModel = new SpinnerNumberModel(1, 1, 30, 1);
        JSpinner spinner = new JSpinner(sModel);

        // ✅ FIX: better dialog (OK / Cancel)
        int option = JOptionPane.showConfirmDialog(
                null,
                spinner,
                "Select Quantity",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        int qty = (Integer) spinner.getValue();

        double price = (int) selectedMeal[1];
        double itemTotal = price * qty;
        
        String itemName = selectedMeal[0].toString();   // ✅ ADD THIS
        currentOrder.add(new OrderItem(itemName, qty)); // ✅ ADD THIS

        String formattedTotal = String.format("%.2f", itemTotal);

        // Align total column to right
        orderTable.getColumnModel().getColumn(2).setCellRenderer(
                new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(RIGHT);
            }
        }
        );

        if (currentRow < tableModel.getRowCount()) {
            tableModel.setValueAt(qty, currentRow, 0);
            tableModel.setValueAt(selectedMeal[0], currentRow, 1);
            tableModel.setValueAt(formattedTotal, currentRow, 2);
            currentRow++;
        } else {
            JOptionPane.showMessageDialog(this, "Table is full!");
        }

        // ✅ FIX: update grand total properly
        grandTotal += itemTotal;
        updateTotalDisplay();
    }

    private int getNextOrderNumber() {
    int highestOrderNumber = 0;

    try {
        if (Files.exists(Paths.get("C:\\Kitchen Terminal\\Orders.txt"))) {
            for (String line : Files.readAllLines(Paths.get("C:\\Kitchen Terminal\\Orders.txt"))) {

                if (line.startsWith("ORDER #")) {
                    try {
                        int number = Integer.parseInt(
                                line.substring(7).trim()
                        );

                        if (number > highestOrderNumber) {
                            highestOrderNumber = number;
                        }

                    } catch (NumberFormatException e) {
                        // Ignore invalid order numbers
                    }
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return highestOrderNumber + 1;
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 153, 153));

        jPanel2.setBackground(new java.awt.Color(253, 194, 194));

        orderTable.setBackground(new java.awt.Color(255, 245, 245));
        orderTable.setForeground(new java.awt.Color(255, 51, 51));
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Qty", "Item", "Total Price"
            }
        ));
        jScrollPane3.setViewportView(orderTable);

        jButton7.setBackground(new java.awt.Color(255, 51, 0));
        jButton7.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jButton7.setText("CANCEL ORDER");
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.setOpaque(true);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(42, 221, 7));
        jButton8.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jButton8.setText("DONE");
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("Grand Total:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setLayout(new java.awt.GridLayout(3, 2, 5, 5));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/foodorderingsystem/ramen2.png"))); // NOI18N
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/foodorderingsystem/jjangmyeon1.png"))); // NOI18N
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/foodorderingsystem/jjeon2.png"))); // NOI18N
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/foodorderingsystem/kimbap1.png"))); // NOI18N
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/foodorderingsystem/coke1.png"))); // NOI18N
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/foodorderingsystem/icedtea1.png"))); // NOI18N
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(jButton6);

        jScrollPane2.setViewportView(jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Kia's Seoul Table");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 19, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (orderTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No order yet!");
            return;
        }

        // =========================
        // Save order for kitchen terminal
        try (FileWriter writer = new FileWriter("C:\\Kitchen Terminal\\Orders.txt", true)) {

            int nextOrderNumber = getNextOrderNumber();

            String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(new java.util.Date());

            String time = new java.text.SimpleDateFormat("HH:mm:ss")
                    .format(new java.util.Date());

            writer.write("ORDER #" + String.format("%03d", nextOrderNumber) + "\n");
            writer.write("DATE: " + date + "\n");
            writer.write("TIME: " + time + "\n");

            for (OrderItem item : currentOrder) {
                writer.write(item.qty + " x " + item.name + "\n");
            }

            writer.write("===========================\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        // =========================

        // Receipt PDF (unchanged)
        ReceiptPrinter.printReceipt(orderTable, grandTotal);

        JOptionPane.showMessageDialog(this,
                "Order complete!\nTotal amount: ₱ " + String.format("%.2f", grandTotal));

        // reset
        tableModel.setRowCount(0);
        for (int i = 0; i < 8; i++) {
            tableModel.addRow(new Object[]{null, null, null});
        }

        currentRow = 0;
        grandTotal = 0;
        updateTotalDisplay();
        currentOrder.clear(); // ✅ ADD THIS
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // clear table rows
        tableModel.setRowCount(0);

        // ✅ recreate empty rows again
        for (int i = 0; i < 8; i++) {
            tableModel.addRow(new Object[]{null, null, null});
        }

        currentRow = 0; // ✅ INSERT HERE

        // reset total
        grandTotal = 0;
        updateTotalDisplay();

        JOptionPane.showMessageDialog(this, "Order cancelled!");
    }//GEN-LAST:event_jButton7ActionPerformed

    // ✅ FIX: implemented method (no more crash)
    private void updateTotalDisplay() {
        jLabel1.setText("Grand Total: ₱ " + String.format("%.2f", grandTotal));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FoodSystemUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable orderTable;
    // End of variables declaration//GEN-END:variables
}
