package ui;

import util.GameModel;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

public class TimerPanel extends JPanel implements Observer {

    private GameModel gameModel;
    public Time whiteTime;
    public Time blackTime;

    private JPanel displayPanel;
    private JPanel whiteTimerPanel;
    private JPanel whiteTimerDigitsPanel;
    private JLabel whiteTimerDigitsLabel;
    private JPanel whiteTimerStatusPanel;
    private JPanel blackTimerPanel;
    private JPanel blackTimerDigitsPanel;
    private JLabel blackTimerDigitsLabel;
    private JPanel blackTimerStatusPanel;

    public TimerPanel(GameModel gameModel) {
        super(new BorderLayout());
        this.gameModel = gameModel;
        whiteTime = Time.valueOf("00:00:00");
        blackTime = Time.valueOf("00:00:00");
        initialize();
        gameModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void whiteTimerTikTok() {
        /*
        [FIXME]-timer
            Update whiteTime
            Update whiteDigitsLabel
            Show whiteTimerStatusPanel
            Blind blackTimerStatusPanel
         */
        /* In GameModel.initializeTimers(), it calls this method every 1 sec
        * */
        proceedTime(whiteTime);
        whiteTimerDigitsLabel.setText(whiteTime.toString());
        whiteTimerStatusPanel.setVisible(true);
        blackTimerStatusPanel.setVisible(false);
    }

    public void blackTimerTikTok() {
        // [FIXME]-timer: same with whiteTimerTikTok
        proceedTime(blackTime);
        blackTimerDigitsLabel.setText(blackTime.toString());
        blackTimerStatusPanel.setVisible(true);
        whiteTimerStatusPanel.setVisible(false);
    }

    private void proceedTime(Time t) {
        t.setTime(t.getTime() + 1000);
    }

    //[FIXME] - countdown
    private void countdown(Time t) {t.setTime(t.getTime()-1000);}


//[FIXME]
    public void whiteTimerDown() {
        countdown(whiteTime);
        whiteTimerDigitsLabel.setText(whiteTime.toString());
        whiteTimerStatusPanel.setVisible(true);
        blackTimerStatusPanel.setVisible(false);
    }

    public void blackTimerDown() {
        countdown(blackTime);
        blackTimerDigitsLabel.setText(blackTime.toString());
        blackTimerStatusPanel.setVisible(true);
        whiteTimerStatusPanel.setVisible(false);
    }






    private void initialize() {
        whiteTimerDigitsLabel = new JLabel(whiteTime.toString());
        whiteTimerDigitsLabel.setFont(whiteTimerDigitsLabel.getFont().deriveFont(48f));
        whiteTimerDigitsPanel = new JPanel();
        whiteTimerDigitsPanel.add(whiteTimerDigitsLabel);
        whiteTimerStatusPanel = new JPanel();
        whiteTimerStatusPanel.setBackground(Color.WHITE);
        whiteTimerPanel = new JPanel(new BorderLayout());
        whiteTimerPanel.add(whiteTimerDigitsPanel, BorderLayout.LINE_START);
        whiteTimerPanel.add(whiteTimerStatusPanel, BorderLayout.CENTER);
        whiteTimerPanel.setBorder(BorderFactory.createTitledBorder("White"));

        blackTimerDigitsLabel = new JLabel(blackTime.toString());
        blackTimerDigitsLabel.setFont(blackTimerDigitsLabel.getFont().deriveFont(48f));
        blackTimerDigitsPanel = new JPanel();
        blackTimerDigitsPanel.add(blackTimerDigitsLabel);
        blackTimerStatusPanel = new JPanel();
        blackTimerStatusPanel.setBackground(Color.BLACK);
        blackTimerPanel = new JPanel(new BorderLayout());
        blackTimerPanel.add(blackTimerDigitsPanel, BorderLayout.LINE_START);
        blackTimerPanel.add(blackTimerStatusPanel, BorderLayout.CENTER);
        blackTimerPanel.setBorder(BorderFactory.createTitledBorder("Black"));

        displayPanel = new JPanel(new GridLayout(2, 1));
        displayPanel.add(whiteTimerPanel);
        displayPanel.add(blackTimerPanel);

        this.add(displayPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(300, 200));
    }

}
