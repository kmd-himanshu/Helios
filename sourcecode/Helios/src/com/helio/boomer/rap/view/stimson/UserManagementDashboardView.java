package com.helio.boomer.rap.view.stimson;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.helio.app.boomer.common.dal.model.User;
import com.helio.boomer.rap.dto.ClientDTO;
import com.helio.boomer.rap.dto.LocationDTO;
import com.helio.boomer.rap.dto.UserDTO;
import com.helio.boomer.rap.engine.UserListController;
import com.helio.boomer.rap.engine.model.PE_User;
import com.helio.boomer.rap.engine.modellist.UserModelList;
import com.helio.boomer.rap.engine.servicedata.UserManagementDAO;
import com.helio.boomer.rap.service.PasswordGenerationService;
import com.helio.boomer.rap.service.SendEmailServices;
import com.helio.boomer.rap.utility.BorderLayoutUserMgmtDashboard;

/*
 @Date   : 7-Sep-2012
 @Author : RSystems International Ltd
 @purpose: Creating a User Admin and Global Admin Screen in User Management dashboard
 @Task   : RMAP-115
 */
public class UserManagementDashboardView extends ViewPart {

	public static final String ID = "com.helio.boomer.rap.view.stimson.usermanagementdashboardview";

	// Widgets used in the window
	private Text fuserName;
	private Text luserName;
	private Text role;
	private Text client;
	private Text email;
	private DateTime expire;
	private UserDTO userDTO = new UserDTO();
	private Combo roleCombo;
	private Combo clientCombo;
	private Button yes;
	private Button no;
	Text expireText;

	String userRole = "ClientAdmin";
	String mode = "create";
	long userId = 66593240;

	UserManagementDAO userManagementDAO = new UserManagementDAO();

	User user = null;

