package login;

 

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

public class SearchList extends JFrame implements WindowListener {
	JLabel jlbTitle, jlbSD, jlbMemo;
	JTextArea jtaTitle, jtaSD, jtaMemo;
	ImageIcon imgTitle, imgSD, imgMemo;
	JPanel p1;
	JScrollPane jsp1, jsp2, jsp3;	 
	 
	
	public SearchList() {	
		p1 = new JPanel();  
		p1.setLayout(null);
		
		imgTitle = new ImageIcon("src/image/title.PNG");
		imgSD = new ImageIcon("src/image/startday.PNG");
		imgMemo = new ImageIcon("src/image/memo.PNG");
	  
		jlbTitle = new JLabel(imgTitle);
		jlbSD = new JLabel(imgSD);
		jlbMemo = new JLabel(imgMemo);
		
		jtaTitle = new JTextArea();		
		jtaSD = new JTextArea();
		jtaMemo = new JTextArea();	
		
		jsp1 = new JScrollPane(jtaTitle, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp2 = new JScrollPane(jtaSD,  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp3 = new JScrollPane(jtaMemo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jsp1.setBounds(0, 30, 113, 425);
		jsp2.setBounds(115, 30, 113, 425);
		jsp3.setBounds(230, 30, 215, 425);
		jlbTitle.setBounds(0, 0, 113, 30);
		jlbSD.setBounds(115, 0, 113, 30);
		jlbMemo.setBounds(230, 0, 215, 30);
		
	//	jtaTitle.setBounds(0, 30, 113, 400);		
	//	jtaSD.setBounds(115, 30, 113, 400);
	//	jtaMemo.setBounds(230, 30, 195, 400);	
	
		p1.add(jsp1);
		p1.add(jlbTitle);
		p1.add(jlbSD);
		p1.add(jsp2);	
		p1.add(jlbMemo);
		p1.add(jsp3);	
		//add(jtaTitle);
		//add(jtaSD);
		//add(jtaMemo);
		
		add(p1);
		setBounds(100, 100, 462, 500);
		//setVisible(true);

	}

	public SearchList(ArrayList<CallenderVO> list) {

		this();
		Calendar cal = Calendar.getInstance();
		
		String date;
		for (int i = 0; i < list.size(); i++) {
			
			String[] splt = list.get(i).getStartDay().split("-");
			date = splt[0]+"-"+splt[1]+"-"+splt[2];
			if (list.get(i).isInputTime()) {
				
				String time = splt[3]+":"+splt[4];
				jtaTitle.append(list.get(i).getTitle() + "\n");
				jtaSD.append(date +"  "+ "\n");
				if(list.get(i).getMemo()!=null){
					jtaMemo.append(list.get(i).getMemo() + "\n");
				}else{					
					jtaMemo.append("내용이 없음" + "\n");
				}
			} else if (!list.get(i).isInputTime()) {
				jtaTitle.append(list.get(i).getTitle() + "\n");
				jtaSD.append(date + "\n");
				if(list.get(i).getMemo()!=null){
					jtaMemo.append(list.get(i).getMemo() + "\n");
				}else{					
					jtaMemo.append("내용이 없음" + "\n");
				}
			}
		}
		 
	}
	
	public static void main(String[] args) {
		new SearchList();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		this.setVisible(false);
		
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
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
