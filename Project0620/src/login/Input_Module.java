package login;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Input_Module extends JFrame implements ActionListener, MouseListener {
	final int VIEWALL = 0;
	final int INPUT = 1;
	final int SEARCH = 2;
	final int UPDATE = 3;
	final int DELETE = 4;

	JComboBox<String> jcmb;
	JTextField jtfTitle, jtfStart, jtfEnd, jtfStartTime, jtfEndTime, jtfRepeatDate, jtfRepeatNums;
	JTextArea jtfMemo;
	JButton jbtnCategory, jbtnaAllow, jbtnDelete, jbtnCancel, jbtnAdd, jbtnStart, jbtnEnd;
	JLabel lbDay, lbStart, lbEnd, lbRepeat, lbRepeatnums, lbMemo, lbTime;
	JCheckBox jcbDay, jcbTime;
	JScrollPane jsp1, jsp2, jspMemo;

	JMenuItem jmi1, jmi2, jmi3, jmi4;
	String title, start, end, starttime, endtime, memo, category;
	int redate, renums;
	boolean allday, inputtime;
	JPanel contentPane;
	ImageIcon imgadd, imgCategory, imgAllow, imgDelete, imgCancel, imgStart, imgEnd;
	String id;
	CallenderVO vo;
	CalCover cc;
	SocketStream ss;
	String etc;

	// -------------viewModule에서 쓸거
	JButton jbtnUpdate;
	JButton jbtnNew;

	public Input_Module(String id, SocketStream ss, HashSet<String> categoryList) {
		this.ss = ss;

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}
		});
		this.id = id;
		setLayout(null);
		setBounds(100, 100, 478, 414);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		imgadd = new ImageIcon("src/image/add.png");
		imgCategory = new ImageIcon("src/image/category.png");
		imgAllow = new ImageIcon("src/image/insert1.png");
		imgDelete = new ImageIcon("src/image/del.png");
		imgCancel = new ImageIcon("src/image/cancel.png");
		imgStart = new ImageIcon("src/image/calendar.png");
		imgEnd = new ImageIcon("src/image/calendar.png");

		jtfTitle = new JTextField(" 제목을 입력해주세요");
		jtfTitle.addMouseListener(this);
		jtfStart = new JTextField();
		jtfEnd = new JTextField();
		jtfStartTime = new JTextField();
		jtfEndTime = new JTextField();
		jtfRepeatDate = new JTextField();
		jtfRepeatNums = new JTextField();
		jtfMemo = new JTextArea();
		jspMemo = new JScrollPane(jtfMemo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		lbDay = new JLabel("하루종일");
		lbStart = new JLabel("시작");
		lbEnd = new JLabel("종료");
		lbRepeat = new JLabel("반복 간격                  일");
		lbRepeatnums = new JLabel("반복 회수");
		lbMemo = new JLabel("메모");
		lbTime = new JLabel("시간입력");
		jbtnCancel = new JButton(imgCancel);
		jbtnaAllow = new JButton(imgAllow);
		jbtnDelete = new JButton(imgDelete);
		jbtnCategory = new JButton(imgCategory);
		jbtnAdd = new JButton(imgadd);
		jbtnStart = new JButton(imgStart);
		jbtnEnd = new JButton(imgEnd);
		jcbDay = new JCheckBox();
		jcbTime = new JCheckBox();
		jcmb = new JComboBox<String>();

		jcmb.addItem("");
		for (String str : categoryList) {
			jcmb.addItem(str);
		}
		jcmb.addActionListener(this);

		jtfTitle.setBounds(20, 10, 300, 35);
		jtfStart.setBounds(115, 70, 116, 30);
		jtfEnd.setBounds(115, 105, 116, 30);
		jtfStartTime.setBounds(265, 70, 95, 30);
		jtfEndTime.setBounds(265, 105, 95, 30);
		jtfRepeatDate.setBounds(225, 140, 40, 30);
		jtfRepeatNums.setBounds(370, 140, 40, 30);
		jtfStartTime.setEnabled(false);
		jtfEndTime.setEnabled(false);

		jspMemo.setBounds(115, 180, 330, 100);

		lbDay.setBounds(392, 65, 140, 30);
		lbStart.setBounds(75, 70, 130, 30);
		lbEnd.setBounds(75, 105, 130, 30);
		lbRepeat.setBounds(160, 140, 130, 30);
		lbRepeatnums.setBounds(310, 140, 130, 30);

		lbMemo.setBounds(75, 180, 150, 30);
		lbTime.setBounds(392, 88, 140, 30);

		jbtnCancel.setBounds(265, 300, 80, 60);
		jbtnaAllow.setBounds(360, 300, 80, 60);
		jbtnDelete.setBounds(20, 300, 80, 60);
		jcmb.setBounds(323, 10, 80, 35);
		jbtnAdd.setBounds(405, 10, 40, 35);

		jbtnStart.setBounds(230, 70, 25, 29);
		jbtnEnd.setBounds(230, 105, 25, 29);

		jbtnCancel.setOpaque(false);
		jbtnCancel.setContentAreaFilled(false);
		jbtnCancel.setBorderPainted(false);
		jbtnCancel.setFocusable(false);

		jbtnaAllow.setOpaque(false);
		jbtnaAllow.setContentAreaFilled(false);
		jbtnaAllow.setBorderPainted(false);
		jbtnaAllow.setFocusable(false);

		jbtnDelete.setOpaque(false);
		jbtnDelete.setContentAreaFilled(false);
		jbtnDelete.setBorderPainted(false);
		jbtnDelete.setFocusable(false);

		jbtnCategory.setOpaque(false);
		jbtnCategory.setContentAreaFilled(false);
		jbtnCategory.setBorderPainted(false);
		jbtnCategory.setFocusable(false);

		jbtnAdd.setOpaque(false);
		jbtnAdd.setContentAreaFilled(false);
		jbtnAdd.setBorderPainted(false);
		jbtnAdd.setFocusable(false);

		jbtnStart.setOpaque(false);
		jbtnStart.setContentAreaFilled(false);
		jbtnStart.setBorderPainted(false);
		jbtnStart.setFocusable(false);

		jbtnEnd.setOpaque(false);
		jbtnEnd.setContentAreaFilled(false);
		jbtnEnd.setBorderPainted(false);
		jbtnEnd.setFocusable(false);

		jcbDay.setBounds(370, 65, 20, 30);
		jcbTime.setBounds(370, 88, 20, 30);

		jcbTime.setOpaque(false);
		jcbTime.setContentAreaFilled(false);
		jcbTime.setBorderPainted(false);
		jcbTime.setFocusable(false);

		jcbDay.setOpaque(false);
		jcbDay.setContentAreaFilled(false);
		jcbDay.setBorderPainted(false);
		jcbDay.setFocusable(false);

		jbtnCancel.addActionListener(this);
		jbtnaAllow.addActionListener(this);
		jbtnDelete.addActionListener(this);
		jbtnCategory.addActionListener(this);
		jbtnAdd.addActionListener(this);
		jbtnStart.addActionListener(this);
		jbtnEnd.addActionListener(this);

		jcbDay.addMouseListener(this);
		jcbTime.addMouseListener(this);

		jtfMemo.setLineWrap(true);

		contentPane.add(jtfTitle);
		contentPane.add(jtfStart);
		contentPane.add(jtfEnd);
		contentPane.add(jtfStartTime);
		contentPane.add(jtfEndTime);
		contentPane.add(jcmb);
		contentPane.add(jtfRepeatDate);
		contentPane.add(jtfRepeatNums);
		contentPane.add(jspMemo);
		contentPane.add(lbDay);
		contentPane.add(lbStart);
		contentPane.add(lbEnd);
		contentPane.add(lbRepeat);
		contentPane.add(lbRepeatnums);
		contentPane.add(lbMemo);
		contentPane.add(lbTime);
		contentPane.add(jbtnCancel);
		contentPane.add(jbtnaAllow);
		contentPane.add(jbtnDelete);
		contentPane.add(jbtnCategory);
		contentPane.add(jbtnAdd);
		contentPane.add(jbtnStart);
		contentPane.add(jbtnEnd);
		contentPane.add(jcbDay);
		contentPane.add(jcbTime);
		jbtnDelete.setVisible(false);
		setVisible(true);
	}

	public void makeVO() {
		redate = 0;
		renums = 0;
		title = jtfTitle.getText();
		start = jtfStart.getText();
		end = jtfEnd.getText();
		starttime = jtfStartTime.getText();
		endtime = jtfEndTime.getText();
		if (!jtfRepeatDate.getText().equals(""))
			redate = Integer.parseInt(jtfRepeatDate.getText());
		if (!jtfRepeatNums.getText().equals(""))
			renums = Integer.parseInt(jtfRepeatNums.getText());
		allday = jcbDay.isSelected();
		inputtime = jcbTime.isSelected();
		memo = jtfMemo.getText();
		StringBuffer inputStart=new StringBuffer();
		StringBuffer inputEnd=new StringBuffer();
		
		inputStart.append(start);
		System.out.println(jcbDay.isSelected());
		if (!jcbDay.isSelected()) {

		
			inputEnd.append(end);
			if (jcbTime.isSelected()) {
				
				
				inputStart.append("-");
				inputStart.append(starttime.replaceAll(":", "-"));
				inputEnd.append("-");
				inputEnd.append(endtime.replaceAll(":", "-"));
				//년-월-일-시-분-초 형태의 문자열로 date 저장
			}
			else if(!jcbTime.isSelected()){
				
				//시간입력이 없다면 -시-분에 쓰레기값을 넣는다
				inputStart.append("-00-00");
				inputEnd.append("-00-00");
			}
		}else if(jcbDay.isSelected()){
			//당일치기 일정이면 끝나는날에 당일 값을 넣어준다
			if(jcbTime.isSelected()){
				inputStart.append("-");
				inputStart.append(starttime.replaceAll(":", "-"));
			
			}
			else{
				inputStart.append("-00-00");
				
			}
			inputEnd = inputStart;
			
			
		}
		
		
		// VO 객체 생성

		vo = new CallenderVO(id, title, category, memo, inputStart.toString(), inputEnd.toString(), allday, inputtime, redate, renums);

		cc = new CalCover(vo, INPUT, etc);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == jbtnaAllow) {
			makeVO();
			try {
				ss.oos.writeObject(cc);
				String strResult = (String) ss.ois.readObject();
				JOptionPane.showMessageDialog(this, strResult);
				this.setVisible(false);

			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else if (o == jbtnCancel) {
			this.setVisible(false);
		} else if (o == jbtnStart) {
			new innerCal(jtfStart);
		} else if (o == jbtnEnd) {
			new innerCal(jtfEnd);
		} else if (o == jcmb) {
			category = (String) jcmb.getSelectedItem();
		} else if (o == jbtnAdd) {
			String addStr = JOptionPane.showInputDialog("카테고리 추가:");
			jcmb.addItem(addStr);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		/* 미사용 */}

	@Override
	public void mousePressed(MouseEvent e) {
		Object o = e.getSource();

		if (o == jcbDay) {
			jtfEnd.setEnabled(jcbDay.isSelected());
			jtfEndTime.setEnabled(jcbDay.isSelected());
		}
		if (o == jcbTime) {
			jtfStartTime.setEnabled(!jcbTime.isSelected());
			jtfEndTime.setEnabled(!jcbTime.isSelected());
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// 일자입력창 옆에 버튼누르면 미니달력 뜨게 만들기
	class innerCal extends JFrame implements ActionListener, MouseListener {
		JPanel contentPane;
		JLabel[][] jDay = new JLabel[6][7];
		JLabel yearMonth;
		JButton beforeM, nextM;
		Calendar today;
		int year, month, day;
		JTextField result;
		ImageIcon imgLeft, imgRight;

		public innerCal(JTextField result) {
			contentPane = new JPanel();
			contentPane.setBackground(new Color(255, 255, 255));

			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			this.result = result;
			this.setLayout(null);
			setHeader();

			today = Calendar.getInstance();
			int x = 10;
			int y = 100;
			LineBorder l = new LineBorder(Color.gray);
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					jDay[i][j] = new JLabel();
					jDay[i][j].setBorder(l);
					jDay[i][j].setBounds(x, y, 50, 40);
					jDay[i][j].setBackground(Color.white);
					jDay[i][j].addMouseListener(this);
					add(jDay[i][j]);
					x += 50;
				}
				y += 40;
				x = 10;
			}

			imgLeft = new ImageIcon("src/image/left.PNG");
			imgRight = new ImageIcon("src/image/right.PNG");

			beforeM = new JButton(imgLeft);
			nextM = new JButton(imgRight);

			beforeM.setBounds(5, 10, 50, 50);
			beforeM.setOpaque(false);
			beforeM.setContentAreaFilled(false);
			beforeM.setBorderPainted(false);
			beforeM.setFocusable(false);

			nextM.setBounds(310, 10, 50, 50);
			nextM.setOpaque(false);
			nextM.setContentAreaFilled(false);
			nextM.setBorderPainted(false);
			nextM.setFocusable(false);

			beforeM.addActionListener(this);
			nextM.addActionListener(this);

			contentPane.add(beforeM);
			contentPane.add(nextM);

			yearMonth = new JLabel();
			yearMonth.setBounds(170, 10, 100, 40);

			contentPane.add(yearMonth);
			setcal(today);

			setSize(385, 385);
			setVisible(true);

		}

		public void setcal(Calendar cal) {
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH);
			day = cal.get(Calendar.DAY_OF_MONTH);
			int dayofM = 0;
			cal.set(year, month, 1);
			cal.set(year, month, 1);
			int firstday = cal.get(Calendar.DAY_OF_WEEK);
			cal.add(Calendar.MONTH, 1);// 1달뒤
			cal.add(Calendar.DATE, -1);// 1일전
			int lastday = cal.get(Calendar.DAY_OF_MONTH);

			for (int i = 0; i < lastday; i++) {
				dayofM = i + firstday - 1;
				jDay[dayofM / 7][dayofM % 7].setText((i + 1) + "");
			}
			if (month == 0) {
				// beforeM.setText("12");
				// nextM.setText((month + 2)+"");
			} else if (month == 11) {
				// beforeM.setText(month + "");
				// nextM.setText("1");
			} else {
				// beforeM.setText(month + "");
				// nextM.setText((month + 2) + "");
			}
			yearMonth.setText(year + "-" + (month + 1));

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			JLabel j = (JLabel) e.getSource();
			String day = j.getText();
			result.setText(yearMonth.getText() + "-" + day);
			if (e.getClickCount() == 2) {
				this.setVisible(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		public void setHeader() {
			LineBorder l = new LineBorder(Color.GRAY, 1);
			JLabel d;
			d = new JLabel();
			d.setForeground(Color.RED);
			d.setHorizontalAlignment(JLabel.CENTER);
			d.setText("일");
			d.setBounds(10, 60, 50, 40);
			d.setBorder(l);
			add(d);
			d = new JLabel();
			d.setHorizontalAlignment(JLabel.CENTER);
			d.setText("월");
			d.setBounds(60, 60, 50, 40);
			d.setBorder(l);
			add(d);
			d = new JLabel();
			d.setHorizontalAlignment(JLabel.CENTER);
			d.setText("화");
			d.setBounds(110, 60, 50, 40);
			d.setBorder(l);
			add(d);
			d = new JLabel();
			d.setHorizontalAlignment(JLabel.CENTER);
			d.setText("수");
			d.setBounds(160, 60, 50, 40);
			d.setBorder(l);
			add(d);
			d = new JLabel();
			d.setHorizontalAlignment(JLabel.CENTER);
			d.setText("목");
			d.setBounds(210, 60, 50, 40);
			d.setBorder(l);
			add(d);
			d = new JLabel();
			d.setHorizontalAlignment(JLabel.CENTER);
			d.setText("금");
			d.setBounds(260, 60, 50, 40);
			d.setBorder(l);
			add(d);
			d = new JLabel();
			d.setForeground(Color.blue);
			d.setHorizontalAlignment(JLabel.CENTER);
			d.setText("토");
			d.setBounds(310, 60, 50, 40);
			d.setBorder(l);
			add(d);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					jDay[i][j].setText("");
				}
			}
			if (o == beforeM) {
				Calendar cal = Calendar.getInstance();
				cal.set(year, month - 1, 1);
				setcal(cal);

			} else if (o == nextM) {
				Calendar cal = Calendar.getInstance();
				cal.set(year, month + 1, 1);
				setcal(cal);

			}
		}

	}
}
