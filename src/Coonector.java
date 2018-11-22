import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.application.Platform;

public class Coonector extends Thread {

	private Socket socket;
	private OutputStream toServer;
	private BNK48 control;
	public Coonector(String ip,int port,BNK48 tmp) {
		// TODO Auto-generated constructor stub
		control = tmp;
		try {
			socket = new Socket(ip, port);
			toServer = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Tank Create!");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		receiver();
	}
	private void receiver() {
		// TODO Auto-generated method stub
		Thread t = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputStream fromServer = null;
				try {
					fromServer = socket.getInputStream();
					DataInputStream in = new DataInputStream(fromServer);
					while (true) {
						try{
							if (in.available()>0) {
								String str = in.readUTF();
								System.out.println(str);
								String[] strSplit = str.split("_");
								if (strSplit[0].matches("#Client(.*)")) {
									BNK48 tmp;
                                    tmp = control;
                                    String[] strData = strSplit[1].split("\\|");
                                    System.out.println("originX = " + strData[0]);
                                    final double originX;
                                    System.out.println("originY = " + strData[1]);
                                    double originY = Double.parseDouble(strData[1]);
                                    System.out.println("dX = " + strData[2]);
                                    double dX = Double.parseDouble(strData[2]);
                                    System.out.println("dY = " + strData[3]);
                                    double dY = Double.parseDouble(strData[3]);
                                    System.out.println("r = " + strData[4]);
                                    double r = Double.parseDouble(strData[4]);
                                    System.out.println("g = " + strData[5]);
                                    double g = Double.parseDouble(strData[5]);
                                    System.out.println("b = " + strData[6]);
                                    double b = Double.parseDouble(strData[6]);
                                    System.out.println("ID = " + strData[7]);
                                    int MyID = Integer.parseInt(strData[7]);
                                    if (dX > 0) {
                                        originX = 0.0;
                                    } else {
                                        originX = 500.0;
                                    }
//                                    if (MyID==control.MyID) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                        	
                                        		System.out.println(strSplit[0]+"Create");
                                                tmp.frogCreate(originX, originY, dX, dY, r, g, b);
											
                                        }
                                    });
//                                    }
								}
								else if (strSplit[0].matches("#Tank(.*)")) {
                                    int MyID = Integer.parseInt(strSplit[1]);
                                    control.MyID = MyID;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            control.update();
                                        }
                                    });
                                } else if (strSplit[0].matches("#Total(.*)")) {
                                    int max = Integer.parseInt(strSplit[1]);
                                    System.out.println("Recieve Total " + max);
                                    control.max = max;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            control.update();
                                        }
                                    });
                                }
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						fromServer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	public void sent(String str) {
        try {
            DataOutputStream out = new DataOutputStream(toServer);
            out.flush();
            out.writeUTF(str);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
