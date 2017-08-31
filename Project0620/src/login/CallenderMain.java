package login;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class CallenderMain extends JFrame implements ActionListener, MouseListener, WindowListener {

	// ������ �����ִ� command
	final int VIEWALL = 0;
	final int INPUT = 1;
	final int SEARCH = 2;
	final int UPDATE = 3;
	final int DELETE = 4;

	JPanel[][] jsp = new JPanel[6][7];
	// 7��*6�� �з� �ǳ� ����
	JLabel[][] dayNum = new JLabel[6][7];
	// 7��*6�� �з� �� ����
	JList<String>[][] sList = new JList[6][7];
	// ���ں� ���� ���ø�� ����
	ArrayList<CallenderVO>[] listInDay = new ArrayList[31];
	// �������� �ҷ��� �Ѵ�ġ ������ �Ϸ羿 ������ ���� ArrayList 31�� ����
	ArrayList<String>[] titleInDay = new ArrayList[31];
	// ���ں� ���� ���ø�Ͽ� �� ���ڿ� ArrayList 31�� ����
	HashSet<String> hsCategory;
	// ī�װ� ���
	Calendar today = Calendar.getInstance();

	String id = null;

	int year = 0;
	int month = 0;
	int day = 0;
	int firstday = 0;
	int lastday = 0;
	JLabel d;
	JButton beforeM, nextM; // ������ ������ �Ѿ��
	JTextField jtfYear, jtfMonth;
	JLabel lbYear, lbMonth;
	JButton moveTo;

	JTextField jtfSearch;
	JButton jbtnToday, jbtnSearch, jbtnList, jbtnExit, jbtnAdd;
	JPanel contentPane;
	ImageIcon imbCalYear, imgCalMon, imgMove, imgToday, imgSearch, imglist, imgLogout, imgAdd;

	JComboBox<String> jcmbCategory;
	SocketStream ss;
	// ������ Ŭ�������� ������ �����ϴ� ���� �ʹ� ���߾���
	// ������ Ŭ�������� Input/OutputStream�� ������ ����ϴ� ������ ��Ӵ���
	// ���ϰ� Input/OutputStream�� �����ΰ��� Ŭ������ �Ű������� ���� �����Ѵ�
	View_Module viewModule;
	// Ŭ�� �������Ǽ� â�� ��� ��������, �����ڿ��� �ѹ��� new�ǰ��ϰ�
	// �⺻ visible(false), Ŭ���������� visible(true)�ǰ� ������
	// �̷����ϴ� update, new��ư�����ϱ� ��ư�� �����̵ȴ� �����
	// �׳� Ŭ���ҋ����� ���� new �ϰ�����, release�� �����ϱ� Ŭ��ī��Ʈ ���� ��

	String category;

	public CallenderMain(String id, SocketStream ss) {
		// ���� �ʱ�ȭ
		this.ss = ss;
		this.id = id;

		LineBorder l = new LineBorder(Color.GRAY, 1);
		Font big = new Font("���� ���", Font.CENTER_BASELINE, 30);

		hsCategory = new HashSet<String>();
		contentPane = new JPanel();

		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		for (int i = 0; i < 31; i++) {
			listInDay[i] = new ArrayList<CallenderVO>();
			titleInDay[i] = new ArrayList<String>();
		}
		int x = 50;
		int y = 300;
		this.setLayout(null);

		// ���̺� ���(��ȭ�����)
		setHeader();

		// ���̺��� �����ذ� ����for������ ���
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				jsp[i][j] = new JPanel();
				jsp[i][j].setBounds(x, y, 100, 100);
				jsp[i][j].setBorder(l);
				dayNum[i][j] = new JLabel();
				jsp[i][j].add(dayNum[i][j], BorderLayout.NORTH);
				jsp[i][j].setBackground(Color.white);
				sList[i][j] = new JList<String>();
				sList[i][j].addMouseListener(this);
				jsp[i][j].add(sList[i][j], BorderLayout.CENTER);
				this.add(jsp[i][j]);
				x += 100;
			}
			y += 100;
			x = 50;
		}
		imbCalYear = new ImageIcon("src/image/year.PNG");
		imgCalMon = new ImageIcon("src/image/month.PNG");
		imgMove = new ImageIcon("src/image/move.PNG");
		imgToday = new ImageIcon("src/image/today.PNG");
		imgSearch = new ImageIcon("src/image/search.PNG");
		imglist = new ImageIcon("src/image/category.PNG");
		imgLogout = new ImageIcon("src/image/exit.PNG");
		imgAdd = new ImageIcon("src/image/square_add.PNG");

		jbtnAdd = new JButton(imgAdd);
		jbtnAdd.setBounds(720, 70, 32, 32);
		jbtnAdd.setOpaque(false);
		jbtnAdd.setContentAreaFilled(false);
		jbtnAdd.setBorderPainted(false);
		jbtnAdd.setFocusable(false);
		jbtnAdd.addActionListener(this);
		this.addWindowListener(this);
		contentPane.add(jbtnAdd);// �߰�

		jbtnExit = new JButton(imgLogout);
		jbtnExit.setBounds(720, 20, 32, 32);
		jbtnExit.setOpaque(false);
		jbtnExit.setContentAreaFilled(false);
		jbtnExit.setBorderPainted(false);
		jbtnExit.setFocusable(false);
		jbtnExit.addActionListener(this);
		contentPane.add(jbtnExit); // �����ư

		jtfSearch = new JTextField();
		jbtnToday = new JButton(imgToday);
		jbtnToday.setBounds(20, 20, 50, 50);
		jbtnToday.setOpaque(false);
		jbtnToday.setContentAreaFilled(false);
		jbtnToday.setBorderPainted(false);
		jbtnToday.setFocusable(false);
		jbtnToday.addActionListener(this);
		contentPane.add(jbtnToday);// ���� ��¥

		jtfSearch = new JTextField();
		jtfSearch.setBounds(500, 20, 100, 40);
		jtfSearch.setHorizontalAlignment(JTextField.CENTER);
		contentPane.add(jtfSearch); // �˻��ʵ�

		jbtnSearch = new JButton(imgSearch);
		jbtnSearch.setBounds(610, 20, 35, 35);
		jbtnSearch.setOpaque(false);
		jbtnSearch.setContentAreaFilled(false);
		jbtnSearch.setBorderPainted(false);
		jbtnSearch.setFocusable(false);
		jbtnSearch.addActionListener(this);
		contentPane.add(jbtnSearch); // �˻���ư

		// ��� �Է��� ����?
		lbYear = new JLabel(imbCalYear);
		lbMonth = new JLabel(imgCalMon);
		jtfYear = new JTextField();
		jtfMonth = new JTextField();
		jtfYear.setBounds(200, 160, 100, 60);
		lbYear.setBounds(300, 160, 50, 60);
		jtfMonth.setBounds(400, 160, 100, 60);
		lbMonth.setBounds(500, 160, 50, 60);
		jtfYear.setFont(big);
		lbYear.setFont(big);
		jtfMonth.setFont(big);
		lbMonth.setFont(big);
		contentPane.add(lbYear);
		contentPane.add(lbMonth);
		contentPane.add(jtfYear);
		contentPane.add(jtfMonth);

		beforeM = new JButton();
		nextM = new JButton();
		moveTo = new JButton(imgMove);
		beforeM.setBounds(50, 170, 80, 50);
		beforeM.setFont(new Font("���� ���", Font.BOLD, 20));

		beforeM.setOpaque(false);
		beforeM.setContentAreaFilled(false);
		beforeM.setBorderPainted(false);
		beforeM.setFocusable(false);

		nextM.setBounds(670, 170, 80, 50);
		nextM.setFont(new Font("���� ���", Font.BOLD, 20));

		nextM.setOpaque(false);
		nextM.setContentAreaFilled(false);
		nextM.setBorderPainted(false);
		nextM.setFocusable(false);

		moveTo.setBounds(570, 170, 80, 50);
		// moveTo.setText("�̵�");

		moveTo.setOpaque(false);
		moveTo.setContentAreaFilled(false);
		moveTo.setBorderPainted(false);
		moveTo.setFocusable(false);

		moveTo.addActionListener(this);
		beforeM.addActionListener(this);
		nextM.addActionListener(this);
		contentPane.add(beforeM);
		contentPane.add(nextM);
		contentPane.add(moveTo);

		category = null;
		jcmbCategory = new JComboBox<String>();
		jcmbCategory.addItem("��ü����");
		category = "��ü����";
		
		Calendar cal = Calendar.getInstance();

		// �޷�ó�� ����, ���ں� ���� �ֱ�
		setcal(cal);

		// ��ȸâ �̸� �ϳ� �����صΰ� �Ⱥ��̰��صд�
		// ���߿� setVO, setView �ؼ� ����
		viewModule = new View_Module(id, ss, hsCategory);
		viewModule.setVisible(false);

		// ���� ������ �ִ��� Ȯ���� Ȯ��â�� ���
		if (!listInDay[today.get(Calendar.DAY_OF_MONTH) - 1].isEmpty()) {
			String todaySchedule = "";
			for (String str : titleInDay[today.get(Calendar.DAY_OF_MONTH) - 1]) {
				todaySchedule = todaySchedule + str + "\n";
			}
			JOptionPane.showMessageDialog(this, "�������� �ֽ��ϴ�:\n\n" + todaySchedule);
		}

		this.setBounds(100, 100, 800, 1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void setcal(Calendar cal) {
		// �迭 �ʱ�ȭ
		// �����غ��� hs�� �ʱ�ȭ���ʿ䰡..�ֳ�
		hsCategory.clear();
		for (int k = 0; k < 31; k++) {
			listInDay[k].clear();
			titleInDay[k].clear();
		}

		CallenderVO cVo = new CallenderVO();
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH);
		String etc = "";
	
		etc = year + " " + (month + 1) + " " + this.id + " " + category;
		// ������ ������ "�� �� id ī�װ�" ���ڿ� ����
		// �������� etc.length()�� ī�װ��� ���É���� �ȉ���� �Ǻ�
		CalCover mc = new CalCover(cVo, VIEWALL, etc);
		try {
			ss.oos.writeObject(mc);
			ArrayList<CallenderVO> list = new ArrayList<CallenderVO>();
			list = (ArrayList<CallenderVO>) ss.ois.readObject();
			// �������� �Ѵ޾�ġ ���� ����Ʈ�� �޴´�.
			for(CallenderVO v:list)
			{
				System.out.println(v.getTitle());
			}

			// �Ѵ޾�ġ �޾ƿ� ������ �Ϸ��Ϸ��� ArrayList�� ����ֱ�
			int i = 0; // ���ڸ� ���� idx
			int j = 0; // list�� idx

			while (i < 31 && j < list.size()) {
				
				// list���ִ� ������ startday�� cal1�� ������ i+1�� ���ڰ� ��ġ�� i��° arraylist��
				// ����
				String[]splt = list.get(j).getStartDay().split("-");
				if ((i + 1) == Integer.parseInt(splt[2])) {
					listInDay[i].add(list.get(j));
					String title = list.get(j).getTitle();

					// ������ �ʹ� �涧 7���̳��� �ڸ���
					if (title.length() > 7) {
						title = title.substring(0, 5);
						title = title + "..";
					}
					titleInDay[i].add(title);
					hsCategory.add(list.get(j).getCategory());
					// ī�װ��� �߰��ϱ�
					j++;
				} // ���ں� VO����Ʈ, ���� ����Ʈ�� �߰�
				else {
					i++;
				}
			} // ������ ���մٴ� �����Ͽ��� �Ҽ��ִ�

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 7*6 �г�, ��, JList �迭���� ���Ͽ� �°� ����, ���ں� ���� �־��ֱ�
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		int dayofM = 0;
		cal.set(year, month, 1);// �̹��� ù���� ����
		firstday = cal.get(Calendar.DAY_OF_WEEK);// �̹��� ù ������ ���
		cal.add(Calendar.MONTH, 1);// 1�޵��ϰ�
		cal.add(Calendar.DATE, -1);// 1�����ؼ�
		lastday = cal.get(Calendar.DAY_OF_MONTH);// �̹��� ���������� ��´�

		for (int k = 0; k < lastday; k++) {
			// ex)7*6�迭���� 1���� ù���� �迭, 1+ firstday - 1�Ͽ� ��ġ�Ѵ� =>
			dayofM = k + firstday - 1;
			dayNum[dayofM / 7][dayofM % 7].setText((k + 1) + "");
			// ArrayList.toArray()�� ������� String[]���� ĳ������ �ȵȴٰ��ϴ� �Ѷ��Ѷ� �ٲ��ݽô�
			String[] tempStr = new String[titleInDay[k].size()];
			for (int l = 0; l < tempStr.length; l++)
				tempStr[l] = titleInDay[k].get(l);

			sList[dayofM / 7][dayofM % 7].setListData(tempStr);
			sList[dayofM / 7][dayofM % 7].addMouseListener(this);

			// ������ ȸ������ ǥ��
			if (year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH)
					&& k == today.get(Calendar.DAY_OF_MONTH) - 1) {
				jsp[dayofM / 7][dayofM % 7].setBackground(Color.LIGHT_GRAY);
			}
		}

		// �̹��޿����� ������/�������� 13���̳� 0���� �Ǹ� �ȵǴϱ�
		if (month == 0) {
			beforeM.setText("12��");
			nextM.setText((month + 2) + "��");
		} else if (month == 11) {
			beforeM.setText(month + "��");
			nextM.setText("1��");
		} else {
			beforeM.setText(month + "��");
			nextM.setText((month + 2) + "��");
		}

		// �� �� TextField�� �⵵ �� ����ϱ�
		jtfYear.setText(year + "");
		jtfYear.setHorizontalAlignment(JTextField.CENTER);
		jtfMonth.setText((month + 1) + "");
		jtfMonth.setHorizontalAlignment(JTextField.CENTER);

		// ī�װ� ������ ���ϰ� ��ü��ȸ�ÿ��� ī�װ�����Ʈ�� ������Ʈ����
		if (category == "��ü����") {
			// ī�װ������ �ʱ�ȭ�ؾߵǴµ� removeAll�� remove�� �ȸ����ϴ�
			// ���� ���ظ԰ڽ��ϴ� �׳� ���� ���� �ع����ô�...
			// jcmbCategory.removeAll();
			jcmbCategory.setVisible(false);// ������ �Ⱥ��̰� ���ְ�
			jcmbCategory = new JComboBox<String>();
			jcmbCategory.addItem("��ü����");//��ü���� �߰�
			Iterator<String> it = hsCategory.iterator();
			while (it.hasNext()) {
				jcmbCategory.addItem(it.next());
			}
			jcmbCategory.addActionListener(this);
			jcmbCategory.setBounds(100, 25, 80, 32);
			add(jcmbCategory);
			jcmbCategory.setVisible(true);
		}
	}

	public void setHeader() {
		LineBorder l = new LineBorder(Color.GRAY, 1);
		d = new JLabel();
		d.setForeground(Color.RED);
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("��");
		d.setBounds(50, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("��");
		d.setBounds(150, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("ȭ");
		d.setBounds(250, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("��");
		d.setBounds(350, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("��");
		d.setBounds(450, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("��");
		d.setBounds(550, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setForeground(Color.blue);
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("��");
		d.setBounds(650, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();

		// �� �ϱ����� �޷�â���� �ʱ�ȭ������
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				dayNum[i][j].setText("");
				jsp[i][j].setBackground(Color.white);
				sList[i][j].removeAll();
			}
		}
		// �����޷ΰ���
		if (o == beforeM) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1);
			setcal(cal);

		}
		// �����޷ΰ���
		else if (o == nextM) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, month + 1, 1);
			setcal(cal);
		}
		// �� �� TF���Էµ� �޷� ����
		else if (o == moveTo) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(jtfYear.getText()), Integer.parseInt(jtfMonth.getText()) - 1, 1);
			setcal(cal);
		}
		// ���÷� ���ư���
		else if (o == jbtnToday) {
			Calendar cal = Calendar.getInstance();
			setcal(cal);
		}
		// �˻��ϱ�
		else if (o == jbtnSearch) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(jtfYear.getText()), Integer.parseInt(jtfMonth.getText()) - 1, 1);
			setcal(cal);

			// �˻��κ� jbtn ��ġ
			System.out.println("�˻�");

			CallenderVO cVo = new CallenderVO();
			cVo.setTitle(jtfSearch.getText()); // jtfSearch�ʵ忡 �� �ؽ��� ���ؼ� VO��
												// title�� ��
			cVo.setId(id);
			System.out.println(cVo.getId()+" "+cVo.getTitle());

			
			CalCover mc = new CalCover(cVo, 2, "");
			try {
				ss.oos.writeObject(mc);
				ArrayList<CallenderVO> list = (ArrayList<CallenderVO>) ss.ois.readObject();
				/*
				 * for(int i = 0 ;i< list.size();i++){
				 * System.out.println(list.get(i)); }
				 */
				SearchList sl = new SearchList(list);
				sl.setVisible(true);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else if (o == jbtnExit) {
			System.exit(0);
		} else if (o == jbtnAdd) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(jtfYear.getText()), Integer.parseInt(jtfMonth.getText()) - 1, 1);
			setcal(cal);
			Input_Module module = new Input_Module(id, ss, hsCategory);
			module.setVisible(true);

		}
		// ī�װ� �����ص� �޷� ��ü�� �ٽ� ������
		else if (o == jcmbCategory) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(jtfYear.getText()), Integer.parseInt(jtfMonth.getText()) - 1, 1);
			category = (String) jcmbCategory.getSelectedItem();
			setcal(cal);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	// �Է�â, ��ȸâ���� ���ƿË����� update���ش�
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(jtfYear.getText()), Integer.parseInt(jtfMonth.getText()) - 1, 1);
		setcal(cal);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// x���� listInDay���� y���� VO��ü�� �ҷ��;���
		// x= ����Ʈ���ִ� ���� ���� ��¥�� -1
		// y= ����Ʈ���� ���õ� �ε���
		JList jlst = (JList) e.getSource();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Integer.parseInt(jtfMonth.getText()) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (e.getClickCount() == 2) {
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					if (jlst == sList[i][j]) {
						int x = Integer.parseInt(dayNum[i][j].getText());
						int y = jlst.getSelectedIndex();

						if (x >= 1 && y != -1)
						// �̻��Ѱ� �����ϸ� y���� -1�ߴ��� ��������....
						{
							CallenderVO vSelected = listInDay[x - 1].get(y);
							// ���õ� VO�� �Ű������� view_module ����
							viewModule = new View_Module(id,ss,hsCategory);
							viewModule.setVo(vSelected);
							viewModule.setView();
						}

						sList[i][j].clearSelection();
						break;

					}
				}
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
