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
  
    //����������  
    public void startServer(){  
        try{  
        ss = new ServerSocket(8888);  
        started = true;  
        System.out.println("������������!");  
        } catch (BindException e1){  
            System.out.println("�˿ڱ�ռ�ã���رպ�����");  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
                while(started){  
                s = ss.accept();  
                System.out.println("һ���ͻ�������");  
                Client c = new Client(s);  
                new Thread(c).start();  
                clients.add(c);  
            }  
        }catch (IOException e) {  
                e.printStackTrace();  
            }finally {  
                try {  
                    ss.close();//�رմ��׽��֡� �� accept() �����е�ǰ�������̶߳������׳� SocketException��   
                    //������׽�����һ����֮������ͨ������رո�ͨ����  
  
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
                   
                    
            //�ѵ�ǰ�û���������Ϣ���͸������û�.     
           try {   
               while (flag) {      
                    String str = dis.readUTF();  
                    for (int i = 0; i < clients.size();i++) {     
                    Client c = clients.get(i);  
                    //��õ�ǰ�û���IP     
            ip=s.getInetAddress().getHostAddress();     
                    c.send(str);  
                }  
                }  
           }catch (EOFException e) {  
                System.out.println("�ͻ��˹ر���!");  
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
                dos.writeUTF(ip+"˵��");  
                dos.writeUTF(str+'\n');  
            } catch (IOException e) {  
                clients.remove(this);  
            }  
        }  
    }  
   }  
