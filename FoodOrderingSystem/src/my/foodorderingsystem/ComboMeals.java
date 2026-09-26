/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.foodorderingsystem;

/**
 *
 * @author Mike
 */
public class ComboMeals {
    String comboname;
    double comboprice;
    public ComboMeals(String name, double price)
    {
        comboname=name;
        comboprice=price;
    }
    
    public void setData(String name, double price){
        comboname=name;
        comboprice=price;
    }
    
    public String getName() {
        return comboname;
    }
    
    public double getPrice() {
        return comboprice;
    }
}
