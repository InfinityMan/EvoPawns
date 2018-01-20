/*
 * Copyright (C) 2018 Dmig
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
import ru.dmig.pawns.Evolution;
import ru.dmig.pawns.Game;

/**
 *
 * @author Dmig
 */
public final class PawnChange extends javax.swing.JFrame {

    /**
     * Creates new form PawnChange
     */
    public PawnChange() {
        initComponents();
    }

    public static void init() {
        java.awt.EventQueue.invokeLater(() -> {
            new VarChange().setVisible(true);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pawnL = new javax.swing.JLabel();
        pawnI = new javax.swing.JTextField();
        turnL = new javax.swing.JLabel();
        turnI = new javax.swing.JTextField();
        info1L = new javax.swing.JLabel();
        info2L = new javax.swing.JLabel();
        inputI = new javax.swing.JTextField();
        inputL = new javax.swing.JLabel();
        firstL = new javax.swing.JLabel();
        firstI = new javax.swing.JTextField();
        secondL = new javax.swing.JLabel();
        secondI = new javax.swing.JTextField();
        outputL = new javax.swing.JLabel();
        outputI = new javax.swing.JTextField();
        finishB = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pawn config");
        setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        setResizable(false);

        pawnL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        pawnL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pawnL.setText("Pawn amount:");

        pawnI.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        pawnI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pawnI.setText("000");
        pawnI.setMaximumSize(new java.awt.Dimension(6, 24));

        turnL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        turnL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        turnL.setText("Turn amount:");

        turnI.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        turnI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        turnI.setText("000");

        info1L.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        info1L.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info1L.setText("Pawn amount % 4 = 0");

        info2L.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        info2L.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info2L.setText("Pawn amount % Turn amount = 0");

        inputI.setEditable(false);
        inputI.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        inputI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputI.setText("0");

        inputL.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        inputL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inputL.setText("Input layer");

        firstL.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        firstL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        firstL.setText("First hidden");

        firstI.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        firstI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        firstI.setText("0");

        secondL.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        secondL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        secondL.setText("Second hidden");

        secondI.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        secondI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        secondI.setText("0");

        outputL.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        outputL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        outputL.setText("Ouput layer");

        outputI.setEditable(false);
        outputI.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        outputI.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        outputI.setText("0");

        finishB.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        finishB.setText("Finish");
        finishB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(finishB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(info1L, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(turnL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pawnL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(turnI)
                                        .addComponent(pawnI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(info2L, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(inputI, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(inputL, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(firstI, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(firstL, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(secondI, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(secondL, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(outputI, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(outputL, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pawnL, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pawnI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(turnL)
                    .addComponent(turnI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(info1L)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(info2L)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(firstL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(firstI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(secondL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(secondI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(outputL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(outputI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(finishB)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void finishBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishBActionPerformed
        getValues();
        Game.restartGame();
        dispose();
    }//GEN-LAST:event_finishBActionPerformed

    public void setupValues() {
        pawnI.setText(Game.AMOUNT_OF_PAWNS + "");
        turnI.setText(Game.TURN_PAWN_AMOUNT + "");
        inputI.setText(Game.LAYERS_OF_NET[0] + "");
        firstI.setText(Game.LAYERS_OF_NET[1] + "");
        secondI.setText(Game.LAYERS_OF_NET[2] + "");
        outputI.setText(Game.LAYERS_OF_NET[3] + "");
    }

    public void getValues() {
        int p = Integer.valueOf(pawnI.getText());
        int t = Integer.valueOf(turnI.getText());
        int i = Integer.valueOf(inputI.getText());
        int f = Integer.valueOf(firstI.getText());
        int s = Integer.valueOf(secondI.getText());
        int o = Integer.valueOf(outputI.getText());

        if (testValues(p, t, i, f, s, o)) {
            Game.AMOUNT_OF_PAWNS = p;
            Game.TURN_PAWN_AMOUNT = t;
            
            final int[] layers = {i, f, s, o};
            Game.LAYERS_OF_NET = layers;
        } else {
            Game.showMessage("Invalid values!", true);
        }
    }

    public boolean testValues(int pawn, int turn, int i, int f, int s, int o) {
        if (!(pawn > 0 && pawn <= 1000 && pawn % 4 == 0)) {
            return false;
        }
        if (!(turn > 0 && turn <= 1000 && pawn % turn == 0)) {
            return false;
        }
        if (!(i > 0 && f > 0 && s > 0 && o > 0)) {
            return false;
        }
        if (!(i <= 100 && f <= 100 && s <= 100 && o <= 100)) {
            return false;
        }
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton finishB;
    private javax.swing.JTextField firstI;
    private javax.swing.JLabel firstL;
    private javax.swing.JLabel info1L;
    private javax.swing.JLabel info2L;
    private javax.swing.JTextField inputI;
    private javax.swing.JLabel inputL;
    private javax.swing.JTextField outputI;
    private javax.swing.JLabel outputL;
    private javax.swing.JTextField pawnI;
    private javax.swing.JLabel pawnL;
    private javax.swing.JTextField secondI;
    private javax.swing.JLabel secondL;
    private javax.swing.JTextField turnI;
    private javax.swing.JLabel turnL;
    // End of variables declaration//GEN-END:variables
}