	public UserManagementDashboardView() {
		// Initialize Preferences from Store
		System.out.println("Initializing Preferences");
		try {
			String checkVal = RWT.getSettingStore().getAttribute("myAttribute");
			System.out.println("Got stored attribute of: " + checkVal);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createPartControl(Composite parent) {

setParent(parent);
		
//		parent.setLayout(new FillLayout(SWT.VERTICAL));
		parent.setLayout(new BorderLayoutUserMgmtDashboard());		
		
		Label labelImg = new Label(parent, SWT.LEFT);
		Label labelText = new Label(parent, SWT.LEFT);
		
	
		Display display = parent.getDisplay();
		
//		Image image = new Image(display, "coins.png");
		
//		 Image i = new Image(Display.getCurrent(), filepath);
		
		
		
		
		
		ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin("com.helio.boomer.rap", "/icons/UM1.jpg");
		Image image = id.createImage();
		labelImg.setImage(image);
//		labelImg.setSize(100, 100);
//		
//		labelText.setSize(200, 200);
		labelText.setText("PLEASE SELECT USER MANAGEMENT TASK FROM THE NAVIGATION PANE");
		
		
		FontData fontData = labelText.getFont().getFontData()[0];
		Font font = new Font(display, new FontData(fontData.getName(), fontData
		    .getHeight(), SWT.ITALIC));
		labelText.setFont(font);
		
	/////////
		
		labelImg.setLayoutData(new BorderLayoutUserMgmtDashboard.BorderData(BorderLayoutUserMgmtDashboard.CENTER));
		labelText.setLayoutData(new BorderLayoutUserMgmtDashboard.BorderData(BorderLayoutUserMgmtDashboard.SOUTH));
		
		
		////////

		
		/////////
//		lbl.setSize(20, 200);
//		lbl.setText("USER MANAGEMENT BY SELECTION FROM User Management Navigation");
//		lbl.setLocation(100, 100);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * This method is used to create Expire Calender field
	 * 
	 * @param parent
	 */
	public void createExpireDateTimeAndTextField(Composite parent) {
		if (userRole.equals("ClientAdmin")) {
			new Label(parent, SWT.LEFT).setText("Expires:");
			yes = new Button(parent, SWT.RADIO);
			yes.setText("Yes");
			yes.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if (yes.getSelection()) {
						expire.setVisible(true);
					} else {
						expire.setVisible(false);
					}

				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					if (yes.getSelection()) {
						expire.setVisible(true);
					} else {
						expire.setVisible(false);
					}

				}
			});
			expire = new DateTime(parent, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
			expire.setVisible(false);
			new Label(parent, SWT.LEFT).setText("");
			no = new Button(parent, SWT.RADIO);
			no.setText("No");
			expireText = new Text(parent, SWT.READ_ONLY);
			expireText.setText("never Expire");
			expireText.setVisible(false);
			no.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if (no.getSelection()) {
						expireText.setVisible(true);
					} else {
						expireText.setVisible(false);
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					if (no.getSelection()) {
						expireText.setVisible(true);
					} else {
						expireText.setVisible(false);
					}
				}
			});

		}
	}

	/**
	 * This method is used to create First Name text box
	 * 
	 * @param parent
	 */
	public void createFirstNameTextBox(Composite parent) {

		if (mode.equals("create")) {
			fuserName = new Text(parent, SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			fuserName.setLayoutData(data);
		} else if (mode.equals("edit") && null != user) {
			fuserName = new Text(parent, SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			fuserName.setLayoutData(data);
			fuserName.setText(user.getFirstName());
		} else if (mode.equals("view") && null != user) {
			fuserName = new Text(parent, SWT.READ_ONLY);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			fuserName.setLayoutData(data);
			fuserName.setText(user.getFirstName());
		}
	}

	/**
	 * This method is used to create Last Name text box
	 * 
	 * @param parent
	 */
	public void createLastNameTextBox(Composite parent) {
		if (mode.equals("create")) {
			luserName = new Text(parent, SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			luserName.setLayoutData(data);
		} else if (mode.equals("edit")) {
			luserName = new Text(parent, SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			luserName.setLayoutData(data);
			luserName.setText(user.getLastName());
		} else if (mode.equals("view") && null != user) {
			luserName = new Text(parent, SWT.READ_ONLY);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			luserName.setLayoutData(data);
			luserName.setText(user.getLastName());
		}
	}

	/**
	 * This method is used to create a Expires date picker field
	 * 
	 * @param parent
	 */
	public void createExpireDatePicker(Composite parent) {
		// if (mode.equals("create")) {
		expire = new DateTime(parent, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
		// expire.setDay(01);
		// expire.setYear(0);
		// expire.setMonth(0);
		expire.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				expire.setDate(expire.getYear(), expire.getMonth(),
						expire.getDay());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		// } else if (mode.equals("edit") && null != user) {
		// String[] values = convertFromSqlDateToDateTime(user.getExpire());
		// int year = Integer.valueOf(values[0]);
		// int month = Integer.valueOf(values[1]);
		// int day = Integer.valueOf(values[2]);
		// expire = new DateTime(parent, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
		// expire.setDate(year, month - 1, day);
		// } else if (mode.equals("view") && null != user) {
		// String[] values = convertFromSqlDateToDateTime(user.getExpire());
		//
		// Text expire = new Text(parent, SWT.READ_ONLY);
		// String date = values[1].concat("/").concat(values[2]).concat("/")
		// .concat(values[0]);
		// expire.setText(date);
		// }
	}

	/**
	 * This method is used to create a Email Text box
	 * 
	 * @param parent
	 */
	public void createEmailText(Composite parent) {
		if (mode.equals("create")) {
			email = new Text(parent, SWT.BORDER);
			GridData data3 = new GridData(GridData.FILL_HORIZONTAL);
			data3.horizontalSpan = 2;
			email.setLayoutData(data3);
		} else if (mode.equals("edit") && null != user) {
			email = new Text(parent, SWT.BORDER);
			GridData data3 = new GridData(GridData.FILL_HORIZONTAL);
			data3.horizontalSpan = 2;
			email.setLayoutData(data3);
			email.setText(user.getEmailAddr());
		} else if (mode.equals("view") && null != user) {
			email = new Text(parent, SWT.READ_ONLY);
			GridData data3 = new GridData(GridData.FILL_HORIZONTAL);
			data3.horizontalSpan = 2;
			email.setLayoutData(data3);
			email.setText(user.getEmailAddr());
		}
	}

	public void createSaveButton(Composite parent) {
		final Button btn = new Button(parent, SWT.BUTTON1);
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (fuserName.getText().equals("")
						|| fuserName.getText().equals(null)) {

					MessageDialog.openError(getSite().getShell(), "Error",
							"First Name should not be blank or Empty");
				} else {
					userDTO.setFuserName(fuserName.getText());
				}
				if (luserName.getText().equals("")
						|| luserName.getText().equals(null)) {

					MessageDialog.openError(getSite().getShell(), "Error",
							"Last Name should not be blank or Empty");
				} else {
					userDTO.setLuserName(luserName.getText());
				}
				if (email.getText().equals("") || email.getText().equals(null)) {

					MessageDialog.openError(getSite().getShell(), "Error",
							"Email should not be blank or Empty");
				} else {
					userDTO.setEmail(email.getText());
				}
				if (expire.getDay() == 1
						&& expire.getMonth() == 0
						&& expire.getYear() == Calendar.getInstance().get(
								Calendar.YEAR)) {
					expire.setDate(01, 0, 0);
				} else {
					userDTO.setExpire(expire);
				}

				if (userRole.equals("GlobalAdmin")) {

					if (null != roleCombo.getText()
							&& "" != roleCombo.getText()) {

						System.out.println("combo input role value : "
								+ roleCombo.getText());
						// userDTO.setRole(roleCombo.getText());

					} else if (roleCombo.getSelectionIndex() > -1) {

						int x = roleCombo.getSelectionIndex();
						System.out.println("selected combo index : " + x);
						System.out.println("selected combo value : "
								+ roleCombo.getItem(x));
						// userDTO.setRole(roleCombo.getItem(x));
					} else {
						MessageDialog.openError(getSite().getShell(), "Error",
								"Please enter the Role in Role drop down.");
					}

					if (null != clientCombo.getText()
							&& "" != clientCombo.getText()) {

						System.out.println("combo input client value : "
								+ clientCombo.getText());
						userDTO.setClient(clientCombo.getText());

					} else if (clientCombo.getSelectionIndex() > -1) {

						int x = clientCombo.getSelectionIndex();
						System.out.println("selected combo index : " + x);
						System.out.println("selected combo value : "
								+ clientCombo.getItem(x));
						userDTO.setClient(clientCombo.getItem(x));
					} else {
						MessageDialog.openError(getSite().getShell(), "Error",
								"Please enter the Client in Client drop down.");
					}

				} else {
					// userDTO.setRole(role.getText());
					userDTO.setClient(client.getText());
				}

				/*
				 * Going to set password
				 */

				PasswordGenerationService passwordService = new PasswordGenerationService();
				if (null != passwordService.getUserPassword()) {
					userDTO.setPassword(passwordService.getUserPassword());
				} else {
					MessageDialog
							.openError(getSite().getShell(), "Error",
									"There is an issue while password generation,Please contact to Admin");
				}
				if (mode.equals("create")) {

					UserDTO user1 = userManagementDAO.addUser(userDTO);
					if (null != user1 && 0L != user1.getUserId()) {
						MessageDialog.openConfirm(getSite().getShell(),
								"Success", user1.getMessage());
						SendEmailServices sendEmail = new SendEmailServices();
						UserDTO userEmail = sendEmail.sendEmail(user1);
						if (null != userEmail.getMessage()) {
							MessageDialog.openConfirm(getSite().getShell(),
									"Success", userEmail.getMessage());
						} else if (null != userEmail.getErrorMessage()) {
							MessageDialog.openConfirm(getSite().getShell(),
									"Error", userEmail.getErrorMessage());
						}
					} else if (null != user1) {
						MessageDialog.openConfirm(
								getSite().getShell(),
								"Error",
								null != user1.getErrorMessage() ? user1
										.getErrorMessage() : user1
										.getExMessage());

					}
				} else if (mode.equals("edit")) {

					userDTO.setUserId(user.getId());
					userDTO.setPassword(user.getPassword());

					UserDTO user1 = userManagementDAO.updateUser(userDTO, user);
					if (null != user1 && 0L != user1.getUserId()) {
						MessageDialog.openConfirm(getSite().getShell(),
								"Success", user1.getMessage());
					} else if (null != user1) {
						MessageDialog.openConfirm(
								getSite().getShell(),
								"Error",
								null != user1.getErrorMessage() ? user1
										.getErrorMessage() : user1
										.getExMessage());
					}
				}
			}
		});
		if (mode.equals("create")) {
			btn.setText("Save");
		} else if (mode.equals("edit")) {
			btn.setText("Save&Update");
		} else if (mode.equals("view")) {
			btn.setVisible(false);
		}
	}

	/**
	 * This method is used to create a location drop down
	 * 
	 * @param parent
	 */
	public void createLocationCombo(Composite parent, String clientName) {

		new Label(parent, SWT.LEFT).setText("Location : ");
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER);

		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		// sc.setAlwaysShowScrollBars(true);
		sc.setMinHeight(150);
		sc.setMinWidth(200);

		org.eclipse.swt.widgets.List locationCombo = new org.eclipse.swt.widgets.List(
				sc, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		locationCombo.setBounds(40, 20, 200, 150);
		/*
		 * Going to call database to populate location multiple select combo
		 */

		ClientDTO clientDto = new ClientDTO();
		clientDto.setClientName(clientName);
		List<LocationDTO> locationList = userManagementDAO
				.getLocationFromClient(clientDto);
		if (null != locationList || !"".equals(locationList)) {
			Iterator<LocationDTO> locationItr = locationList.iterator();
			for (; locationItr.hasNext();) {
				LocationDTO location = locationItr.next();
				locationCombo.add(location.getLocationName());
			}
		} else {
			new Text(parent, SWT.READ_ONLY)
					.setText("Client is not associated with any Location.");

		}

	}

	/**
	 * This method is used to create a role drop down
	 * 
	 * @param parent
	 */
	public void createRoleCombo(Composite parent) {
		new Label(parent, SWT.LEFT).setText("Role:");

		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER);

		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		// sc.setAlwaysShowScrollBars(true);
		sc.setMinHeight(150);
		sc.setMinWidth(200);

		org.eclipse.swt.widgets.List locationCombo = new org.eclipse.swt.widgets.List(
				sc, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		locationCombo.setBounds(40, 20, 200, 150);
		/*
		 * Going to call database to populate location multiple select combo
		 */

		for (int loopIndex = 0; loopIndex < 9; loopIndex++) {
			locationCombo.add("Item Number " + loopIndex);
		}

	}

	/**
	 * This method is used to create client drop down
	 * 
	 * @param parent
	 */
	public void createClientCombo(Composite parent) {
		new Label(parent, SWT.LEFT).setText("Client:");
		if (mode.equals("create")) {
			clientCombo = new Combo(parent, SWT.NONE);
			clientCombo.setBounds(50, 50, 150, 65);
			List<ClientDTO> clients = userManagementDAO.getAllClients();
			String[] clientArr = new String[clients.size()];
			Iterator<ClientDTO> clientItr = clients.iterator();
			for (int i = 0; clientArr.length > i && clientItr.hasNext(); i++) {
				clientArr[i] = clientItr.next().getClientName();
			}
			String clientItems[] = clientArr;
			clientCombo.setItems(clientItems);
			clientCombo.setVisible(true);
		} else if (mode.equals("edit") && null != user) {
			clientCombo = new Combo(parent, SWT.NONE);
			clientCombo.setBounds(50, 50, 150, 65);
			List<ClientDTO> clients = userManagementDAO.getAllClients();
			String[] clientArr = new String[clients.size()];
			Iterator<ClientDTO> clientItr = clients.iterator();
			for (int i = 0; clientArr.length > i && clientItr.hasNext(); i++) {
				clientArr[i] = clientItr.next().getClientName();
			}
			String clientItems[] = clientArr;
			clientCombo.setItems(clientItems);
			clientCombo.setText(user.getClient().getName());
			clientCombo.setVisible(true);
		}
		new Label(parent, SWT.LEFT).setText("");
	}

	/**
	 * This method is used to convert sql date to String array
	 * 
	 * @param date
	 * @return array of string
	 */
	private String[] convertFromSqlDateToDateTime(java.sql.Date date) {
		String dats = String.valueOf(date);
		String[] vals = dats.split("-");
		return vals;
	}

	// *********************************************************************************************************//
	public void createForm(String mode, PE_User user, Viewer viewer) {
		Composite comp = getParent();
		Display display = comp.getDisplay();
		Shell shell = new Shell(display, SWT.None);
		Point pointSize = comp.getSize();

		shell.setSize(pointSize);
		shell.setLocation(320, 210);
		createContents(shell, mode);

		/*
		 * To enable multiple clicks on the sub-menu items under the menu item
		 */
		UserListController ulc = UserListController.getInstance();
		UserModelList uml = ulc.getUserModelList();
		viewer.setInput(uml);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

	public void createContents(Composite parent, String mode) {
		user = userManagementDAO.getUserById(userId);
		parent.setLayout(new GridLayout(3, true));
		// Create the label and input box
		new Label(parent, SWT.LEFT).setText("First Name of User:");
		createFirstNameTextBox(parent);
		// Create the label and input box
		new Label(parent, SWT.LEFT).setText("Last Name of User:");
		createLastNameTextBox(parent);
		if (userRole.equals("ClientAdmin")) {
			new Label(parent, SWT.LEFT).setText("Role:");
			if (mode.equals("create")) {
				role = new Text(parent, SWT.READ_ONLY);
				GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
				data1.horizontalSpan = 2;
				role.setLayoutData(data1);
				role.setText("User");
			} else if (mode.equals("edit") && null != user) {
				role = new Text(parent, SWT.READ_ONLY);
				GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
				data1.horizontalSpan = 2;
				role.setLayoutData(data1);
				role.setText("User");
			} else if (mode.equals("view")) {
				role = new Text(parent, SWT.READ_ONLY);
				GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
				data1.horizontalSpan = 2;
				role.setLayoutData(data1);
				role.setText("User");
			}
			role.setVisible(true);
		}
		if (userRole.equals("GlobalAdmin") && !mode.equals("view")) {
			createRoleCombo(parent);
		} else if (userRole.equals("GlobalAdmin") && mode.equals("view")) {
			new Label(parent, SWT.LEFT).setText("Role:");
			Text clientCombo = new Text(parent, SWT.READ_ONLY);
			GridData data4 = new GridData(GridData.FILL_HORIZONTAL);
			data4.horizontalSpan = 2;
			clientCombo.setLayoutData(data4);
			clientCombo.setText("Client in view mode");
			clientCombo.setVisible(true);
		}
		if (userRole.equals("ClientAdmin")) {
			new Label(parent, SWT.LEFT).setText("Client:");
			client = new Text(parent, SWT.READ_ONLY);
			GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
			data2.horizontalSpan = 2;
			client.setLayoutData(data2);
			if (mode.equals("create")) {
				client.setText("Safeway");
			} else if (mode.equals("edit") || mode.equals("view")
					&& null != user) {
				client.setText(user.getClient().getName());
			}
			client.setVisible(true);
		}
		if (userRole.equals("GlobalAdmin") && !mode.equals("view")) {
			new Label(parent, SWT.LEFT).setText("");
			createClientCombo(parent);
		} else if (userRole.equals("GlobalAdmin") && mode.equals("view")) {
			new Label(parent, SWT.LEFT).setText("Client:");
			Text clientCombo = new Text(parent, SWT.READ_ONLY);
			GridData data4 = new GridData(GridData.FILL_HORIZONTAL);
			data4.horizontalSpan = 2;
			clientCombo.setLayoutData(data4);
			clientCombo.setText(user.getClient().getName());
			clientCombo.setVisible(true);
		}
		new Label(parent, SWT.LEFT).setText("Email:");
		createEmailText(parent);
		createExpireDateTimeAndTextField(parent);
		if (userRole.equals("GlobalAdmin") && mode.equals("create")) {

			new Label(parent, SWT.LEFT).setText("Location :");
			Text location = new Text(parent, SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			location.setLayoutData(data);
		} else if (userRole.equals("ClientAdmin") && null != client.getText()
				&& !"".equals(client.getText())) {
			createLocationCombo(parent, client.getText());
		}
		new Label(parent, SWT.LEFT).setText("");
		createSaveButton(parent);
	}

	public Composite getParent() {

		return globParent;
	}

	private void setParent(Composite parent) {
		globParent = parent;

	}

	public String getMode() {

		return mode;
	}

	private void setMode(String mod) {
		mode = mod;

	}

	Composite globParent = null;

	// *********************************************************************************************************//

}