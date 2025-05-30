/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.victorvilar.contaspagar.configuration;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import javax.swing.UIManager;

/**
 *
 * @author victor
 */
public abstract class LookAndFellConfiguration {
    
        public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Table.showHorizontalLines",true);
            UIManager.put("Table.alternateRowColor", Color.decode("#f7f3f2"));

        } catch (Exception ex) {
            System.out.println(ex);

        }
    }
}
