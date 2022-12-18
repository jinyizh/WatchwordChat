import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class MyHttpRequest {

  private String filename;
  private Socket clientSocket;
  private HashMap<String, String> map = new HashMap<>();

  public String getFilename() {
    return this.filename;
  }

  public Socket getClientSocket() {
    return this.clientSocket;
  }

  public HashMap<String, String> getMap() {
    return this.map;
  }

  public MyHttpRequest(Socket clientSocket) {

    this.clientSocket = clientSocket;
    System.out.println("Client joined: " + clientSocket);

    Scanner sc;
    try {
      sc = new Scanner(clientSocket.getInputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // handle 1st line of header
    String line = sc.nextLine();
    System.out.println("parsing headers...");
    System.out.println(line);
    String[] lineSplit = line.split(" ");
    filename = lineSplit[1];
    if (Objects.equals(filename, "/")) {
      filename = "/index.html";
    }
    // handle rest of lines
    line = sc.nextLine();
    while (!line.equals("")) {
      System.out.println(line);
      String[] restLineSplit = line.split(": ");
      map.put(restLineSplit[0], restLineSplit[1]);
      line = sc.nextLine();
    }
    System.out.println("done parsing headers");
  }

  public boolean isWebSocketRequest() {
    return map.containsKey("Sec-WebSocket-Key"); // header also contains "Connection: Upgrade"
  }

  // Server handshake response:
  public static void handshake(Socket clientSocket, String key) throws IOException, NoSuchAlgorithmException {
    OutputStream outputStream = clientSocket.getOutputStream();
    PrintWriter printWriter = new PrintWriter(outputStream);

    key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"; // magic string
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] data = key.getBytes();
    String result = Base64.getEncoder().encodeToString(md.digest(data));

    // send response back to client asking for a web socket
    printWriter.print("HTTP/1.1 101 Switching Protocols\r\n");
    printWriter.print("Upgrade: websocket\r\n");
    printWriter.print("Connection: Upgrade\r\n");
    printWriter.print("Sec-WebSocket-Accept: " + result + "\r\n");
    printWriter.print("\r\n");
    printWriter.flush();
    System.out.println("done sending handshake");
  }
}