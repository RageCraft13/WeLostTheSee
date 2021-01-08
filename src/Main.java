import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import entity.exceptions.*;
import gameSystem.*;
import gameSystem.exceptions.*;
import locations.exceptions.*;
import objects.*;

/**
 * Main Program.
 * @author Afonso Batista
 */
public class Main {
	/* Other Constants */
	private static final String YES = "YES", NO = "NO";
	private static final String YOUR_BAG = "\nYour Bag:";
	private static final int MAIN_SPEED = 65;
	
	/* Game lines */
	private static final String START_NEW_PLAYER = "\nSo... Whats your name?\n\n> ";
	private static final String START_IS_THAT_YOUR_NAME = "\nHi %s is that your name?\n Yes or No?\n\n> ";
	
	
	/* Success Constants */
	private static final String SUCCESS_START = "\nWelcome to your Adventure!",
		SUCCESS_MENU = "\n%s* We Lost The Sea *%s\n%s( Menu )%s\n**%s**\n*%s*\n%sStart\n%sInformation\n%sCredits\n%sExit\n%s(*Test*)\n*%s*\n**%s**\n\n",
		SUCCESS_INF = "\nWe Lost The Sea is a Text Adventure type Game, you just need to type what you want to do and create your own journey.\n"
						+ "\nAny command mismatch you can type ( Help ) to print a list of all the commands in game.\n"
						+ "\nYou can walk for any direction you want (if there aren't some type of object blocking your way), such as ( 'N' - North 'S' - South 'E' - East 'W' - West ).\n"
						+ "\nAnd a lot more to discover... Good luck Adventurer!!\n\n",
		SUCCESS_CREDITS = "\nWE LOST THE SEE\nA Text Adventure Game\nBy Afonso Batista ( 2019-2020 ) \nThanks for playing this game!\n\n",
		SUCCESS_DISCRIPTION_MODE_ON = "\nYour Adventure is now in description mode, which always gives bigger descriptions of locations.\n\n",
		SUCCESS_DESCRIPTION_MODE_OFF = "\nYour Adventure is no longer in description mode.\n\n",
		SUCCESS_MINIGAME = "\nInsert numbers between 0 and %d\nYou have %s guesses\n",
		SUCCESS_MYNAME = "\nYour Name is %s don't you remember ?\n\n",
		SUCCESS_GET_1 = "\nTaken.",
		SUCCESS_GET_2 = "\nYou have put %s in your Bag.",
		SUCCESS_GET_3 = "\nNice catch, %s!",
		SUCCESS_GET_4 = "\nYou get hit by a wave of laziness, causing you to refuse to pick up the item...\n",
		SUCCESS_DROP_1 = "\nYou have droped the item.",
		SUCCESS_DROP_2 = "\nNow %s are droped in the middle of %s.",
		SUCCESS_DROP_3 = "\nYou throw away as far as you can %s!",
		SUCCESS_DROP_4 = "\nBetween wind sounds %s says :\n\"hmmm now I fell lighter...\"",
		SUCCESS_ITEM_QUANTITY = "\nYou have %d of that item\n\n",
		SUCCESS_WALKING = "\nYou walked a couple of steps to the %s.\n",
		SUCCESS_SIT = "\nYou sat on a %s.\n\n",
		SUCCESS_LAY = "\nYou Lay down in %s.\n\n",
		SUCCESS_STAND = "\nYou stand up.\n",
		SUCCESS_PUT = "\nYou have put %s on %s.\n\n",
		SUCCESS_CLOSE = "\nYou have closed %s.\n\n",
		SUCCESS_PLAYER_MONEY = "\nYou have %.2f$ in your pocket.\n\n",
		SUCCESS_EXIT = "\nLeaving...";
	
