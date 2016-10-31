package UserLogin;

import java.io.BufferedReader;  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.EOFException;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintStream;  
import java.net.BindException;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.util.ArrayList;  
import java.util.List;  
  
public class test {  
    boolean started = false;  
    ServerSocket ss = null;  
    Socket s = null;  
    String ip;  
    List<Client> clients = new ArrayList<Client>();  
    //int count;  
  
    //开启服务器  
    public void startServer(){  
        try{  
        ss = new ServerSocket(8888);  
        started = true;  
        System.out.println("服务器已启动!");  
        } catch (BindException e1){  
            System.out.println("端口被占用，请关闭后重试");  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
                while(started){  
                s = ss.accept();  
                System.out.println("一个客户已连接");  
                Client c = new Client(s);  
                new Thread(c).start();  
                clients.add(c);  
            }  
        }catch (IOException e) {  
                e.printStackTrace();  
            }finally {  
                try {  
                    ss.close();//关闭此套接字。 在 accept() 中所有当前阻塞的线程都将会抛出 SocketException。   
                    //如果此套接字有一个与之关联的通道，则关闭该通道。  
  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
          
        }  
  
    public static void main(String[] args) {  
        new test().startServer();  
    }  
    
   class Client implements Runnable {     
        private Socket s;  
        private DataInputStream dis = null;  
        private DataOutputStream dos = null;  
        private boolean flag = false;  
          
        public Client(Socket s){  
            this.s = s;  
            try {  
                dis = new DataInputStream(s.getInputStream());  
                dos = new DataOutputStream(s.getOutputStream());  
                flag = true;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
          
        @Override    
        public void run() {        
                   
                    
            //把当前用户发来的信息发送给所有用户.     
           try {   
               while (flag) {      
                    String str = dis.readUTF();  
                    for (int i = 0; i < clients.size();i++) {     
                    Client c = clients.get(i);  
                    //获得当前用户的IP     
            ip=s.getInetAddress().getHostAddress();     
                    c.send(str);  
                }  
                }  
           }catch (EOFException e) {  
                System.out.println("客户端关闭了!");  
            } catch (IOException e) {  
                e.printStackTrace();  
            }finally {  
                    try {  
                        if(dis != null) dis.close();  
                        if(dos != null) dos.close();  
                        if(s != null)  {  
                            s.close();  
                        }  
                          
                    } catch (IOException e1) {  
                        e1.printStackTrace();  
                    }     
                }       
            }  
      
    public void send(String str){  
        try{  
                dos.writeUTF(ip+"说：");  
                dos.writeUTF(str+'\n');  
            } catch (IOException e) {  
                clients.remove(this);  
            }  
        }  
    }  
   }  
