package login;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import login.Input_Module.innerCal;

public class View_Module extends Input_Module {
	final int VIEWALL = 0;
	final int INPUT = 1;
	final int SEARCH = 2;
	final int UPDATE = 3;
	final int DELETE = 4;
	CallenderVO vo;

	// 수정, 새로만들기 버튼 2개 필요

	JTextField jtfCtg;
	JTextField jtfdDay;
	JLabel jlbdDay;
	ImageIcon imgNew, imgModify;
	HashSet<String> categoryList;
	String etc = "";
	public View_Module(String id, SocketStream ss,HashSet<String> categoryList) {
		super(id, ss,categoryList);
		this.categoryList = categoryList;
	}


	public void setVo(CallenderVO vo) {
		this.vo = vo;
	}
	public void setView(){
		//update delete도 inputModule에 추가시켜버리자
		imgNew = new ImageIcon("src/image/new_sch.PNG");
		imgModify = new ImageIcon("src/image/modify.PNG");
		
		jcbDay.setVisible(false);
		lbDay.setVisible(false);
		lbTime.setVisible(false);
		lbRepeatnums.setText("남은 횟수");
		jcbTime.setVisible(false);
		jbtnCategory.setVisible(false);
		jbtnaAllow.setVisible(false);
		jbtnAdd.setVisible(false);
		jbtnStart.setVisible(false);
		jbtnEnd.setVisible(false);
		jcmb.setVisible(false);
		jbtnAdd.setVisible(false);
		
		jtfCtg = new JTextField();
		jtfCtg.setText(vo.getCategory());
		jtfCtg.setBounds(323,10,100,35);
		//265, 70, 95, 30
		jlbdDay = new JLabel("D-Day");
		jlbdDay.setBounds(380,70,50,30);
		jtfdDay = new JTextField();
		jtfdDay.setBounds(380, 105, 50, 30);
		
		// 편집불가능하게
		jtfStart.setEditable(false);
		jtfEnd.setEditable(false);
		jtfMemo.setEditable(false);
		jtfRepeatDate.setEditable(false);
		jtfRepeatNums.setEditable(false);
		jtfStartTime.setEditable(false);
		jtfEndTime.setEditable(false);
		jtfTitle.setEditable(false);

		jtfCtg.setEditable(false);
		add(jtfCtg);

		jbtnNew = new JButton(imgNew);
		jbtnUpdate = new JButton(imgModify);
		jbtnNew.addActionListener(this);
		jbtnUpdate.addActionListener(this);
		
		jbtnNew.setOpaque(false);
		jbtnNew.setContentAreaFilled(false);
		jbtnNew.setBorderPainted(false);
		jbtnNew.setFocusable(false);

		jbtnUpdate.setOpaque(false);
		jbtnUpdate.setContentAreaFilled(false);
		jbtnUpdate.setBorderPainted(false);
		jbtnUpdate.setFocusable(false);

		jbtnDelete.setVisible(true);
		jbtnNew.setBounds(20, 300, 80, 60);
		jbtnUpdate.setBounds(130, 300, 80, 60);
		jbtnDelete.setBounds(240, 300, 80, 60);
		jbtnCancel.setBounds(350, 300, 80, 60);
		
		

		add(jbtnNew);
		add(jbtnUpdate);
		add(jtfdDay);
		add(jlbdDay);
		jbtnUpdate.addMouseListener(this);

		jtfTitle.setText(vo.getTitle());
		jtfTitle.setEnabled(false);
		
		 
		String[] spltStart = vo.getStartDay().split("-");
		jtfStart.setText(spltStart[0] + "-" + spltStart[1] + "-"
				+ spltStart[2]);
		jtfMemo.setText(vo.getMemo());
		if (vo.isInputTime()) {
			jtfStartTime.setText(spltStart[3] + ":" + spltStart[4]);
		}

		if (!vo.isAllDay()) {
			String[] spltEnd = vo.getEndDay().split("-");
			
			jtfEnd.setText(spltEnd[0] + "-" +spltEnd[1] + "-"
				+ spltEnd[2]);
			if(vo.isInputTime()){
				jtfEndTime.setText(spltEnd[3] + ":" + spltEnd[4]);
			}
		}
		jtfCtg.setText(vo.getCategory());
		jtfRepeatDate.setText(vo.getRepeatTerm()+"");
		jtfRepeatNums.setText(vo.getRepeatNum()+"");
		Calendar today = Calendar.getInstance();
		Calendar thisday = Calendar.getInstance();
		thisday.set(Integer.parseInt(spltStart[0]), Integer.parseInt(spltStart[1])-1,Integer.parseInt(spltStart[2]));
		
		
		double dday = Math.ceil((double)(today.getTimeInMillis() - thisday.getTimeInMillis())/1000/60/60/24);
		
		jtfdDay.setText(dday+"");
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		Object o = e.getSource();

	 if(o==jbtnaAllow){
		 	
			makeVO();
			System.out.println(cc.getV().getCategory());
			super.cc.setCommand(UPDATE);
			
			try {
				ss.oos.writeObject(cc);
				String ack = (String) ss.ois.readObject();
				JOptionPane.showMessageDialog(this, ack);
	
				this.setVisible(false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
		}else if (o == jbtnDelete) {
			// 현재의 VO객체,command = 4 = DELETE, etc = "" 로 cover만들어서
			
			cc = new CalCover(vo, DELETE, "");
			// 서버에 보내자
			System.out.println(vo.toString());
			try {
				ss.oos.writeObject(cc);
				String ack = (String) ss.ois.readObject();
				JOptionPane.showMessageDialog(this, ack);
				
				this.setVisible(false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else if (o == jbtnNew) {
			// 입력창 띄우고 이창은 끄자
			
			new Input_Module(id, ss,categoryList);
			
			this.setVisible(false);
		} else if (o == jbtnCancel) {
			this.setView();
			this.setVisible(false);

		} else if (o == jbtnStart) {
			new innerCal(jtfStart);
		} else if (o == jbtnEnd) {
			new innerCal(jtfEnd);
		}else if(o==jcmb){
			category = (String) jcmb.getSelectedItem();
		}else if(o==jbtnAdd){
			String addStr = JOptionPane.showInputDialog("카테고리 추가:");
			jcmb.addItem(addStr);
		}
	}
	
	//클릭해도 잘 안되는데 이거도 안되나
	@Override
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if (o == jbtnUpdate) {
			// disabled 되있던 jtf들을 enable하게 만들고
			jcbDay.setVisible(true);
			jcbDay.setSelected(vo.isAllDay());
			jcbTime.setVisible(true);
			jcbTime.setSelected(vo.isInputTime());
			lbStart.setVisible(true);
			lbTime.setVisible(true);
			jtfMemo.setEditable(true);
			jtfStart.setEditable(true);
			jtfEnd.setEditable(true);
			jbtnStart.setVisible(true);
			jbtnEnd.setVisible(true);
			jtfCtg.setVisible(false);
			jbtnAdd.setVisible(true);
			jcmb.setVisible(true);
			
			jtfdDay.setVisible(false);
			jlbdDay.setVisible(false);
			jbtnNew.setVisible(false);
			jbtnUpdate.setVisible(false);
			jbtnDelete.setVisible(false);
			jbtnaAllow.setBounds(20, 300, 80, 60);
			jbtnaAllow.setVisible(true);
			
			
			// makeVO로 vo객체 만들고 command = UPDATE = 3
			// etc = " "로 cover만들어서 서버에 보내자

		}
	}
	


}
