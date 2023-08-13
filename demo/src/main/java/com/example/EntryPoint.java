package com.example;

import javax.swing.UIManager;

public class EntryPoint {
    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        }
        catch(Exception e){
            e.printStackTrace();
        }
       
        new GUI().setVisible(true);
    }
}
