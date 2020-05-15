package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.util.Pair;
import simulator.control.ControlPanel;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class StatusBar extends JPanel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void StatusBarPanel (ControlPanel ticks) {
		JPanel status = new JPanel(new BorderLayout());
		status.add(TimeAndEvents(), BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public Component TimeAndEvents() {
		JPanel te = new JPanel(new BorderLayout());
		te.add(SetTime(null),BorderLayout.WEST);
		te.add(SetEvent(null, null), BorderLayout.EAST);
		return te;
	}

	
	private Component SetEvent(ChangeWeatherDialog cw, ChangeCO2ClassDialog co){
		cw = new ChangeWeatherDialog();
		co = new ChangeCO2ClassDialog();
		
		if (((Object) cw.OkButton(paramString())).isSelected()) {
			System.out.print("Event added:" + cw.getName());
		}
		else if (((Object) co.OkButton(paramString())).isSelected()) {
			System.out.print("Event added:" + co.getName());
		}
		return null;
	}
	
	private Component SetTime(ControlPanel e) {
		return null;
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
