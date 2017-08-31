package login;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame implements ActionListener {
	final int LOGIN = 6;
	

	JPanel contentPane;
	JButton jbtnLogin, jbtnJoin;
	JLabel jlId, jlPw, jlTopic;
	JTextField jtfId;
	JPasswordField jpfPw;
	ImageIcon imgJbtnLogin, imgJbtnJoin, imgJlId, imgJlPw, imgJlTopic, imgJtfId, imgJpfPw;

	JoinView jView;

	Boolean loginCheck = false;
	SocketStream ss;

	public LoginView() {
		
		ss = new SocketStream();
		ss.setStream();
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(200, 100, 900, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		imgJbtnLogin = new ImageIcon("src/image/jbtnLogin.PNG");
		imgJbtnJoin = new ImageIcon("src/image/jbtnjoin.PNG");
		imgJlId = new ImageIcon("src/image/idlb.PNG");
		imgJlPw = new ImageIcon("src/image/pwlb.PNG");
		// imgJlTopic = new ImageIcon("src/image/cal.PNG");
		imgJlTopic = new ImageIcon("src/image/calender_schedule.PNG");

		jbtnLogin = new JButton(imgJbtnLogin);
		jbtnJoin = new JButton(imgJbtnJoin);
		jlId = new JLabel(imgJlId);
		jlPw = new JLabel(imgJlPw);
		jlTopic = new JLabel(imgJlTopic);
		jtfId = new JTextField(20);
		jpfPw = new JPasswordField(20);

		jlTopic.setBounds(50, 100, 510, 510);
		jlId.setBounds(600, 250, 46, 46);
		jlPw.setBounds(600, 300, 46, 46);
		jtfId.setBounds(650, 250, 200, 46);
		jpfPw.setBounds(650, 300, 200, 46);
		jbtnLogin.setBounds(600, 350, 250, 37);
		jbtnJoin.setBounds(600, 390, 250, 37);

		jbtnLogin.addActionListener(this);
		/*
		 * jbtnLogin.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { do_btnLogin_actionPerformed(e); }
		 * });
		 */

		jbtnJoin.addActionListener(this);

		contentPane.add(jbtnLogin);
		contentPane.add(jbtnJoin);
		contentPane.add(jlId);
		contentPane.add(jlPw);
		contentPane.add(jlTopic);
		contentPane.add(jtfId);
		contentPane.add(jpfPw);

		setVisible(true);

	}

	/*
	 * protected void do_btnLogin_actionPerformed(ActionEvent e) { MemberDAO dao
	 * = new MemberDAO(); MemberDTO dto = new MemberDTO();
	 * 
	 * char[] pass = jpfPw.getPassword(); String password = new String(pass);
	 * String id = jtfId.getText(); String pw = password;
	 * System.out.println(id); System.out.println(pw); dto.setId(id);
	 * dto.setPw(pw); boolean check = dao.isLogin(dto); if(check==true){
	 * JOptionPane.showConfirmDialog(this, "success"); }else{
	 * JOptionPane.showConfirmDialog(this, "fail"); }
	 * 
	 * }
	 */

	public static void main(String[] args) {
		new LoginView();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(jbtnLogin)) {

			char[] pass = jpfPw.getPassword();
			String password = new String(pass);
			String id = jtfId.getText();
			String pw = password;
			CallenderVO v = null;
			// 소켓연결
			String idPw = id + " " + pw;
			CalCover cc = new CalCover(v, LOGIN, idPw);

			try {
				ss.oos.writeObject(cc);
				loginCheck = (boolean) ss.ois.readObject();
			} catch (ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			System.out.println(loginCheck);

			if (loginCheck == true) {
				CallenderMain cMain = new CallenderMain(id, ss);
				// JOptionPane.showConfirmDialog(this, id+" 님 로그인 성공하셨습니다.");
				JOptionPane.showMessageDialog(this, id + "님 로그인 성공하셨습니다.");

				this.setVisible(false);
				cMain.setVisible(true);

			} else if (loginCheck == false) {

				// JOptionPane.showConfirmDialog(this, "로그인 실패하셨습니다.");
				JOptionPane.showMessageDialog(this, "로그인 실패하셨습니다.");

				jtfId.setText("");
				jpfPw.setText("");
			}
		} else if (e.getSource().equals(jbtnJoin)) {
			jView = new JoinView(ss);
			this.setVisible(false);
			jView.setVisible(true);
		}

	}

}