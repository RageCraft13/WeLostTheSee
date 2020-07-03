package gameSystem;

import java.time.format.DateTimeFormatter;
import java.util.*;

import gameSystem.exceptions.*;
import player.*;
import items.*;
import locations.*;

public class GameSystemClass implements GameSystem {
	
	private List<Location> map;
	private Player player;
	private String timePlayed;
	private int timePlayedMinutes, timePlayedHours;
	
	public GameSystemClass() {
		map = new ArrayList<Location>(Arrays.asList(new BedRoom()));
		
		timePlayedMinutes=0;
		timePlayedHours=0;
	}
	
	DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	Timer timer = new Timer();
	TimerTask taskMinutes = new TimerTask() {
		public void run() {
			if(timePlayedMinutes<59)
				timePlayedMinutes++;
			else {
				timePlayedMinutes=0;
				timePlayedHours++;
			}	
		}
	};
	
	public void startTimer() {
		timer.scheduleAtFixedRate(taskMinutes, 60000, 60000);
	}
	
	public void newPlayer(String name) throws TooLongNameException {
		if(name.length()>22) throw new TooLongNameException();
		player = new PlayerClass(name, map.get(0));
	}
	
	public String timePlayed() {
		String strTimePlayedMinutes;
		if(timePlayedMinutes<10)
			strTimePlayedMinutes = "0" + timePlayedMinutes;
		else
			strTimePlayedMinutes = Integer.toString(timePlayedMinutes);
		timePlayed = timePlayedHours+":"+strTimePlayedMinutes;
		return timePlayed;
	}
	
	public void teleportToLocation(Location location) {
		player.setLocation(location);
	}
	
	public void getItem(String items) {
		String str[] = items.split(" ");
		List<Item> itemList = new LinkedList<Item>();
		List<String> itemsType = new LinkedList<String>(); 
		
		for(String item: str) itemsType.add(item);
		
		Iterator<Item> it = getCurrentLocation().getItem(itemsType);
		
		while(it.hasNext())
			itemList.add(it.next());
		
		player.getItem(itemList);
	}
	
	public Iterator<ArrayList<Item>> listBag() {
		return player.listBag();
	}
	
	public Location getCurrentLocation() {
		return player.getLocation();
	}
	
	public String getPlayerName() {
		return player.getName();
	}
}