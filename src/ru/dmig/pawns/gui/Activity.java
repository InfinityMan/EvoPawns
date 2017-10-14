/*
 * Copyright (C) 2017 Dmig
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.dmig.pawns.gui;

import ru.dmig.pawns.Game;

/**
 *
 * @author Dmig
 */
public class Activity extends javax.swing.JFrame {
    
    private float gameSpeed = 2;
    
    public static void init() {
        java.awt.EventQueue.invokeLater(() -> {
            new Activity().setVisible(true);
        });
    }

    /**
     * Creates new form Activity
     */
    public Activity() {
        initComponents();
    }
    
    public void update() {
        
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        speedPanel = new javax.swing.JPanel();
        curSpeedL = new javax.swing.JLabel();
        speedPlus = new javax.swing.JButton();
        speedMinus = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        genL = new javax.swing.JLabel();
        fitL = new javax.swing.JLabel();
        avgL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        curSpeedL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        curSpeedL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        curSpeedL.setText("Current speed: 1x");

        speedPlus.setText("+");
        speedPlus.setMaximumSize(new java.awt.Dimension(70, 25));
        speedPlus.setMinimumSize(new java.awt.Dimension(70, 25));
        speedPlus.setPreferredSize(new java.awt.Dimension(70, 25));
        speedPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedPlusActionPerformed(evt);
            }
        });

        speedMinus.setText("-");
        speedMinus.setMaximumSize(new java.awt.Dimension(70, 25));
        speedMinus.setMinimumSize(new java.awt.Dimension(70, 25));
        speedMinus.setPreferredSize(new java.awt.Dimension(70, 25));
        speedMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedMinusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout speedPanelLayout = new javax.swing.GroupLayout(speedPanel);
        speedPanel.setLayout(speedPanelLayout);
        speedPanelLayout.setHorizontalGroup(
            speedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(speedPanelLayout.createSequentialGroup()
                .addGroup(speedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(speedPanelLayout.createSequentialGroup()
                        .addComponent(speedPlus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(speedMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(curSpeedL, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        speedPanelLayout.setVerticalGroup(
            speedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(speedPanelLayout.createSequentialGroup()
                .addComponent(curSpeedL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(speedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(speedPlus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(speedMinus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        genL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        genL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        genL.setText("Generation: ");

        fitL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        fitL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fitL.setText("Fittest: ");

        avgL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        avgL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avgL.setText("Average: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(genL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(fitL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(avgL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(genL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fitL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(avgL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(speedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(speedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void speedPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedPlusActionPerformed
        if(Game.TICK_DURATION > 4 && Game.DURATION_OF_ROUND > 3) {
            Game.TICK_DURATION = Game.TICK_DURATION / 2;
            Game.DURATION_OF_ROUND = Game.DURATION_OF_ROUND / 2;
            gameSpeed = gameSpeed*2;
        }
    }//GEN-LAST:event_speedPlusActionPerformed

    private void speedMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedMinusActionPerformed
        Game.TICK_DURATION = Game.TICK_DURATION * 2;
        Game.DURATION_OF_ROUND = Game.DURATION_OF_ROUND * 2;
        gameSpeed = gameSpeed/2;
    }//GEN-LAST:event_speedMinusActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avgL;
    private javax.swing.JLabel curSpeedL;
    private javax.swing.JLabel fitL;
    private javax.swing.JLabel genL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton speedMinus;
    private javax.swing.JPanel speedPanel;
    private javax.swing.JButton speedPlus;
    // End of variables declaration//GEN-END:variables
}