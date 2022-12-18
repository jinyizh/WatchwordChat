import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Watchword {
  private static HashMap<String, Watchword> map = new HashMap<>(); // number of watchwords exist
  private String name;
  public ArrayList<Socket> clients = new ArrayList<>(); // clients connect to word object

  public static HashMap<String, Watchword> getMap() {
    return Watchword.map;
  }

  public static void setMap(String name, Watchword watchword) {
    Watchword.map.put(name, watchword);
  }

  public synchronized static Watchword getWord(String name) { // create word when client joins
    if(!Watchword.getMap().containsKey(name)) { // prevent duplicated words
      Watchword watchword = new Watchword(name);
      Watchword.setMap(name, watchword); // put created watchword into map
      return watchword;
    } else { // word exists
      return Watchword.getMap().get(name);
    }
  }

  private Watchword(String name) { // constructor is only allowed to be accessed though getWord;
    this.name = name;
  }

  public void setClient(Socket client) { // client in room
    this.clients.add(client);
  }
}