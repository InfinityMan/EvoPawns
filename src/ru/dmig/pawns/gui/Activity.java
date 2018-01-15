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

import java.util.Timer;
import java.util.TimerTask;
import ru.dmig.pawns.Game;
import ru.epiclib.base.Base;

/**
 *
 * @author Dmig
 */
public final class Activity extends javax.swing.JFrame {
    
    public static double fittest = 0;
    public static double avg = 0;
    
    public static double killerKilled = 0;
    public static double borderKilled = 0;
    public static double starveKilled = 0;
    
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
        setupUpdater();
    }
    
    public void update() {
        String speed = (Game.TICK_DURATION == 0 ? "max" : (Game.TICK_DURATION + " tks"));
        curSpeedL.setText("Current speed: " + speed);
        genL.setText("Generation: " + (Game.generation - 1));
        fitL.setText("Fittest: " + Base.maximumFractionDigits(2, fittest));
        avgL.setText("Average: " + Base.maximumFractionDigits(2, avg));
        killKL.setText("Killer: " + Base.maximumFractionDigits(0, killerKilled * 100) + " %");
        bordKL.setText("Border: " + Base.maximumFractionDigits(0, borderKilled * 100) + " %");
        strvKL.setText("Starve: " + Base.maximumFractionDigits(0, starveKilled * 100) + " %");
    }
    
    public void setupUpdater() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 100, 100);
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
        killKL = new javax.swing.JLabel();
        bordKL = new javax.swing.JLabel();
        strvKL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Activity");

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

        killKL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        killKL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        killKL.setText("Killer: ");

        bordKL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        bordKL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bordKL.setText("Border: ");

        strvKL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        strvKL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        strvKL.setText("Starve: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(genL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(fitL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(avgL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(killKL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bordKL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(strvKL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(genL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fitL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(avgL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(killKL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bordKL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(strvKL, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        Game.upThread.changeSpeed(false);
        update();
    }//GEN-LAST:event_speedPlusActionPerformed

    private void speedMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedMinusActionPerformed
        Game.upThread.changeSpeed(true);
        update();
    }//GEN-LAST:event_speedMinusActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avgL;
    private javax.swing.JLabel bordKL;
    private javax.swing.JLabel curSpeedL;
    private javax.swing.JLabel fitL;
    private javax.swing.JLabel genL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel killKL;
    private javax.swing.JButton speedMinus;
    private javax.swing.JPanel speedPanel;
    private javax.swing.JButton speedPlus;
    private javax.swing.JLabel strvKL;
    // End of variables declaration//GEN-END:variables
}
