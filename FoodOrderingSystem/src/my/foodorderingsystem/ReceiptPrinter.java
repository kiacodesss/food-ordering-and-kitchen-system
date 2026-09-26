/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.foodorderingsystem;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;

/**
 *
 * @author Kiana M. Yeo
 * @date April 18, 2026
 */

public class ReceiptPrinter {
    public static void printReceipt(JTable table, double grandTotal) {
        
        PrinterJob job = PrinterJob.getPrinterJob();

        job.setPrintable((Graphics g, PageFormat pf, int page) -> {

            // ================= FIX #1: safer page check =================
            if (page != 0) { // 🔴 CHANGED (was: page > 0)
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.translate(pf.getImageableX(), pf.getImageableY());

            int y = 20;

            // Title
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            g2.drawString("OFFICIAL RECEIPT", 150, y);
            y += 20;

            // Date
            g2.setFont(new Font("Monospaced", Font.PLAIN, 12));

            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            g2.drawString("Date: " + date, 20, y);
            y += 20;

            g2.drawString("----------------------------------------", 20, y);
            y += 20;

            g2.drawString("Qty   Item                Total Price", 20, y);
            y += 20;

            g2.drawString("----------------------------------------", 20, y);
            y += 20;

            // ================= FIX #2: NULL SAFE TABLE LOOP =================
            for (int i = 0; i < table.getRowCount(); i++) {

                Object qtyObj = table.getValueAt(i, 0);   // 🔴 CHANGED
                Object itemObj = table.getValueAt(i, 1);  // 🔴 CHANGED
                Object totalObj = table.getValueAt(i, 2); // 🔴 CHANGED

                // 🔴 NEW: skip empty rows (prevents crash)
                if (qtyObj == null || itemObj == null || totalObj == null) {
                    continue;
                }

                String qty = qtyObj.toString();
                String item = itemObj.toString();
                String total = totalObj.toString();

                // ================= FIX #3: better spacing =================
                g2.drawString(
                        String.format("%-5s %-20s %10s", qty, item, total), // 🔴 CHANGED spacing
                        20, y
                );

                y += 20;
            }

            y += 10;
            g2.drawString("----------------------------------------", 20, y);
            y += 20;

            // Grand total
            g2.setFont(new Font("Monospaced", Font.BOLD, 12));
            g2.drawString("GRAND TOTAL: ₱ " + String.format("%.2f", grandTotal), 20, y); // 🔴 CHANGED (added ₱)
            y += 30;

            g2.drawString("Thank you!", 20, y);

            return Printable.PAGE_EXISTS;
        });

        // ================= FIX #4: safer print handling =================
        boolean doPrint = job.printDialog();

        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                // 🔴 CHANGED: safer error handling
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Print failed: " + e.getMessage());
            }
        } else {
            // 🔴 NEW: user cancelled print
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Print cancelled.");
        }
    }
}
