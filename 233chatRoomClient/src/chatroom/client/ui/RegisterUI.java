package chatroom.client.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import chatroom.client.util.CryptoTools;
import chatroom.client.util.DBManager;
import chatroom.client.util.User;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

/**
 *	一个注册界面</br>
 *	通过检测数据库中是否已存在对应的用户名，注册成功把新的用户数据插入到数据库
 */
public class RegisterUI {

	protected Shell shell;
	private Text userN_text;
	private Text pwd_text;
	private Text enpwd_text;
	private Text sex_text;
	private Text username;
	private Text password;
	private Text ensurePassword;
	private Button maleButton;
	private Button femaleButton;
	
	DBManager dbManager = null;

	/**
	 * Open the window.
	 */
	public void open(DBManager dbManager) {
		Display display = Display.getDefault();
		createContents();
		this.dbManager = dbManager;
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
	 * 界面绘制
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN);
		shell.setModified(true);
		shell.setSize(300, 450);
		shell.setText("注册");
		shell.setLayout(null);
		
		
		userN_text = new Text(shell, SWT.NO_BACKGROUND);
		userN_text.setEnabled(false);
		userN_text.setBounds(33, 85, 57, 23);
		userN_text.setEditable(false);
		userN_text.setText("  用户名  :");
		
		pwd_text = new Text(shell, SWT.NONE);
		pwd_text.setEnabled(false);
		pwd_text.setBounds(33, 129, 57, 23);
		pwd_text.setText("  密  码   :");
		pwd_text.setEditable(false);
		
		enpwd_text = new Text(shell, SWT.NONE);
		enpwd_text.setEnabled(false);
		enpwd_text.setBounds(33, 167, 57, 23);
		enpwd_text.setText("确认密码 :");
		enpwd_text.setEditable(false);
		
		sex_text = new Text(shell, SWT.NONE);
		sex_text.setTouchEnabled(true);
		sex_text.setEnabled(false);
		sex_text.setBounds(33, 208, 57, 23);
		sex_text.setText("  性  别   :");
		sex_text.setEditable(false);
		
		maleButton = new Button(shell, SWT.RADIO);
		maleButton.setSelection(true);
		maleButton.setBounds(115, 208, 39, 17);
		maleButton.setText("男");
		
		femaleButton = new Button(shell, SWT.RADIO);
		femaleButton.setBounds(172, 208, 39, 17);
		femaleButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		femaleButton.setText("女");
		
		username = new Text(shell, SWT.BORDER);
		username.setBounds(96, 85, 139, 23);
		
		password = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		password.setBounds(96, 129, 139, 23);
		
		ensurePassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		ensurePassword.setBounds(96, 167, 139, 23);
		
		Button register = new Button(shell, SWT.NONE);
		register.setBounds(33, 262, 80, 27);
		register.setText("注    册");
		register.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				registerOperation();
			}
		});
		
		Button back = new Button(shell, SWT.NONE); 
		back.addSelectionListener(new SelectionAdapter() {	//返回按钮监听事件
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		back.setText("返    回");
		back.setBounds(155, 262, 80, 27);

	}
	
	//处理注册操作
	private void registerOperation(){ 
		User newUser = new User();

		String name = username.getText();
		String pass = password.getText();
		
		if(name.equals("") || pass.equals("")){ //检查是否已输入用户名密码
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("注册失败");
			dialog.setMessage("用户名密码不能为空！");
	        dialog.open();
	        return;
		}
		
		if(!ensurePassword.getText().equals(pass)){ //检查两次输入密码一致性
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("注册失败");
			dialog.setMessage("啊哈，两次输入密码不一样！");
	        dialog.open();
	        return;
		}
		newUser.setUsername(name);
		newUser.setPassword(CryptoTools.getMD5(pass));
		newUser.setSex(maleButton.getSelection());
		
		if(dbManager.insert(newUser)){   //尝试插入数据库，以成功与否判断用户是否存在
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("注册成功！");
			dialog.setMessage("快去登陆吧！");
			dialog.open();
			shell.dispose();
		} else{
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("注册失败");
			dialog.setMessage("用户已存在");
			dialog.open();
		}
		
	}
	
}
