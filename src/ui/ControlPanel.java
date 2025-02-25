package ui;

import board.Board;
import util.Core;
import util.GameModel;
import util.Move;
import util.MoveLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static board.Board.undoMove;
import static util.MoveLogger.lastmovein;

public class ControlPanel extends JPanel implements Observer {

    private GameModel gameModel;

    private JButton undoButton;
    private JButton saveButton;
    private JButton loadButton;

    public ControlPanel(GameModel gameModel) {
        this.gameModel = gameModel;
        initialize();
        gameModel.addObserver(this);
    }

    private void initialize() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setLayout(new GridLayout(0, 1));

        undoButton = new JButton("Request Undo");
        undoButton.setEnabled(true);
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // [TODO] 이 부분에 undo 관련 로직 들어가야함...
                gameModel.undoMove();
            }
        });
        saveButton = new JButton("Save Game");
        //[FIXME]
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MoveLogger.lastmoveout();
            }
        });
        saveButton.setEnabled(true);
        loadButton = new JButton("Load Game");
        loadButton.setEnabled(true);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModel.initialize();

                ArrayList<Move> loadmovelist = lastmovein();

                for (int i = 0; i < loadmovelist.size(); i++) {
                    gameModel.executeMove(loadmovelist.get(i));

                }
            }
        });

        this.add(undoButton);
        this.add(saveButton);
        this.add(loadButton);
        this.setPreferredSize(new Dimension(300, 200));
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
