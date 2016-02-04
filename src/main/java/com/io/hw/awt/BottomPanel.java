package com.io.hw.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public class BottomPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private JProgressBar pb;
	Timer time;
	int counter = 0;

	public Timer getTime() {
		return time;
	}

	public void setTime(Timer time) {
		this.time = time;
	}

	public BottomPanel() {
		
	}

	public void continueTime() {
		if (time.isRunning()) {
			time.start();
		} else {
			time.restart();
		}
	}

	public void startProgress() {
		counter = 0;
		pb.setValue(counter);
		time.restart();
	}

	public void stopProgress() {
		if (time.isRunning()) {
			// this.pb.set;
			int counter = 0;
			pb.setValue(counter);
			time.stop();
		}
	}

	public void setProcessBar(BoundedRangeModel rangeModel) {

		pb.setModel(rangeModel);

	}

    @Override
    public void run()
    {
        pb = new JProgressBar();
        pb.setPreferredSize(new Dimension(680, 20));

        time = new Timer(10, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                counter++;

                pb.setValue(counter);

                Timer t = (Timer) e.getSource();

                if (counter == pb.getMaximum()) {

                    t.stop();

                    counter = 0;

                    t.start();

                }

            }

        });
        time.stop();

        /*
         * pb.addMouseListener(new MouseAdapter() {
         * 
         * @Override public void mouseClicked(MouseEvent e) { //
         * System.out.println("enter"); time.stop(); super.mouseClicked(e); }
         * 
         * @Override public void mouseEntered(MouseEvent e) {
         * super.mouseEntered(e);
         * 
         * }});
         */
        /*
         * pb.addMouseWheelListener(new MouseWheelListener() {
         * 
         * @Override public void mouseWheelMoved(MouseWheelEvent e) {
         * time.start();
         * 
         * } });
         */
        pb.setStringPainted(true);
        pb.setMinimum(0);
        pb.setMaximum(500);
        pb.setBackground(Color.white);
        pb.setForeground(Color.red);
        this.add(pb);
        
    }

}