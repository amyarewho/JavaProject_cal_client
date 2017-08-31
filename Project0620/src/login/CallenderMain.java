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

	// 서버에 들어갈수있는 command
	final int VIEWALL = 0;
	final int INPUT = 1;
	final int SEARCH = 2;
	final int UPDATE = 3;
	final int DELETE = 4;

	JPanel[][] jsp = new JPanel[6][7];
	// 7일*6주 분량 판넬 선언
	JLabel[][] dayNum = new JLabel[6][7];
	// 7일*6주 분량 라벨 선언
	JList<String>[][] sList = new JList[6][7];
	// 일자별 일정 선택목록 선언
	ArrayList<CallenderVO>[] listInDay = new ArrayList[31];
	// 서버에서 불러올 한달치 일정을 하루씩 나누어 넣을 ArrayList 31개 선언
	ArrayList<String>[] titleInDay = new ArrayList[31];
	// 일자별 일정 선택목록에 들어갈 문자열 ArrayList 31개 선언
	HashSet<String> hsCategory;
	// 카테고리 목록
	Calendar today = Calendar.getInstance();

	String id = null;

	int year = 0;
	int month = 0;
	int day = 0;
	int firstday = 0;
	int lastday = 0;
	JLabel d;
	JButton beforeM, nextM; // 이전달 다음달 넘어가기
	JTextField jtfYear, jtfMonth;
	JLabel lbYear, lbMonth;
	JButton moveTo;

	JTextField jtfSearch;
	JButton jbtnToday, jbtnSearch, jbtnList, jbtnExit, jbtnAdd;
	JPanel contentPane;
	ImageIcon imbCalYear, imgCalMon, imgMove, imgToday, imgSearch, imglist, imgLogout, imgAdd;

	JComboBox<String> jcmbCategory;
	SocketStream ss;
	// 각각의 클래스에서 소켓을 생성하니 렉이 너무 심했었음
	// 각각의 클래스에서 Input/OutputStream을 선언해 사용하니 소켓이 계속닫힘
	// 소켓과 Input/OutputStream을 변수로가진 클래스를 매개변수로 서로 공유한다
	View_Module viewModule;
	// 클릭 여러번되서 창이 계속 여러번뜸, 생성자에서 한번만 new되게하고
	// 기본 visible(false), 클릭했을떄만 visible(true)되게 해주자
	// 이렇게하니 update, new버튼누르니까 버튼이 먹통이된다 어떡하지
	// 그냥 클릭할떄마다 새로 new 하게하자, release에 넣으니까 클릭카운트 재대로 들어감

	String category;

	public CallenderMain(String id, SocketStream ss) {
		// 변수 초기화
		this.ss = ss;
		this.id = id;

		LineBorder l = new LineBorder(Color.GRAY, 1);
		Font big = new Font("맑은 고딕", Font.CENTER_BASELINE, 30);

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

		// 테이블 헤더(월화수목금)
		setHeader();

		// 테이블을 무식해게 이중for문으로 찍기
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
		contentPane.add(jbtnAdd);// 추가

		jbtnExit = new JButton(imgLogout);
		jbtnExit.setBounds(720, 20, 32, 32);
		jbtnExit.setOpaque(false);
		jbtnExit.setContentAreaFilled(false);
		jbtnExit.setBorderPainted(false);
		jbtnExit.setFocusable(false);
		jbtnExit.addActionListener(this);
		contentPane.add(jbtnExit); // 종료버튼

		jtfSearch = new JTextField();
		jbtnToday = new JButton(imgToday);
		jbtnToday.setBounds(20, 20, 50, 50);
		jbtnToday.setOpaque(false);
		jbtnToday.setContentAreaFilled(false);
		jbtnToday.setBorderPainted(false);
		jbtnToday.setFocusable(false);
		jbtnToday.addActionListener(this);
		contentPane.add(jbtnToday);// 오늘 날짜

		jtfSearch = new JTextField();
		jtfSearch.setBounds(500, 20, 100, 40);
		jtfSearch.setHorizontalAlignment(JTextField.CENTER);
		contentPane.add(jtfSearch); // 검색필드

		jbtnSearch = new JButton(imgSearch);
		jbtnSearch.setBounds(610, 20, 35, 35);
		jbtnSearch.setOpaque(false);
		jbtnSearch.setContentAreaFilled(false);
		jbtnSearch.setBorderPainted(false);
		jbtnSearch.setFocusable(false);
		jbtnSearch.addActionListener(this);
		contentPane.add(jbtnSearch); // 검색버튼

		// 년월 입력후 엔터?
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
		beforeM.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		beforeM.setOpaque(false);
		beforeM.setContentAreaFilled(false);
		beforeM.setBorderPainted(false);
		beforeM.setFocusable(false);

		nextM.setBounds(670, 170, 80, 50);
		nextM.setFont(new Font("맑은 고딕", Font.BOLD, 20));

		nextM.setOpaque(false);
		nextM.setContentAreaFilled(false);
		nextM.setBorderPainted(false);
		nextM.setFocusable(false);

		moveTo.setBounds(570, 170, 80, 50);
		// moveTo.setText("이동");

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
		jcmbCategory.addItem("전체보기");
		category = "전체보기";
		
		Calendar cal = Calendar.getInstance();

		// 달력처럼 일자, 일자별 일정 넣기
		setcal(cal);

		// 조회창 미리 하나 선언해두고 안보이게해둔다
		// 나중에 setVO, setView 해서 쓴다
		viewModule = new View_Module(id, ss, hsCategory);
		viewModule.setVisible(false);

		// 오늘 일정이 있는지 확인후 확인창에 출력
		if (!listInDay[today.get(Calendar.DAY_OF_MONTH) - 1].isEmpty()) {
			String todaySchedule = "";
			for (String str : titleInDay[today.get(Calendar.DAY_OF_MONTH) - 1]) {
				todaySchedule = todaySchedule + str + "\n";
			}
			JOptionPane.showMessageDialog(this, "스케줄이 있습니다:\n\n" + todaySchedule);
		}

		this.setBounds(100, 100, 800, 1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void setcal(Calendar cal) {
		// 배열 초기화
		// 생각해보니 hs는 초기화할필요가..있네
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
		// 서버에 전달할 "년 월 id 카테고리" 문자열 생성
		// 서버에선 etc.length()로 카테고리가 선택됬는지 안됬는지 판별
		CalCover mc = new CalCover(cVo, VIEWALL, etc);
		try {
			ss.oos.writeObject(mc);
			ArrayList<CallenderVO> list = new ArrayList<CallenderVO>();
			list = (ArrayList<CallenderVO>) ss.ois.readObject();
			// 서버에서 한달어치 일정 리스트를 받는다.
			for(CallenderVO v:list)
			{
				System.out.println(v.getTitle());
			}

			// 한달어치 받아온 일정을 하루하루의 ArrayList에 집어넣기
			int i = 0; // 일자를 세는 idx
			int j = 0; // list의 idx

			while (i < 31 && j < list.size()) {
				
				// list에있는 일정의 startday를 cal1에 저장후 i+1과 일자가 일치시 i번째 arraylist에
				// 넣음
				String[]splt = list.get(j).getStartDay().split("-");
				if ((i + 1) == Integer.parseInt(splt[2])) {
					listInDay[i].add(list.get(j));
					String title = list.get(j).getTitle();

					// 제목이 너무 길때 7자이내로 자르기
					if (title.length() > 7) {
						title = title.substring(0, 5);
						title = title + "..";
					}
					titleInDay[i].add(title);
					hsCategory.add(list.get(j).getCategory());
					// 카테고리도 추가하기
					j++;
				} // 일자별 VO리스트, 제목 리스트에 추가
				else {
					i++;
				}
			} // 정렬이 되잇다는 가정하에만 할수있다

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 7*6 패널, 라벨, JList 배열에서 요일에 맞게 일자, 일자별 일정 넣어주기
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		int dayofM = 0;
		cal.set(year, month, 1);// 이번달 첫날로 가서
		firstday = cal.get(Calendar.DAY_OF_WEEK);// 이번달 첫 요일을 얻고
		cal.add(Calendar.MONTH, 1);// 1달뒤하고
		cal.add(Calendar.DATE, -1);// 1일전해서
		lastday = cal.get(Calendar.DAY_OF_MONTH);// 이번달 마지막날을 얻는다

		for (int k = 0; k < lastday; k++) {
			// ex)7*6배열에서 1일은 첫주차 배열, 1+ firstday - 1일에 위치한다 =>
			dayofM = k + firstday - 1;
			dayNum[dayofM / 7][dayofM % 7].setText((k + 1) + "");
			// ArrayList.toArray()의 결과값이 String[]으로 캐스팅이 안된다고하니 한땀한땀 바꿔줍시다
			String[] tempStr = new String[titleInDay[k].size()];
			for (int l = 0; l < tempStr.length; l++)
				tempStr[l] = titleInDay[k].get(l);

			sList[dayofM / 7][dayofM % 7].setListData(tempStr);
			sList[dayofM / 7][dayofM % 7].addMouseListener(this);

			// 오늘은 회색으로 표시
			if (year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH)
					&& k == today.get(Calendar.DAY_OF_MONTH) - 1) {
				jsp[dayofM / 7][dayofM % 7].setBackground(Color.LIGHT_GRAY);
			}
		}

		// 이번달에따라 다음달/이전달이 13월이나 0월이 되면 안되니까
		if (month == 0) {
			beforeM.setText("12월");
			nextM.setText((month + 2) + "월");
		} else if (month == 11) {
			beforeM.setText(month + "월");
			nextM.setText("1월");
		} else {
			beforeM.setText(month + "월");
			nextM.setText((month + 2) + "월");
		}

		// 년 월 TextField에 년도 월 출력하기
		jtfYear.setText(year + "");
		jtfYear.setHorizontalAlignment(JTextField.CENTER);
		jtfMonth.setText((month + 1) + "");
		jtfMonth.setHorizontalAlignment(JTextField.CENTER);

		// 카테고리 선택을 안하고 전체조회시에만 카테고리리스트를 업데이트하자
		if (category == "전체보기") {
			// 카테고리목록을 초기화해야되는데 removeAll도 remove도 안먹힙니다
			// 더는 못해먹겠습니다 그냥 새로 생성 해버립시다...
			// jcmbCategory.removeAll();
			jcmbCategory.setVisible(false);// 이전거 안보이게 해주고
			jcmbCategory = new JComboBox<String>();
			jcmbCategory.addItem("전체보기");//전체보기 추가
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
		d.setText("일");
		d.setBounds(50, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("월");
		d.setBounds(150, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("화");
		d.setBounds(250, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("수");
		d.setBounds(350, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("목");
		d.setBounds(450, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("금");
		d.setBounds(550, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
		d = new JLabel();
		d.setForeground(Color.blue);
		d.setHorizontalAlignment(JLabel.CENTER);
		d.setText("토");
		d.setBounds(650, 250, 100, 50);
		d.setBorder(l);
		contentPane.add(d);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();

		// 뭐 하기전에 달력창부터 초기화해주자
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				dayNum[i][j].setText("");
				jsp[i][j].setBackground(Color.white);
				sList[i][j].removeAll();
			}
		}
		// 이전달로가기
		if (o == beforeM) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1);
			setcal(cal);

		}
		// 다음달로가기
		else if (o == nextM) {
			Calendar cal = Calendar.getInstance();
			cal.set(year, month + 1, 1);
			setcal(cal);
		}
		// 년 월 TF에입력된 달로 가기
		else if (o == moveTo) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(jtfYear.getText()), Integer.parseInt(jtfMonth.getText()) - 1, 1);
			setcal(cal);
		}
		// 오늘로 돌아가기
		else if (o == jbtnToday) {
			Calendar cal = Calendar.getInstance();
			setcal(cal);
		}
		// 검색하기
		else if (o == jbtnSearch) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(jtfYear.getText()), Integer.parseInt(jtfMonth.getText()) - 1, 1);
			setcal(cal);

			// 검색부분 jbtn 서치
			System.out.println("검색");

			CallenderVO cVo = new CallenderVO();
			cVo.setTitle(jtfSearch.getText()); // jtfSearch필드에 쓴 텍스를 겟해서 VO의
												// title에 셋
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
		// 카테고리 선택해도 달력 전체를 다시 세팅함
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
	// 입력창, 조회창에서 돌아올떄마다 update해준다
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
		// x번쨰 listInDay에서 y번쨰 VO객체를 불러와야함
		// x= 리스트가있는 곳의 라벨의 날짜값 -1
		// y= 리스트에서 선택된 인덱스
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
						// 이상한거 선택하면 y값이 -1뜨는지 몰랐었다....
						{
							CallenderVO vSelected = listInDay[x - 1].get(y);
							// 선택된 VO를 매개변수로 view_module 실행
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
