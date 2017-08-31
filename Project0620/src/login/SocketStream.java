package login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketStream {
	
	// 각각의 클래스에서 소켓을 생성하니 렉이 너무 심했었음
	// 각각의 클래스에서 Input/OutputStream을 선언해 사용하니 소켓이 계속닫힘
	// 소켓과 Input/OutputStream을 변수로가진 클래스를 매개변수로 서로 공유한다

	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public SocketStream() {
		try {
			s = new Socket("192.168.0.36", 5000);
			System.out.println("소켓생성");
		
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
