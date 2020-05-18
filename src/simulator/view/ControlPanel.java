package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFileChooser fc;
	private JButton _exit;

	JButton _run = new JButton();
	boolean _stopped;
	
	private Controller _ctrl;
	private AbstractButton load;
	private AbstractButton contClass;
	private AbstractButton weather;
	private AbstractButton run;
	ChangeWeatherDialog w;
	ChangeCO2ClassDialog c;
	
	protected ControlPanel (JFrame panel, Controller cont) {
		
		JPanel panel = new JPanel (new BorderLayout());

		JToolBar toolBar = new JToolBar();
		panel.add(toolBar, BorderLayout.PAGE_START);
		
		JButton file =  new JButton();
		file.setIcon(new ImageIcon("icons/open.png"));
		file.add(File(null), BorderLayout.WEST);
		
		JButton co2D =  new JButton();
		co2D.setIcon(new ImageIcon("icons/co2class.png"));
		co2D.setAction(c.ChangeCO2ClassDialog(panel,cont));
		
		JButton weather =  new JButton();
		weather.setIcon(new ImageIcon("icons/weather.png"));
		weather.setAction(w.ChangeWeatherDialog(panel, cont));
		
		JButton run = new JButton();
		run.setIcon(new ImageIcon("icons/run.png"));
		
		JButton stop =new JButton();
		stop.setIcon(new ImageIcon("stop.png"));
		
		JSpinner ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		
		JButton exit = new JButton();
		exit.setIcon(new ImageIcon("icons/exit.png"));
		
		this.setVisible(true);
	}
	
	public Component File (ActionEvent e) {
		
		Controller c = new Controller(null, null);
		InputStream i = null;
		
		if (e.getSource() == this.fc) {
			int v = fc.showOpenDialog(null);
			if (v==JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//si el fichero no existe o loadEvent...
				if (file == null) {
					//throw new IllegalArgumentException("Not valid road parameters");
					JOptionPane.showMessageDialog(null, "FILE DOESN�T EXIST!!", null, JOptionPane.ERROR_MESSAGE);
					// se supone que hay q lanzar una excepcion con un message dialog
				}
				else {
					file.getName();
					c.reset();
					c.loadEvents(i);
				}
			}
		}
		return fc;	
	}
	
	@SuppressWarnings("unused")
	private void run_sim(int n){
		
		if (n > 0 && !_stopped) {
			try {
				enableToolBar(false);
				_ctrl.run(1, null);
			}
			catch (Exception e) {
				// TODO show error message
				JOptionPane.showMessageDialog(ControlPanel.this, e.getMessage());
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}});
			} 
		else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	@SuppressWarnings("unused")
	private void stop() {
		_stopped = true;
	}
	
	private void enableToolBar(boolean enable) {
		if (enable == true) {
			load.setEnabled(enable);
			((AbstractButton) contClass).setEnabled(enable);
			((AbstractButton) weather).setEnabled(enable);
			((AbstractButton) run).setEnabled(enable);
		}
		else {
			load.setEnabled(enable);
			((AbstractButton) contClass).setEnabled(enable);
			((AbstractButton) weather).setEnabled(enable);
			((AbstractButton) run).setEnabled(enable);	
		}
	}
	
	//salida del simuador
	public void ExitButton() {
        setLayout(null);
        _exit = new JButton("Exit");
        _exit.setBounds(300,250,100,30);
        add(_exit);
        _exit.addActionListener((ActionListener) this);
        actionPerformed(null);
   }
   public void actionPerformed(ActionEvent e) {
	   if (e.getSource() == _exit) {
		   System.exit(0);
       }
   }

	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
