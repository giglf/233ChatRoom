package chatroom.client.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

import chatroom.client.util.Packet;
import chatroom.client.util.User;

/**
 * This will act as an UI list the online users.
 * @author giglf
 *
 */
public class ChatRoomUI {

	protected Text inputArea; //设置为public为了在ChatClient中设置监听事件
	protected List userList; 
	protected Shell shell;
	protected Text textDisplay;
	
	private ObjectOutputStream output;   //output
	private ObjectInputStream input;     //input
	private Socket clientSocket;         //socket to connect
	private User self;                   //store the user information
	
	private static final int PORT = 23399;
	private static final String HOSTNAME = "localhost";
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public ChatRoomUI(User user){
		self = user;
		boolean isConnect = true;
		try {
			clientSocket = new Socket(InetAddress.getByName(HOSTNAME), PORT);
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			input = new ObjectInputStream(clientSocket.getInputStream());
			output.writeObject(Packet.sendEnter(self));
			output.flush();
		} catch (Exception e) {
			isConnect = false;
			e.printStackTrace();
		}
		
		this.open(isConnect);
	}

//	public static void main(String[] args) {
//		ChatRoomUI oListUI = new ChatRoomUI();
//	}
	
	/**
	 * Open the window.
	 */
	public void open(boolean isConnect) {   //传入参数表明网络是否连接上
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		if(!isConnect){
			connectError();     //网络连接不上时弹出通知框
			shell.dispose();
		}
		run(display);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN);
		shell.setSize(600, 450);
		shell.setText("233ChatRoom");
		shell.addShellListener(new ShellAdapter() {  //当窗口关闭时关闭连接
			@Override
			public void shellClosed(ShellEvent e) {  
				terminateAll();
			}
		});
		
		userList = new List(shell, SWT.BORDER|SWT.V_SCROLL);
		userList.setBounds(446, 10, 138, 401);		
		
		inputArea = new Text(shell, SWT.BORDER);
		inputArea.setBounds(10, 341, 430, 70);
		
		textDisplay = new Text(shell, SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.WRAP);
		textDisplay.setEditable(false);
		textDisplay.setBounds(10, 10, 430, 325);
		textDisplay.append("		Welcome to 233ChatRoom   --developed by giglf\n");
		
		inputArea.addKeyListener(new KeyListener() { //输入框事件监听
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == SWT.CR) {
					String message = inputArea.getText();
					try {
						output.writeObject(Packet.sendMessage(self.getUsername(), message));   //发送消息
					} catch (Exception e) {
						e.printStackTrace();
					}
					inputArea.setText("");
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}
		});
	}
	
	//客户监听socket消息进程
	private void run(Display display){
		Thread thread = new Thread(new Runnable() {	
			@Override
			public void run() {
				while (true) {
					try {
						Packet packet = (Packet) input.readObject();
						switch (packet.getPacketType()) {
						case CLIENT_ENTER:
							display.syncExec(new Runnable() {   //swt的实现方式决定，需要用UI同步进程，不然会卡住
								@Override
								public void run() {
									clientEnter(packet.source);
								}
							});
							break;
						case CLIENT_EXIT:
							display.syncExec(new Runnable() {  //swt的实现方式决定，需要用UI同步进程，不然会卡住
								@Override
								public void run() {
									clientExit(packet.source);
								}
							});
							break;
						case MESSAGE:
							display.syncExec(new Runnable() {  //swt的实现方式决定，需要用UI同步进程，不然会卡住
								@Override
								public void run() {
									showMessage(packet.source, packet.message);
								}
							});
							break;
						case FILE_REQUEST:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						break;  //当socket关闭后，捕捉到readObject的异常，break跳出循环，线程完成关闭。
					}
				}
			}
		});
		thread.start();
	}
	
	//when connect fail. Show a Message Box.
	private void connectError(){
		MessageBox dialog=new MessageBox(shell, SWT.OK|SWT.ICON_INFORMATION);
		dialog.setText("网络连接错误");
		dialog.setMessage("请检查网络设置");
        dialog.open();
	}
	
	//handle with client enter
	private void clientEnter(String username) {
		if(userList.indexOf(username)==-1){
			userList.add(username);
			showMessage(username, "--enter this chatroom--");
		}
	}
	
	//handle with client Exit
	private void clientExit(String username) {
		userList.remove(username);
		showMessage(username, "--exit this chatroom--");
	}
	
	//show message in chat window.
	private void showMessage(String username, String message){
		textDisplay.append(username + " (" + date2String(LocalDateTime.now()) + "): " + message + "\n");
	}
	
	//Change time message to String
	private static String date2String(LocalDateTime localDateTime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		return localDateTime.format(formatter);
	}
	
	//when the windows close, that should close some connections.
	private void terminateAll(){
		try {
			output.writeObject(Packet.sendExit(self.getUsername()));
			
			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
