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

/**
 * Auto generated by eclipse's plugin <br/>
 * This will act as registerUI
 * @author giglf
 *
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
	Button maleButton;
	Button femaleButton;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RegisterUI window = new RegisterUI();
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
	 * �������
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN);
		shell.setModified(true);
		shell.setSize(300, 450);
		shell.setText("ע��");
		shell.setLayout(null);
		
		userN_text = new Text(shell, SWT.NO_BACKGROUND);
		userN_text.setEnabled(false);
		userN_text.setBounds(33, 85, 57, 23);
		userN_text.setEditable(false);
		userN_text.setText("  �û���  :");
		
		pwd_text = new Text(shell, SWT.NONE);
		pwd_text.setEnabled(false);
		pwd_text.setBounds(33, 129, 57, 23);
		pwd_text.setText("  ��  ��   :");
		pwd_text.setEditable(false);
		
		enpwd_text = new Text(shell, SWT.NONE);
		enpwd_text.setEnabled(false);
		enpwd_text.setBounds(33, 167, 57, 23);
		enpwd_text.setText("ȷ������ :");
		enpwd_text.setEditable(false);
		
		sex_text = new Text(shell, SWT.NONE);
		sex_text.setTouchEnabled(true);
		sex_text.setEnabled(false);
		sex_text.setBounds(33, 208, 57, 23);
		sex_text.setText("  ��  ��   :");
		sex_text.setEditable(false);
		
		maleButton = new Button(shell, SWT.RADIO);
		maleButton.setSelection(true);
		maleButton.setBounds(115, 208, 39, 17);
		maleButton.setText("��");
		
		femaleButton = new Button(shell, SWT.RADIO);
		femaleButton.setBounds(172, 208, 39, 17);
		femaleButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		femaleButton.setText("Ů");
		
		username = new Text(shell, SWT.BORDER);
		username.setBounds(96, 85, 139, 23);
		
		password = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		password.setBounds(96, 129, 139, 23);
		
		ensurePassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		ensurePassword.setBounds(96, 167, 139, 23);
		
		Button register = new Button(shell, SWT.NONE);
		register.setBounds(33, 262, 80, 27);
		register.setText("ע    ��");
		register.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				registerOperation();
			}
		});
		
		Button back = new Button(shell, SWT.NONE);
		back.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		back.setText("��    ��");
		back.setBounds(155, 262, 80, 27);

	}
	
	//����ע�����
	private void registerOperation(){ 
		User newUser = new User();

		String pass = password.getText();
		if(!ensurePassword.getText().equals(pass)){ //���������������һ����
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("ע��ʧ��");
			dialog.setMessage("�����������������벻һ����");
	        dialog.open();
	        return;
		}
		newUser.setUsername(username.getText());
		newUser.setPassword(CryptoTools.getMD5(pass));
		newUser.setSex(maleButton.getSelection());
		
		DBManager dbManager = new DBManager();
		if(dbManager.insert(newUser)){   //���Բ������ݿ⣬�Գɹ�����ж��û��Ƿ����
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("ע��ɹ���");
			dialog.setMessage("��ȥ��½�ɣ�");
			dialog.open();
			shell.dispose();
		} else{
			MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
			dialog.setText("ע��ʧ��");
			dialog.setMessage("�û��Ѵ���");
			dialog.open();
		}
		
	}
	
}