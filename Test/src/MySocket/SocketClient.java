package MySocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by yeyan on 2014/10/21.
 */
public class SocketClient{
    static Socket fd;

    public static void SocketClient_init(String site, int port){
        try{
            fd = new Socket(site,port);
            //System.out.println("Client is created! site:"+site+" port:"+port);
            //return 1;
        }catch (UnknownHostException e){
            //fd=null;
            System.out.println("connect error");
        }catch (IOException e){
            //fd=null;
            System.out.println("connect time out");
            //e.printStackTrace();
        }
    }

    public static boolean IsConnected()
    {
        if(fd == null)
            return false;
        return fd.isConnected();
    }

    public static String sendMsg(String msg){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(fd.getInputStream()));
            PrintWriter out = new PrintWriter(fd.getOutputStream());
            out.println(msg);
            out.flush();
            return in.readLine();
        }catch(IOException e){
            //System.out.println("connect error");
            e.printStackTrace();
        }
        return "";
    }
    public static void closeSocket(){
        try{
            if(fd != null)
            {
                fd.close();
                fd=null;
            }
        }catch(IOException e){
            //System.out.println("connect error");
            e.printStackTrace();
        }
    }
    /*
    public static void main(String[] args) throws Exception{
        SocketClient client = new SocketClient("192.0.1.1",2345);
        System.out.println(client.sendMsg("nimei1"));
        client.closeSocket();
    }
    */
}