	/* Error Constants*/
	private static final String ERROR_INVALID_COMMAND = "\nHoo man! That must be an encrypted type of language I don't understand!\n\n",
		ERROR_TOO_LONG_NAME = "\nWooow! Looks like your name is to big for me to handle... Try write a smaller one. (<22)\n",
		ERROR_OBJECT_NOT_IN_LOCATION = "\n%s doesn't exist in this location...",
		ERROR_NOT_AN_ITEM = "\nYou tried realy hard but, you couldn't put a %s in your bag...",
		ERROR_NO_SPACE = "\nYou can't put these item in your bag while it's full.\nYou need to drop something.",
		ERROR_STAKED_ITEM = "\nYou can't put more %ss in your bag.",
		ERROR_EMPTY_BAG = " * Empty *\n",
		ERROR_ITEM_NOT_IN_BAG = "\nYou don't have %s on your bag.",
	 	ERROR_NO_ITEMS_IN_LOCATION = "\nThere's no items in this location.\n",
	 	ERROR_ITS_AN_ITEM = "\nThats not an object, %s is an item.\n\n",
	 	ERROR_DIFERENT_PROPERTY = "\nYou can't %s in this object.\n\n",
	 	ERROR_ALREADY_DOING_THAT = "\n%s is already doing that.\n\n",
	 	ERROR_NO_EXIT = "\nYou can't go that way.\n\n",
	 	ERROR_WALK_OBJECT ="\nYou stop what you're doing and,";

	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		GameSystem game = new GameSystemClass();
		Command cm;
		printMenu();
		do{
			System.out.print("> ");
			cm = getCommand(in);
			exeFirstOption(in, game, cm);
		} while(!cm.equals(Command.START) && !cm.equals(Command.TEST) && !cm.equals(Command.EXIT));
		
