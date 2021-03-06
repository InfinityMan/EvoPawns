/*
 * Copyright (C)2017-2018 Dmig
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
import javax.swing.JOptionPane;
import ru.dmig.pawns.Evolution;
import ru.dmig.pawns.Game;

/**
 * Class of GUI for changing main variables (Game and Pawn classes)
 *
 * @author Dmig
 */
public final class VarChange extends javax.swing.JFrame {

    public static enum ValueType {
        CYCLE, FOOD, KILLER, MASS, MUTATE
    };

    public static void init() {
        java.awt.EventQueue.invokeLater(() -> {
            new VarChange().setVisible(true);
        });
    }

    /**
     * Creates new form VarChange
     */
    public VarChange() {
        initComponents();
        setupUpdater();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cycleP = new javax.swing.JPanel();
        cycleAL = new javax.swing.JLabel();
        cycleAC = new javax.swing.JButton();
        foodP = new javax.swing.JPanel();
        foodAL = new javax.swing.JLabel();
        foodAC = new javax.swing.JButton();
        killerP = new javax.swing.JPanel();
        killerAL = new javax.swing.JLabel();
        killerAC = new javax.swing.JButton();
        massP = new javax.swing.JPanel();
        massAL = new javax.swing.JLabel();
        massAC = new javax.swing.JButton();
        mutateP = new javax.swing.JPanel();
        mutateAL = new javax.swing.JLabel();
        mutateAC = new javax.swing.JButton();
        pawnConfigB = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Globals");
        setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        setName("globals"); // NOI18N
        setResizable(false);

        cycleAL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        cycleAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cycleAL.setText("Cycle amount: 00k");

        cycleAC.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        cycleAC.setText("Change");
        cycleAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cycleACActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cyclePLayout = new javax.swing.GroupLayout(cycleP);
        cycleP.setLayout(cyclePLayout);
        cyclePLayout.setHorizontalGroup(
            cyclePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cyclePLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cycleAL, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cycleAC)
                .addContainerGap())
        );
        cyclePLayout.setVerticalGroup(
            cyclePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cycleAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cycleAC, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        foodAL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        foodAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foodAL.setText("Food amount: 000");

        foodAC.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        foodAC.setText("Change");
        foodAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodACActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout foodPLayout = new javax.swing.GroupLayout(foodP);
        foodP.setLayout(foodPLayout);
        foodPLayout.setHorizontalGroup(
            foodPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(foodPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(foodAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(foodAC)
                .addContainerGap())
        );
        foodPLayout.setVerticalGroup(
            foodPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(foodAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(foodAC, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        killerAL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        killerAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        killerAL.setText("Kilr amount: 000");
        killerAL.setToolTipText("");

        killerAC.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        killerAC.setText("Change");
        killerAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                killerACActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout killerPLayout = new javax.swing.GroupLayout(killerP);
        killerP.setLayout(killerPLayout);
        killerPLayout.setHorizontalGroup(
            killerPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(killerPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(killerAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(killerAC)
                .addContainerGap())
        );
        killerPLayout.setVerticalGroup(
            killerPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(killerAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(killerAC, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        massAL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        massAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        massAL.setText("Mass tax dist: 00");
        massAL.setToolTipText("");

        massAC.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        massAC.setText("Change");
        massAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                massACActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout massPLayout = new javax.swing.GroupLayout(massP);
        massP.setLayout(massPLayout);
        massPLayout.setHorizontalGroup(
            massPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(massPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(massAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(massAC)
                .addContainerGap())
        );
        massPLayout.setVerticalGroup(
            massPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(massAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(massAC, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        mutateAL.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mutateAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mutateAL.setText("Mutate rate: 00");
        mutateAL.setToolTipText("");

        mutateAC.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mutateAC.setText("Change");
        mutateAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mutateACActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mutatePLayout = new javax.swing.GroupLayout(mutateP);
        mutateP.setLayout(mutatePLayout);
        mutatePLayout.setHorizontalGroup(
            mutatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mutatePLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mutateAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mutateAC)
                .addContainerGap())
        );
        mutatePLayout.setVerticalGroup(
            mutatePLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mutateAL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mutateAC, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        pawnConfigB.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        pawnConfigB.setText("Change pawn config");
        pawnConfigB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pawnConfigBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cycleP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(foodP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(killerP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(massP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mutateP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pawnConfigB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pawnConfigB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cycleP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foodP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(killerP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(massP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mutateP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cycleACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cycleACActionPerformed
        Game.CYCLE_AMOUNT = (int) getUserValue(ValueType.CYCLE);
    }//GEN-LAST:event_cycleACActionPerformed

    private void foodACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodACActionPerformed
        Game.setFoodAmount((int) getUserValue(ValueType.FOOD));
    }//GEN-LAST:event_foodACActionPerformed

    private void killerACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_killerACActionPerformed
        Game.setKillerAmount((int) getUserValue(ValueType.KILLER));
    }//GEN-LAST:event_killerACActionPerformed

    private void massACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_massACActionPerformed
        Game.MASS_MOVE_TAX = getUserValue(ValueType.MASS);
    }//GEN-LAST:event_massACActionPerformed

    private void mutateACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mutateACActionPerformed
        Evolution.MUTATION_RATE = (int) getUserValue(ValueType.MUTATE);
    }//GEN-LAST:event_mutateACActionPerformed

    private void pawnConfigBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pawnConfigBActionPerformed
        PawnChange.init();
    }//GEN-LAST:event_pawnConfigBActionPerformed

    public static boolean ignorePawnWarning() {
        int code = JOptionPane.showConfirmDialog(null, "Warning! If you continue, you break all previous progress!\nCurrent progress will be saved.", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return code == JOptionPane.YES_OPTION;
    }

    public static double getUserValue(ValueType valueType) {
        double newValue;
        String subject;
        switch (valueType) {
            case CYCLE:
                subject = "cycle ";
                break;
            case FOOD:
                subject = "food ";
                break;
            case KILLER:
                subject = "killer ";
                break;
            case MASS:
                subject = " mass tax distance";
                break;
            case MUTATE:
                subject = " mutation rate";
                break;
            default:
                subject = "";
                throw new AssertionError();
        }
        if (valueType != ValueType.MASS && valueType != ValueType.MUTATE) {
            subject += "amount";
        }
        while (true) {
            try {
                newValue = Game.askUserValue("Enter a new " + subject + ".");
                if (newValue % 1 != 0) {
                    throw new NumberFormatException();
                }
                switch (valueType) {
                    case CYCLE:
                        if (newValue >= 1000 && newValue % 1000 == 0) {
                            return newValue;
                        } else {
                            throw new NumberFormatException();
                        }
                    case FOOD:
                        if (newValue > 0) {
                            return newValue;
                        } else {
                            throw new NumberFormatException();
                        }
                    case KILLER:
                        if (newValue >= 0) {
                            return newValue;
                        } else {
                            throw new NumberFormatException();
                        }
                    case MASS:
                        if (newValue > 0) {
                            return newValue;
                        } else {
                            throw new NumberFormatException();
                        }
                    case MUTATE:
                        if (newValue >= 0 && newValue <= 100) {
                            return newValue;
                        } else {
                            throw new NumberFormatException();
                        }
                    default:
                        throw new AssertionError();
                }
            } catch (NumberFormatException ex) {
                System.out.println("!Problem with entering new " + subject);
            }
        }
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

    public void update() {
        updateLabels(Game.CYCLE_AMOUNT, Game.FOOD_AMOUNT,
                Game.KILLER_AMOUNT, Game.MASS_MOVE_TAX,
                Evolution.MUTATION_RATE);
    }

    protected void updateLabels(double cycle,
            double food, double killers, double mass, double mutate) {
        cycleAL.setText("Cycle amount: " + (cycle / 1000) + "k");
        foodAL.setText("Food amount: " + food);
        killerAL.setText("Kilr amount: " + killers);
        massAL.setText("Mass tax dist: " + mass);
        mutateAL.setText("Mutate rate: " + mutate);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cycleAC;
    private javax.swing.JLabel cycleAL;
    private javax.swing.JPanel cycleP;
    private javax.swing.JButton foodAC;
    private javax.swing.JLabel foodAL;
    private javax.swing.JPanel foodP;
    private javax.swing.JButton killerAC;
    private javax.swing.JLabel killerAL;
    private javax.swing.JPanel killerP;
    private javax.swing.JButton massAC;
    private javax.swing.JLabel massAL;
    private javax.swing.JPanel massP;
    private javax.swing.JButton mutateAC;
    private javax.swing.JLabel mutateAL;
    private javax.swing.JPanel mutateP;
    private javax.swing.JButton pawnConfigB;
    // End of variables declaration//GEN-END:variables
}
