package chatroom.client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Auto generated by eclipse's plugin <br/>
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
		shell.setText("��½");
		shell.setLayout(null);
		
		userN_text = new Text(shell, SWT.NO_BACKGROUND);
		userN_text.setEnabled(false);
		userN_text.setBounds(33, 39, 57, 23);
		userN_text.setEditable(false);
		userN_text.setText("  �û���  :");
		
		pwd_text = new Text(shell, SWT.NONE);
		pwd_text.setEnabled(false);
		pwd_text.setBounds(33, 83, 57, 23);
		pwd_text.setText("  ��  ��   :");
		pwd_text.setEditable(false);
		
		
		
		username = new Text(shell, SWT.BORDER);
		username.setBounds(96, 36, 139, 23);
		
		password = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		password.setBounds(96, 80, 139, 23);
		
		
		Button register = new Button(shell, SWT.NONE);
		register.setBounds(43, 125, 80, 27);
		register.setText("��    ½");
		register.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
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
		back.setBounds(155, 125, 80, 27);

	}

}
