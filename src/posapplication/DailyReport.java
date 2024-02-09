/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package posapplication;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import raven.chart.ModelChart;

/**
 *
 * @author New
 */
public class DailyReport extends javax.swing.JFrame {

    /**
     * Creates new form Reports
     */
    public DailyReport() {
        initComponents();
        chart.setTitle("Daily Report");
        chart.addLegend("Amount", Color.decode("#7b4397"), Color.decode("#dc2430"));
        setYearlyData();
        
        //chart.addLegend("Cost", Color.decode("#e65c00"), Color.decode("#F9D423"));
        //chart.addLegend("Profit", Color.decode("#0099F7"), Color.decode("#F11712"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        curveLineChart1 = new raven.chart.CurveLineChart();
        panelShadow1 = new raven.panel.PanelShadow();
        chart = new raven.chart.CurveLineChart();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelShadow1.setBackground(new java.awt.Color(34, 59, 68));
        panelShadow1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelShadow1.setColorGradient(new java.awt.Color(17, 37, 45));

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadow1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(263, 263, 263))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelShadow1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void setDailyData(){
         LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pau_pos","root","root")    ;
            System.out.println("Connected");
            PreparedStatement ps = con.prepareStatement("SELECT date_sold, SUM(quantity*price_per_quantity) AS total_amount " +
                    "FROM product_sold " + "WHERE date_sold BETWEEN ? AND ? " +
                    "GROUP BY date_sold");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String startDateString = startDate.format(formatter);
            String endDateString = endDate.format(formatter);
            
            System.out.println("Start: " + startDateString + "End: " + endDateString);
            
            ps.setString(1, startDateString);
            ps.setString(2, endDateString);
            ResultSet rs = ps.executeQuery();
            
            
             
            
         //   tableModel.addRow(data);
         Map<String,Double> salesData = new LinkedHashMap<>();
         
            LocalDate currentDate = startDate;
            
               while(!currentDate.isAfter(endDate)){
                   String date = currentDate.format(formatter);
                   if(!salesData.containsKey(date)){
                      salesData.put(date,0.0);
                   }
                   currentDate = currentDate.plusDays(1);
               }
            while(rs.next()){
               //Double totalAmount = rs.getInt("quantity") * rs.getDouble("price_per_quantity");
              // System.out.println(totalAmount);
               double totalAmount = rs.getDouble("total_amount");
               System.out.println(totalAmount);
               String date = rs.getString("date_sold");
               salesData.put(date, totalAmount);
               //chart.addData(new ModelChart(date,new double[]{totalAmount}));
               
            }
            
               
        for(Map.Entry<String,Double> entry: salesData.entrySet()){
            String date = entry.getKey();
            double totalAmount = entry.getValue();
             chart.addData(new ModelChart(date,new double[]{totalAmount}));
        }
               
               
            chart.start();
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
    
    private void setWeeklyData(){
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusWeeks(1);
        LocalDate endDate = today;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pau_pos","root","root")    ;
            System.out.println("Connected");
            PreparedStatement minDatePs = con.prepareStatement("SELECT MIN(date_sold) AS min_date FROM product_sold");
            ResultSet minDateRs = minDatePs.executeQuery();
            minDateRs.next();
            String minDateString = minDateRs.getString("min_date");
            LocalDate minDate = LocalDate.parse(minDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            startDate = minDate;
            
            
            PreparedStatement ps = con.prepareStatement("SELECT date_sold, SUM(quantity*price_per_quantity) AS total_amount " +
                    "FROM product_sold " + "WHERE date_sold BETWEEN ? AND ? " +
                    "GROUP BY date_sold");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String startDateString = startDate.format(formatter);
            String endDateString = endDate.format(formatter);
            
            System.out.println("Start: " + startDateString + "End: " + endDateString);
            
            ps.setString(1, startDateString);
            ps.setString(2, endDateString);
            ResultSet rs = ps.executeQuery();
            
            
            
             
            
         //   tableModel.addRow(data);
         Map<String,Double> weeklySalesData = generateWeeklyMap(startDate, endDate);
         
           /* LocalDate currentDate = startDate;
            
               while(!currentDate.isAfter(endDate)){
                   String date = currentDate.format(formatter);
                   if(!salesData.containsKey(date)){
                      salesData.put(date,0.0);
                   }
                   currentDate = currentDate.plusDays(1);
               }*/
            while(rs.next()){
               //Double totalAmount = rs.getInt("quantity") * rs.getDouble("price_per_quantity");
              // System.out.println(totalAmount);
               double totalAmount = rs.getDouble("total_amount");
               System.out.println(totalAmount);
               String dateSold = rs.getString("date_sold");
               String week = getWeekFromDate(dateSold);
               weeklySalesData.put(week, weeklySalesData.getOrDefault(week, 0.0)+totalAmount);
               //chart.addData(new ModelChart(date,new double[]{totalAmount}));
               
            }
            
               
        for(Map.Entry<String,Double> entry: weeklySalesData.entrySet()){
            String week = entry.getKey();
            double totalAmount = entry.getValue();
             chart.addData(new ModelChart(week,new double[]{totalAmount}));
        }
               
               
            chart.start();
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
    
    private Map<String, Double> generateWeeklyMap(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> weeklySalesData = new LinkedHashMap<>();
        LocalDate currentWeekStart = startDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        while (!currentWeekStart.isAfter(endDate)) {
            String weekLabel = currentWeekStart.format(DateTimeFormatter.ofPattern("dd/MM")) + " - " +
                currentWeekStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd/MM"));
            weeklySalesData.put(weekLabel, 0.0); // Initialize with zero sales for each week
            currentWeekStart = currentWeekStart.plusWeeks(1);
        }
        return weeklySalesData;
    }
    
    
    private String getWeekFromDate(String date){
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        String formattedStartDate = startOfWeek.format(DateTimeFormatter.ofPattern("dd/MM"));
        String formattedEndDate = endOfWeek.format(DateTimeFormatter.ofPattern("dd/MM"));
        return formattedStartDate + " - " + formattedEndDate;
        
    }
    
    
    private void setMonthlyData() {
    LocalDate today = LocalDate.now();
    LocalDate startDate = today.minusMonths(11).withDayOfMonth(1); // Start date is 11 months ago, with first day of the month
    LocalDate endDate = today; // End date is today

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pau_pos", "root", "root");
        System.out.println("Connected");
        PreparedStatement ps = con.prepareStatement("SELECT SUBSTRING_INDEX(date_sold, '/', -1) AS year, " +
                                                       "SUBSTRING_INDEX(SUBSTRING_INDEX(date_sold, '/', 2), '/', -1) AS month, " +
                                                       "SUM(quantity * price_per_quantity) AS total_amount " +
                                                       "FROM product_sold " +
                                                       "WHERE STR_TO_DATE(date_sold, '%d/%m/%Y') BETWEEN ? AND ? " +
                                                       "GROUP BY SUBSTRING_INDEX(date_sold, '/', -1), SUBSTRING_INDEX(SUBSTRING_INDEX(date_sold, '/', 2), '/', -1)");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = startDate.format(formatter);
        String endDateString = endDate.format(formatter);

        System.out.println("Start: " + startDateString + "End: " + endDateString);

        ps.setString(1, startDateString);
        ps.setString(2, endDateString);
        ResultSet rs = ps.executeQuery();

        Map<String, Double> monthlySalesData = generateMonthlyMap(startDate, endDate);

        while (rs.next()) {
            int year = rs.getInt("year");
            int month = rs.getInt("month");
            double totalAmount = rs.getDouble("total_amount");
            System.out.println(year +" "+ month);
            String monthLabel = YearMonth.of(year, month).format(DateTimeFormatter.ofPattern("MMM yyyy"));
            monthlySalesData.put(monthLabel, totalAmount);
        }

        for (Map.Entry<String, Double> entry : monthlySalesData.entrySet()) {
            String monthLabel = entry.getKey();
            double totalAmount = entry.getValue();
            chart.addData(new ModelChart(monthLabel, new double[]{totalAmount}));
        }

        chart.start();
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
}

private Map<String, Double> generateMonthlyMap(LocalDate startDate, LocalDate endDate) {
    Map<String, Double> monthlySalesData = new LinkedHashMap<>();
    LocalDate currentDate = startDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
    while (!currentDate.isAfter(endDate)) {
        String monthLabel = currentDate.format(formatter);
        monthlySalesData.put(monthLabel, 0.0); // Initialize with zero sales for each month
        currentDate = currentDate.plusMonths(1);
    }
    return monthlySalesData;
}
private void setYearlyData() {
    LocalDate today = LocalDate.now();

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pau_pos", "root", "root");
        System.out.println("Connected");

        // Query to get the minimum date from the database
        PreparedStatement minDatePs = con.prepareStatement("SELECT DATE_FORMAT(MIN(STR_TO_DATE(date_sold, '%d/%m/%Y')), '%d/%m/%Y') "
            + "AS min_date FROM product_sold;");

        ResultSet minDateRs = minDatePs.executeQuery();
        
        minDateRs.next();
        String minDateString = minDateRs.getString("min_date");
        System.out.println(minDateString);
        
        LocalDate minDate = LocalDate.parse(minDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        LocalDate startDate = minDate;
        LocalDate endDate = today;

        // Check if the time span between the minimum date and the current date is less than one year
        if (startDate.plusYears(1).isAfter(endDate)) {
            JOptionPane.showMessageDialog(rootPane, "Not enough data available to generate yearly report.", 
                    "Insufficient Data", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method if there's not enough data
        }

       PreparedStatement ps = con.prepareStatement("SELECT YEAR(STR_TO_DATE(date_sold, '%d/%m/%Y')) AS year, " +
                                             "MONTH(STR_TO_DATE(date_sold, '%d/%m/%Y')) AS month, " +
                                             "SUM(quantity * price_per_quantity) AS total_amount " +
                                             "FROM product_sold " +
                                             "WHERE STR_TO_DATE(date_sold, '%d/%m/%Y') BETWEEN ? AND ? " +
                                             "GROUP BY YEAR(STR_TO_DATE(date_sold, '%d/%m/%Y')), MONTH(STR_TO_DATE(date_sold, '%d/%m/%Y'))");



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = startDate.format(formatter);
        String endDateString = endDate.format(formatter);

        ps.setString(1, startDateString);
        ps.setString(2, endDateString);
        ResultSet rs = ps.executeQuery();

        Map<String, Double> yearlySalesData = new TreeMap<>();

while (rs.next()) {
    int year = rs.getInt("year");
    int month = rs.getInt("month");
    double totalAmount = rs.getDouble("total_amount");
    
    String monthAbbreviation = Month.of(month).name().substring(0, 3);
    String yearLabel = monthAbbreviation + " " + year;
    
    // Add a leading zero to the year to ensure proper sorting
    yearLabel = monthAbbreviation + " " +String.format("%04d", year);

    yearlySalesData.put(yearLabel, totalAmount);
}

for (Map.Entry<String, Double> entry : yearlySalesData.entrySet()) {
    String yearLabel = entry.getKey();
    double totalAmount = entry.getValue();
    chart.addData(new ModelChart(yearLabel, new double[]{totalAmount}));
}



        chart.start();
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DailyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DailyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DailyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DailyReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DailyReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.chart.CurveLineChart chart;
    private raven.chart.CurveLineChart curveLineChart1;
    private raven.panel.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables
}
