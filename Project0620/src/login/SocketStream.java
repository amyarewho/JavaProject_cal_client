package login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketStream {
	
	// ������ Ŭ�������� ������ �����ϴ� ���� �ʹ� ���߾���
	// ������ Ŭ�������� Input/OutputStream�� ������ ����ϴ� ������ ��Ӵ���
	// ���ϰ� Input/OutputStream�� �����ΰ��� Ŭ������ �Ű������� ���� �����Ѵ�

	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public SocketStream() {
		try {
			s = new Socket("192.168.0.36", 5000);
			System.out.println("���ϻ���");
		
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	}
	public void setStream(){
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void socketClose(){
		try {
			ois.close();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
