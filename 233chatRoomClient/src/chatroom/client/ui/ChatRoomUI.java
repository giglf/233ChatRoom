package chatroom.client.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

import chatroom.client.util.Packet;
import chatroom.client.util.User;

/**
 *	用户真正聊天界面</br>
 *	同时也是客户端的主程序，用于建立与服务端的Socket链接</br>
 *	接受来自服务端的数据
 */
public class ChatRoomUI {

	protected Text inputArea;
	protected List userList; 
	protected Shell shell;
	protected Text textDisplay;
	
	private ObjectOutputStream output;   //output
	private ObjectInputStream input;     //input
	private Socket clientSocket;         //socket to connect
	private User self;                   //store the user information
	
	private static final int PORT = 23399;
	private static final String HOSTNAME = "localhost";
	

	
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
		run(display); //循环接受来自服务端的数据
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN);
		shell.setSize(600, 450);
		shell.setText(self.getUsername() + "'s chatroom");
		shell.addShellListener(new ShellAdapter() {  //当窗口关闭时关闭连接
			@Override
			public void shellClosed(ShellEvent e) {  
				terminateAll();
			}
		});
		
		userList = new List(shell, SWT.BORDER|SWT.V_SCROLL);
		userList.setBounds(446, 10, 138, 401);		
		
		Menu userHandleMenu = new Menu(userList);
		MenuItem sendFileItem = new MenuItem(userHandleMenu, SWT.PUSH);
		sendFileItem.setText("Send File");
		userList.setMenu(userHandleMenu);
		
		//发送文件按钮监听
		sendFileItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
					if(userList.getSelectionIndex() == -1){
						selectUserError();
					} else{
						//获取想要发送的用户
						int select = userList.getSelectionIndex();
						User selectUser = new User();
						selectUser.setUsername(userList.getItem(select));
						
						//选择要发送的文件
						FileDialog fd=new FileDialog(shell,SWT.OPEN); 
						String filePath = fd.open();
						textDisplay.append("******Sending " + filePath + " to " + selectUser.getUsername() + "******\n");
						
						//发送文件
						File file = new File(filePath);
						try {
							output.writeObject(Packet.sendFileRequest(self, selectUser, file.getName(), file.length()));
							sendFile(file);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		inputArea = new Text(shell, SWT.BORDER);
		inputArea.setBounds(10, 341, 430, 70);
		
		textDisplay = new Text(shell, SWT.BORDER|SWT.V_SCROLL|SWT.MULTI|SWT.WRAP);
		textDisplay.setEditable(false);
		textDisplay.setBounds(10, 10, 430, 325);
		textDisplay.append("		Welcome to 233ChatRoom   --developed by giglf\n");
		
		inputArea.addKeyListener(new KeyListener() { //输入框事件监听
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == SWT.CR) {		//检测到键盘回车键被按下时发送消息 （小键盘的回车键不行）
					String message = inputArea.getText();
					try {
						output.writeObject(Packet.sendMessage(self, message));   //发送消息
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
									showMessage(packet.source.getUsername(), packet.message);
								}
							});
							break;
						case FILE_REQUEST:
							display.syncExec(new Runnable() {  //swt的实现方式决定，需要用UI同步进程，不然会卡住
								@Override
								public void run() {
									showMessage(packet.source.getUsername(), "Sending " + packet.message + " to you.\n");
									downloadFile(packet);
								}
							});
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
	
	//handle with client enter
	private void clientEnter(User user) {
		String username = user.getUsername();
		if(userList.indexOf(username)==-1){
			userList.add(username);
			showMessage(username, "--enter this chatroom--");
		}
	}
	
	//handle with client Exit
	private void clientExit(User user) {
		String username = user.getUsername();
		userList.remove(username);
		showMessage(username, "--exit this chatroom--");
	}
	
	//show message in chat window.
	private void showMessage(String username, String message){
		textDisplay.append(username + " (" + date2String(LocalDateTime.now()) + "): " + message + "\n");
	}
	
	//发送文件
	private void sendFile(File file) {
		try {
			Socket sendFileSocket = new Socket(InetAddress.getByName(HOSTNAME), 6505);
			System.out.println("Connected to server on port 6505...");
			byte[] fileBytes = new byte[(int) file.length()];

			BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(file));
			int bytesRead = buffer.read(fileBytes, 0, fileBytes.length);
			System.out.format("Read %d bytes from %s.%n", bytesRead, file.getName());

			OutputStream out = sendFileSocket.getOutputStream();
			out.write(fileBytes, 0, fileBytes.length);
			out.flush();
			
			System.out.println("File sending finished");
			buffer.close();
			out.close();
			sendFileSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//接受别人发来的文件
	public void downloadFile(Packet packet){
		try {
			Socket downloadSocket = new Socket(InetAddress.getByName(HOSTNAME), 6506);
			System.out.println("Connected to server on port 6506");
			
			byte[] fileBytes = new byte[(int) packet.filesize];
			InputStream input = downloadSocket.getInputStream();
			BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(packet.message));
			
			//每次读写操作是缓冲区大小有限制，必须通过循环读写完整的文件，不然当文件太大时会抛出读不到文件结尾的错误
			int bytesRead = input.read(fileBytes, 0, fileBytes.length);
			int currentOffset = bytesRead;
			do{
				bytesRead = input.read(fileBytes, currentOffset, fileBytes.length-currentOffset);
				if(bytesRead >= 0){
					currentOffset += bytesRead;
				}
			}while(currentOffset < fileBytes.length);
			
			buffer.write(fileBytes, 0, fileBytes.length);
			buffer.flush();
			
			input.close();
			buffer.close();
			downloadSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	//when connect fail. Show a Message Box.
	private void connectError(){
		MessageBox dialog=new MessageBox(shell, SWT.OK|SWT.ICON_INFORMATION);
		dialog.setText("网络连接错误");
		dialog.setMessage("请检查网络设置");
        dialog.open();
	}
	
	//When sendFile, should select user first.
	private void selectUserError(){
		MessageBox dialog=new MessageBox(shell, SWT.OK|SWT.ICON_INFORMATION);
		dialog.setText("操作错误");
		dialog.setMessage("请先选择用户");
        dialog.open();
	}
	
	//Change time message to String
	private static String date2String(LocalDateTime localDateTime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		return localDateTime.format(formatter);
	}
	
	//when the windows close, that should close some connections.
	private void terminateAll(){
		try {
			output.writeObject(Packet.sendExit(self));
			
			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
