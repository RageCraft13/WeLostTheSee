package entity;
import java.util.*;

import entity.exceptions.EmpetyBagException;
import entity.exceptions.ItemNotInBagException;
import locations.*;
import objects.*;

public class PlayerClass extends EntityClass implements Player {

	private static final int MONEY_DEFAULT = 100, ACTION = 0;
	
	private int itemsGathered;
	
	public PlayerClass(String name, Location location) {
		super(name, location, MONEY_DEFAULT, ACTION);
	}
	
	public int getQuantity(String item) {
		ArrayList<Item> items = bag.get(item);
		if(items==null) throw new ItemNotInBagException(item);
		
		return items.size();
	}
	
	public void upgradeBag(int size) {
		Map<String, ArrayList<Item>> newBag = new HashMap<String, ArrayList<Item>>(size);
		newBag.putAll(bag);
		bag = newBag;
			
	}
	
	public Iterator<ArrayList<Item>> listBag() {
		Iterator<ArrayList<Item>> it = bag.values().iterator();
		if(!it.hasNext()) throw new EmpetyBagException();
		return it;
	}
	
	public int itemsGathered() {
		return itemsGathered;
	}
	
}