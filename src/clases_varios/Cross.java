/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases_varios;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Console;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sonomusic.frm_principal;

/**
 *
 * @author Mariela
 */
public class Cross extends JPanel {

    private JLabel L;
    private JButton B;
    private int size = 10;

    public Cross(String title) {
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        L = new JLabel(title + " ");
        B = new JButton();
        B.setPreferredSize(new java.awt.Dimension(10, 10));
        B.setIcon(getImage());
        //Listener para cierre de tabs con acceso estatico al `JTabbedPane`
        //B.addActionListener(frm_principal.jTabbedPane1.removeTabAt(frm_principal.jTabbedPane1.indexOfTab(title)));
        B.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frm_principal.jTabbedPane2.removeTabAt(frm_principal.jTabbedPane2.indexOfTab(title));
            }
        });
        add(L, gbc);
        gbc.gridx++;
        gbc.weightx = 0;
        add(B, gbc);
    }

    private ImageIcon getImage() {
        java.awt.Image IMG = null;
        try {
            IMG = new ImageIcon(getClass().getResource("/icons/cross.png")).getImage();
            IMG = IMG.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImageIcon(IMG);
    }
}