		if(cm.equals(Command.START) || cm.equals(Command.TEST)) {
			do{
				System.out.print("> ");
				cm = getCommand(in);
				exeOption(in, game, cm);
			} while(!cm.equals(Command.EXIT));
		}
		in.close();
	}
	
	private enum Command {
		
		START("- Starts the game."), INF("- Give you some information about the all game."),
		LOCATION("- Tells you the name of your location."), DESC("- Description mode gives bigger descriptions of locations."),
		W("- Walk to the West."), E("- Walk to the East."), N("- Walk to the North."), S("- Walk to the South."),
		BAG("- Opens your bag."), STATUS("- Gives you a lot of information about your game play."), 
		GET("<item name> - Catch a floor item."), DROP("<item name> - Puts an item at the floor."),
		QUANT("<item name> - How much of an item do you have."), MYNAME("- Tells you, your name."), 
		MONEY("- How much money do you have."), SIT("<object name> - You sit on an object."), 
		LAY("<object name> - You lay on an object."), STAND("- You stand up if you're down."), PUT("<object name> <second object name> - Put an object above a second object."), 
		TURN("<object name> - Turns an object on or off."), CLOSE("<object name> - Close an object."),
		CLICK("<program name> - Clicks on a program."), ITEMS("- Tells you every item you can encounter at the location you in."),
		OBJECTS("- Tells you every object you can find at the location yuor in."), SELL("<item name> <person name> - Sells an item to another person."), 
		BUY("<person name> - First tells you all the seller have to sell and then you can buy from him."), CREDITS("- Shows the credits of the game."), 
		HELP("- Shows the available commands"), EXIT("- Ends your adventure until you come back."), TEST("- Just for testing..."), UNKNOWN("");
		
		private String description;

		Command (String description) { this.description = description; }
		/**
		 * @return The description of the command.
		 */
		public String getDescription() { return this.description; }
		
	};
	
	/**
	 * @param in - Scanner
	 * @return The user input command.
	 */
	private static Command getCommand(Scanner in) {
		try {
			return Command.valueOf(in.next().toUpperCase());
		} catch(IllegalArgumentException e) {
			return Command.UNKNOWN;
		}
	}
	
	/**
	 * Execute a first available option.
	 * @param in - Scanner.
	 * @param game - GameSystem
	 * @param option - the user input.
	 */
	private static void exeFirstOption(Scanner in, GameSystem game, Command cm) {
		switch(cm) {
			case START:
				start(in,game);
				break;
			case TEST:
				test(in,game);
				break;
			case INF:
				information();
				break;
			case CREDITS:
				credits();
				break;
			case EXIT:
				exit(game);
				break;
			case HELP:
				help();
				break;
			default:
				defaultError(in);
				break;
		}
	}

	/**
	 * Execute one available option.
	 * @param in - Scanner.
	 * @param game - GameSystem
	 * @param option - the user input.
	 */
	private static void exeOption(Scanner in, GameSystem game, Command cm) {
		switch(cm) {
			case INF:
				information();
				break;
			case CREDITS:
				credits();
				break;
			case LOCATION:
				location(game);
				break;
			case GET:
				getItem(in, game);
				break;
			case DROP:
				dropItem(in, game);
				break;
			case DESC:
				fullDescriptions(game);
				break;
			case BAG:
				bagList(game);
				break;
			case QUANT:
				howMuchItems(in, game);
				break;
			case MONEY:
				playerMoney(game);
				break;
			case STATUS:
				playerStatus(game);
				break;
			case MYNAME:
				myName(game);
				break;
			case W:
				goDirection(game, Directions.WEST);
				break;
			case E:
				goDirection(game, Directions.EAST);
				break;
			case N:
				goDirection(game, Directions.NORTH);
				break;
			case S:
				goDirection(game, Directions.SOUTH);
				break;
			case SIT:
				action(in, game, Propertys.SIT);
				break;
			case STAND:
				stand(game);
				break;
			case LAY:
				action(in, game, Propertys.LAY);
				break;
			case PUT:
				action(in, game, Propertys.PUT);
				break;
			case ITEMS:
				locationItems(game);
				break;
			case OBJECTS:
				locationObjects(game);
				break;
			case SELL:
				sell(in, game);
				break;
			case BUY:
				buy(in, game);
				break;
			case HELP:
				help();
				break;
			case EXIT:
				exit(game);
				break;
			default:
				defaultError(in);
				break;
		}
	}
	
	public static void test(Scanner in, GameSystem game) {
		game.newPlayer("Afonso");
		game.startTimer();
		System.out.print("\n");
	}
	
	/**
	 * @param number - number of times you want to multiply.
	 * @param string - String you want to multiply.
	 * @return the input string multiplied by <number> times.
	 */
	public static String multiplier(int number, String string) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < number;i++)
			sb.append(string);
		return sb.toString();
	}
	
	/**
	 * Prints a <text> at a certain <speed>.
	 * @param text - String to print.
	 * @param speed - Speed to print the string in milliseconds.
	 */
	private static void printString(String text, int speed) {
		int counter=0;
		for(int i = 0; i < text.length(); i++) {
			
			char character = text.charAt(i);
			counter++;
		    System.out.printf("%c", character);                           //Meter a primeira letra de cada frase em maiuscula
		    
		    if(counter>=60 && text.length()-i>60 && Character.compare(' ',character)==0) {
		    	counter=0; System.out.println("");
		    }
		    
		    coolDown(speed);
	
		}
	}
	
	/**
	 * Makes a cooldown of <time> seconds between prints.
	 * @param time - coolDown time.
	 */
	private static void coolDown(int time) {
		try{
			Thread.sleep(time);
	    }catch(InterruptedException ex){
	        Thread.currentThread().interrupt();
	    }
	}
	
	/**
	 * Prints the game Menu.
	 */
	private static void printMenu() {
		System.out.printf(SUCCESS_MENU
				, multiplier(15, " "), multiplier(13, " "), multiplier(20, " ")
				, multiplier(20, " "), multiplier(45, "-"), multiplier(47, " ")
				, multiplier(22, " "), multiplier(19, " "), multiplier(21, " ")
				, multiplier(22, " "), multiplier(20, " "), multiplier(47, " "), multiplier(45,"-"));
	}
	
	/**
	 * Prints all the available comments
	 */
	private static void help() {
		System.out.print(multiplier(66, "=")+"\n");
		for(Command cm : Command.values())
			if(!cm.equals(Command.UNKNOWN)) {
				String command = cm.toString().substring(0,1).toUpperCase() + cm.toString().substring(1).toLowerCase();
				System.out.printf("%s %s\n", command, cm.getDescription());
			}
		System.out.print(multiplier(66, "=")+"\n\n");
	}
	
	/**
	 * Starts the game.
	 * @param in - Scanner
	 * @param game - GameSystem
	 */
	private static void start(Scanner in, GameSystem game) {
		printString(SUCCESS_START, MAIN_SPEED);
		newPlayer(in, game);
		game.startTimer();
		printString(String.format("\nHey %s, in this universe where you just entered, you will witness one of the best journeys that you'll ever have!\n\n", game.getPlayerName()), MAIN_SPEED);
		System.out.print("[ PRESS ENTER TO CONTINUE ]");
		in.nextLine();
		enterNewLocation(game);
		printString("You are standing in the middle of your Room...\n\n", MAIN_SPEED);
	}
	
	/**
	 * Prints the name and first description of a location.
	 * @param game - GameSystem
	 */
	private static void enterNewLocation(GameSystem game) {
		location(game); coolDown(1000);
		locationInf(game); coolDown(800);
		if(game.isInDescriptionMode())locationObjects(game);
	}
	
	private static void locationObjects(GameSystem game) {
		try {
			Iterator<NonItem> it = game.allLocationObjects();
			printString(String.format("[ There's "), MAIN_SPEED);
			while(it.hasNext()) {
				NonItem ob = it.next();
				if(!it.hasNext()) printString("and ", MAIN_SPEED);
				printString(String.format("a%s %s%s", ob.getObjectDescription(), ob.getObjectType(), ob.getDirection()), MAIN_SPEED);              //Diferenciar an e a 
				if(!it.hasNext()) printString(". ]\n\n", MAIN_SPEED);
				else printString(", ", MAIN_SPEED);
			}
		} catch(NoObjectsException e) {}
	}
	
	/**
	 * Prints all the items at the location.
	 * @param game - GameSystem
	 */
	private static void locationItems(GameSystem game) {
		try {
			Iterator<LinkedList<Item>> it = game.allLocationItems();
			while(it.hasNext()) {
				Iterator<Item> itItem = it.next().iterator();
				boolean first = true;
				while(itItem.hasNext()) {
					Item item = itItem.next();
					if(first) {
						System.out.printf("\n%s x%d:\n",item.getObjectType(), game.getLocationItemQuant(item.getObjectType()) ); first = false;
					}
					System.out.printf("	%s\n",item.getItemName());
				}
			}
		} catch(NoObjectsException e) {
			printString(ERROR_NO_ITEMS_IN_LOCATION, MAIN_SPEED);
		}
		System.out.print("\n");
	}
	
	/**
	 * Prints all the information about the location.
	 * @param game - GameSystem
	 */
	private static void locationInf(GameSystem game) {
		printString(String.format("%s\n",game.getLocationDescription()), MAIN_SPEED);
	}
	
	/**
	 * Give you some information about the all game.
	 */
	private static void information() {
		printString(SUCCESS_INF, MAIN_SPEED);
	}
	
	/**
	 * Shows the credits of the game.
	 */
	private static void credits() {
		printString(SUCCESS_CREDITS, MAIN_SPEED);
	}
	
	/**
	 * Adds a new player to the game.
	 * @param game - GameSystem
	 */
	private static void newPlayer(Scanner in, GameSystem game) {
		String option;
		String name;
		
		do {
			printString(START_NEW_PLAYER, MAIN_SPEED);
			name = in.next()+in.nextLine();
			printString(String.format(START_IS_THAT_YOUR_NAME, name), MAIN_SPEED);
			option = in.next().toUpperCase(); in.nextLine();
		} while(option.equals(NO));
		
		try {
			if(!option.equals(YES)) throw new InvalidOptionException();
			game.newPlayer(name);
		} catch(TooLongNameException e) {
			printString(ERROR_TOO_LONG_NAME, MAIN_SPEED);
			newPlayer(in,game);
		} catch(InvalidOptionException e) {
			printString(ERROR_INVALID_COMMAND, MAIN_SPEED);
			newPlayer(in,game);
		}
	}
	
	/**
	 * Tells you the name of your location at the moment.
	 * @param game - GameSystem
	 */
	private static void location(GameSystem game) {
		System.out.printf("\n["+game.getLocationName()+"]\n\n", game.getPlayerName().toUpperCase());
	}

	/**
	 * Catch a floor item.
	 * @param in - Scanner
	 * @param game - GameSystem
	 */
	private static void getItem(Scanner in, GameSystem game) {
		
		Random rand = new Random();
		String items = in.next()+in.nextLine();
		Iterator<String> itemsType = game.splitItems(items); 
		
		while(itemsType.hasNext()) {
			int number = rand.nextInt(50);
			try {
				String item = itemsType.next();
				if(number!=50) {
					game.getItem(item);
					
					if(number<=20) printString(SUCCESS_GET_1, MAIN_SPEED);
					else if(number<20 && number>=45) printString(String.format(SUCCESS_GET_2, item), MAIN_SPEED);                  
					else printString(String.format(SUCCESS_GET_3, game.getPlayerName()), MAIN_SPEED);
					
				} else printString(SUCCESS_GET_4, MAIN_SPEED);
			} catch(ObjectNotInLocationException e) {
				printString(String.format(ERROR_OBJECT_NOT_IN_LOCATION, e.getItemType()),MAIN_SPEED);
			} catch(NotAnItemException e) {
				printString(String.format(ERROR_NOT_AN_ITEM, e.getItemType()), MAIN_SPEED);
			} catch(BagFullException e) {
				printString(ERROR_NO_SPACE, MAIN_SPEED);
			} catch(StakedItemException e) {
				printString(String.format(ERROR_STAKED_ITEM, e.getItemType()), MAIN_SPEED);
			}
		}
		System.out.println("\n");
	}
	
	/**
	 * Puts an bag item on the floor.
	 * @param in - Scanner
	 * @param game - GameSystem
	 */
	private static void dropItem(Scanner in, GameSystem game) {
		
		Random rand = new Random();
		String items = in.next()+in.nextLine();
		Iterator<String> itemsType = game.splitItems(items);
		
		while(itemsType.hasNext()) {
			int number = rand.nextInt(50);
			try {
				String item = itemsType.next();
				game.dropItem(item);
				
				if(number<=20) printString(SUCCESS_DROP_1, MAIN_SPEED);
				else if(number<20 && number>=40) printString(String.format(SUCCESS_DROP_2, item, game.getLocationName()), MAIN_SPEED);
				else if(number>40 && number<49) printString(String.format(SUCCESS_DROP_3, item), MAIN_SPEED);
				else printString(String.format(SUCCESS_DROP_4, game.getPlayerName()), MAIN_SPEED);
				
			} catch(ItemNotInBagException e) {
				printString(String.format(ERROR_ITEM_NOT_IN_BAG, e.getItemType()), MAIN_SPEED);
			} 
		}
		System.out.println("\n");
	}

	/**
	 * Description mode gives bigger descriptions of locations
	 * @param game - GameSystem
	 */
	private static void fullDescriptions(GameSystem game) {
		if(!game.isInDescriptionMode())
			printString(SUCCESS_DISCRIPTION_MODE_ON, MAIN_SPEED);
		else
			printString(SUCCESS_DESCRIPTION_MODE_OFF, MAIN_SPEED);
		game.descriptionMode();
	}

	/**
	 * Opens your bag.
	 * @param game - GameSystem
	 */
	private static void bagList(GameSystem game) {
		System.out.println(YOUR_BAG);
		try {
			Iterator<ArrayList<Item>> it = game.listBag();
			while(it.hasNext()) {
				ArrayList<Item> list = it.next();
				Item item = list.get(0);
				int quantity = list.size();
				
				System.out.printf("[%s]: x%d\n\n", item.getObjectType(), quantity);
			}
		} catch(EmpetyBagException e) {
			System.out.println(ERROR_EMPTY_BAG);
		}
	}

	/**
	 * How much of an item do you have.
	 * @param in - Scanner
	 * @param game - GameSystem
	 */
	private static void howMuchItems(Scanner in, GameSystem game) {
		String item = in.next(); in.nextLine();
		
		try {
			printString(String.format(SUCCESS_ITEM_QUANTITY, game.getQuantity(item)), MAIN_SPEED);
		} catch (ItemNotInBagException e) {
			printString(String.format(ERROR_ITEM_NOT_IN_BAG, item), MAIN_SPEED);
		}
	}

	/**
	 * How much money do you have.
	 * @param game - GameSystem
	 */
	private static void playerMoney(GameSystem game) {
		printString(String.format(SUCCESS_PLAYER_MONEY, game.getPlayerBalance()), MAIN_SPEED);
	}

	/**
	 * Gives you a lot of information about your game play.
	 * @param game - GameSystem
	 */
	private static void playerStatus(GameSystem game) {
		location(game);
		System.out.printf("\nPLAYER"+multiplier(25-game.getPlayerName().length(), " ") 
						+"%s\nMONEY"+multiplier(25-Double.toString(game.getPlayerBalance()).length(), " ")
						+"$%d\nITEMS GATHERED"+multiplier(17-Integer.toString(game.itemsGathered()).length(), " ")
						+"%d\n\nTIME PLAYED"+ multiplier(19-game.timePlayed().length(), " ")
						+" %s\nADVENTURE STARTED    %s\n\n", 
						game.getPlayerName(), game.getPlayerBalance(), game.itemsGathered(), game.timePlayed(), game.getStartDate());
	}

	/**
	 * Tells you, your name.
	 * @param game - GameSystem
	 */
	private static void myName(GameSystem game) {
		printString(String.format(SUCCESS_MYNAME,game.getPlayerName()), MAIN_SPEED);
	}

	/**
	 * Walk to a certain direction (West, East, North, South).
	 * @param game - GameSystem
	 * @param string - direction.
	 */
	private static void goDirection(GameSystem game, Directions direction) {
		try {
			switch(direction) {
			case NORTH:
				game.movePlayer(Directions.NORTH);
				break;
			case WEST:
				game.movePlayer(Directions.WEST);
				break;
			case EAST:
				game.movePlayer(Directions.EAST);
				break;
			case SOUTH:
				game.movePlayer(Directions.SOUTH);
				break;
			}
			printString(String.format(SUCCESS_WALKING, direction), MAIN_SPEED);
			enterNewLocation(game);
		} catch(NoExitException e) {
			printString(ERROR_NO_EXIT, MAIN_SPEED);
		} catch(WalkUsingObjectException e) {
			printString(ERROR_WALK_OBJECT, MAIN_SPEED);
			goDirection(game, direction);
		}
	}

	/**
	 * 
	 * @param in - Scanner
	 * @param game - GameSystem
	 */
	public static void action(Scanner in, GameSystem game, Propertys property) {
		String object = in.next(); in.nextLine();
		
		try {
			
			game.action(property, object);
			
			switch(property) {
			case USE:
				printString(String.format("",object), MAIN_SPEED);
				break;
			case SIT:
				printString(String.format(SUCCESS_SIT, object), MAIN_SPEED);
				break;
			case LAY:
				printString(String.format(SUCCESS_LAY, object), MAIN_SPEED);
				break;
			case PUT:
				String secondObject = in.next(); in.nextLine();
				printString(String.format(SUCCESS_PUT, object, secondObject), MAIN_SPEED);
				break;
			}
			
		} catch(ItsAnItemException e) {
			printString(String.format(ERROR_ITS_AN_ITEM,object), MAIN_SPEED);
		} catch(ObjectNotInLocationException e) {
			printString(String.format(ERROR_OBJECT_NOT_IN_LOCATION+"\n\n",object), MAIN_SPEED);
		} catch(DiferentPropertysException e) {
			printString(String.format(ERROR_DIFERENT_PROPERTY,property.name().toLowerCase()), MAIN_SPEED);
		} catch(ObjectOccupiedException e) {
			printString(String.format(ERROR_ALREADY_DOING_THAT, e.getEntety().getName()),MAIN_SPEED);
		}
	}

	/**
	 * You stand up if you're down.
	 * @param game - GameSystem
	 */
	private static void stand(GameSystem game) {
		
	}
	
	private static void buy(Scanner in, GameSystem game) {
		
		
	}
	
	private static void sell(Scanner in, GameSystem game) {
		String item = in.next(),
			   entity = in.next();
		in.nextLine();
		
		try {
			game.sell(item, entity);
		} catch() {
			
		}
	}
	
	/**
	 * Prints an error message when the insert command does not exist.
	 */
	private static void defaultError(Scanner in) {
		in.nextLine();
		printString(ERROR_INVALID_COMMAND, MAIN_SPEED);
	}
	
	/**
	 * Ends your adventure until you come back.
	 */
	private static void exit(GameSystem game) {
		printString(SUCCESS_EXIT, MAIN_SPEED);
		game.exit();
	}
}
