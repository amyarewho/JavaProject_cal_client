package login;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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


public class JoinView extends JFrame implements ActionListener {
	final int JOIN = 5;
	JPanel contentPane;
	JButton jbtnJoin, jbtnBack;
	JLabel jlId, jlPw, jlName;
	JTextField jtfId;
	JPasswordField jpfPw;
	ImageIcon imgJbtnJoin, imgJbtnBack, imgJlId, imgJlPw;
	SocketStream ss;

	Boolean joinCheck = false;
	
	public JoinView(SocketStream ss) {
		
		this.ss = ss;
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(200, 100, 400, 300);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
	
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		//imgJbtnJoin = new ImageIcon("src/image/join.PNG");
		imgJbtnJoin = new ImageIcon("src/image/btnjoin.PNG");
		imgJbtnBack = new ImageIcon("src/image/reply.PNG");
		imgJlId = new ImageIcon("src/image/idjoinlb.PNG");
		imgJlPw = new ImageIcon("src/image/pwjoinlb.PNG");

		jlId = new JLabel(imgJlId);
		jlPw = new JLabel(imgJlPw);

		jtfId = new JTextField(20);
		jpfPw = new JPasswordField(20);

		jbtnJoin = new JButton(imgJbtnJoin);
		jbtnBack = new JButton(imgJbtnBack);
		jbtnBack.setOpaque(false);
		jbtnBack.setContentAreaFilled(false);
		jbtnBack.setBorderPainted(false);
		jbtnBack.setFocusable(false);

		jlId.setBounds(20, 70, 153, 30);
		jlPw.setBounds(20, 110, 153, 30);
		jtfId.setBounds(150, 70, 190, 30);
		jpfPw.setBounds(150, 110, 190, 30);

		jbtnJoin.setBounds(50, 170, 290, 50);
		jbtnBack.setBounds(5, 5, 24, 24);

		jbtnJoin.addActionListener(this);
		jbtnBack.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		contentPane.add(jbtnJoin);
		contentPane.add(jbtnBack);
		contentPane.add(jlId);
		contentPane.add(jlPw);
		contentPane.add(jtfId);
		contentPane.add(jpfPw);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(jbtnJoin)) {

			char[] pass = jpfPw.getPassword();
			String password = new String(pass);
			String id = jtfId.getText();
			String pw = password;
			String idPw = id + " " + pw;
			CallenderVO v = null;

			CalCover cc = new CalCover(v,JOIN,idPw);
			try {
				ss.oos.writeObject(cc);
				joinCheck = (boolean) ss.ois.readObject();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
			
			
			
			

			if (joinCheck == true) {
				JOptionPane.showConfirmDialog(this, "가입 성공 하셨습니다.");
				this.setVisible(false);
				try {
					Thread.sleep(70);
					new LoginView();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			} else if (joinCheck == false) {
				if (id == null || pw == null || id.trim().length() == 0 || pw.trim().length() == 0) {
					JOptionPane.showMessageDialog(this, "아이디, 비밀번호는 필수 기재 사항 입니다.", "경고", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "이미 가입된 사용자입니다.", "경고", JOptionPane.ERROR_MESSAGE);
				}
				jtfId.setText("");
				jpfPw.setText("");

			}

		}
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		this.setVisible(false);	
		new LoginView();
	}

}
