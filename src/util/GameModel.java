package util;

import board.Board;
import pieces.Piece;
import ui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.Observable;
import java.io.*;

import javax.sound.sampled.*;




public class GameModel extends Observable {

    private GameFrame gameFrame;
    private BoardPanel boardPanel;
    private TimerPanel timerPanel;
    private ControlPanel controlPanel;
    private MoveHistoryPanel moveHistoryPanel;

    private Timer whiteTimer;
    private Timer blackTimer;





    public GameModel() {
        initialize();
    }


    private void initialize() {
        switch(Core.getPreferences().getTimerMode()) {
            case COUNTDOWN:


                initializecountdownTimers();

                break;
            case STOPWATCH:
                initializeTimers();
                break;
        }

        initializeUIComponents();
    }

    public void onMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        onLocalMoveRequest(originFile, originRank, destinationFile, destinationRank);
    }

    private void onLocalMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        Move move = new Move(originFile, originRank, destinationFile, destinationRank);
        if (MoveValidator.validateMove(move)) {
            executeMove(move);
        } else {
            //
        }
    }

    private void executeMove(Move move) {
        MoveLogger.addMove(move);
        Board.executeMove(move);
        moveHistoryPanel.printMove(move);
        boardPanel.executeMove(move);

        switchTimer(move);
        if (MoveValidator.isCheckMove(move)) {
            if (MoveValidator.isCheckMate(move)) {
                stopTimer();
                gameFrame.showCheckmateDialog();
                Soundon("C:/Users/user/Downloads/gameover.wav");

            } else {
                gameFrame.showCheckDialog();
            }
        }
    }

    public Piece queryPiece(char file, int rank) {
        return Board.getSquare(file, rank).getCurrentPiece();
    }

    private void initializeUIComponents() {
        boardPanel = new BoardPanel(this);
        timerPanel = new TimerPanel(this);
        controlPanel = new ControlPanel(this);
        moveHistoryPanel = new MoveHistoryPanel(this);
        gameFrame = new GameFrame(this);
    }

    private void initializeTimers() {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.whiteTimerTikTok();
            }
        });
        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.blackTimerTikTok();
            }
        });
    }

    private void initializecountdownTimers() {
        //TODO



        whiteTimer = new Timer( 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.whiteTimerDown();
            }
        });

        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.blackTimerDown();
            }
        });
    }

    private void switchTimer(Move move) {
        /*
        [TODO] 각자 차례가 되면 다른 색깔 TIMER를 STOP하고 TIMER를 START한다. countdown인 경우 수정해줘야 한다.
         */
        if (move.getPiece().getColor() == Piece.Color.WHITE) {

            this.whiteTimer.stop();
            if (Core.getPreferences().getTimerMode() == Preferences.TimerMode.COUNTDOWN) {
                this.timerPanel.blackTime = Time.valueOf(Core.getPreferences().getTimeLimit()+":00:00");
            }

            this.blackTimer.start();
        }
        else {
            if (Core.getPreferences().getTimerMode() == Preferences.TimerMode.COUNTDOWN) {
                this.timerPanel.whiteTime = Time.valueOf(Core.getPreferences().getTimeLimit()+":00:00");
            }
            this.whiteTimer.start();
            this.blackTimer.stop();
        }
    }





    //[FIXME]
    public void Soundon(String file) {
        try {
            File soundFile = new File(file);
            final Clip clip = AudioSystem.getClip();
            clip.addLineListener(new LineListener()
            {
                @Override
                public void update(LineEvent event)
                {
                    if (event.getType() == LineEvent.Type.STOP)
                        clip.close();
                }
            });
            clip.open(AudioSystem.getAudioInputStream(soundFile));
            clip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void stopTimer() {
        // [FIXME] 두가지 타이머 다 STOP
        this.whiteTimer.stop();
        this.blackTimer.stop();

    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public TimerPanel getTimerPanel() {
        return timerPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public MoveHistoryPanel getMoveHistoryPanel() {
        return moveHistoryPanel;
    }


}


