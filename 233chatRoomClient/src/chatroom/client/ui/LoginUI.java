package chatroom.client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import chatroom.client.util.CryptoTools;
import chatroom.client.util.DBManager;
import chatroom.client.util.User;

/**
 * This will act as LoginUI
 * @author giglf
 *
 */
public class LoginUI {
	
	protected Shell shell;
	private Text userN_text;
	private Text pwd_text;
	private Text username;
	private Text password;
	
	private DBManager dbManager;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LoginUI window = new LoginUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		dbManager = new DBManager();
		shell.open();
		shell.layout();
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
		shell.setModified(true);
		shell.setSize(300, 200);
		shell.setText("登陆");
		shell.setLayout(null);
		shell.addShellListener(new ShellAdapter() {  //监听关闭事件，当窗口关闭时，关闭数据库的连接。
			public void shellClosed(final ShellEvent e){
				dbManager.close();
			}
		});
		
		userN_text = new Text(shell, SWT.NO_BACKGROUND);
		userN_text.setEnabled(false);
		userN_text.setBounds(33, 39, 57, 23);
		userN_text.setEditable(false);
		userN_text.setText("  用户名  :");
		
		pwd_text = new Text(shell, SWT.NONE);
		pwd_text.setEnabled(false);
		pwd_text.setBounds(33, 83, 57, 23);
		pwd_text.setText("  密  码   :");
		pwd_text.setEditable(false);
		
		
		
		username = new Text(shell, SWT.BORDER);
		username.setBounds(96, 36, 139, 23);
		
		password = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		password.setBounds(96, 80, 139, 23);
		
		
		Button register = new Button(shell, SWT.NONE);
		register.setBounds(43, 125, 80, 27);
		register.setText("登    陆");
		register.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkLogin();
			}
		});
		
		Button back = new Button(shell, SWT.NONE);
		back.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RegisterUI registerUI = new RegisterUI();
				registerUI.open(dbManager);
			}
		});
		back.setText("注     册");
		back.setBounds(155, 125, 80, 27);

	}
	
	private void checkLogin(){
		
		String name = username.getText();
		String pass = password.getText();
		
		if(name.equals("") || pass.equals("")){
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("登陆失败");
			dialog.setMessage("请输入用户名或密码");
	        dialog.open();
	        return;
		}
		
		User user = dbManager.select(name, CryptoTools.getMD5(pass));
		if(user==null){
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("登陆失败");
			dialog.setMessage("用户名或密码不正确");
	        dialog.open();
		} else{
			shell.dispose();
			dbManager.close();
			ChatRoomUI client = new ChatRoomUI(user);
		}
		
	}

}
