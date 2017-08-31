package login;

import java.io.Serializable;

public class MemCover implements Serializable{
	private MemberVO v;
	private int command;
	public MemCover(){
		
	}
	public MemCover(MemberVO v, int command){
		this.v = v;
		this.command = command;
	}
	public MemberVO getV() {
		return v;
	}
	public void setV(MemberVO v) {
		this.v = v;
	}
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	
	// command == 1 일때 회원가입

}
