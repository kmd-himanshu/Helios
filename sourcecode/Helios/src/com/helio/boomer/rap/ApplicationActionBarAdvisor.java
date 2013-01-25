package com.helio.boomer.rap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.helio.boomer.rap.view.DeckView;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.

//	private IWorkbenchAction exitAction;
    private Action openExternalDeckAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands key bindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

//        exitAction = ActionFactory.QUIT.create(window);
//        register(exitAction);
        
        openExternalDeckAction = new Action() {
            public void run() {
              ExternalBrowser.open("com.helio.boomer.locations.deck",
                                   DeckView.BASE_URL, 
                                   ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR | 
                                   ExternalBrowser.STATUS);
            }
          };
          openExternalDeckAction.setText( "Open Monitoring" );
          openExternalDeckAction.setId( "com.helio.boomer.rap.action.opendeck" );
          openExternalDeckAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.helio.boomer.rap", "/icons/sample3.gif"));
          register( openExternalDeckAction );

    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager mainMenu = new MenuManager("&Main", IWorkbenchActionConstants.M_FILE);
        MenuManager actionMenu = new MenuManager("&Account", "com.helio.boomer.rap.menu.action");
        MenuManager adminMenu = new MenuManager("A&dmin", "com.helio.boomer.rap.menu.admin");
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(mainMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        /*
        @Date   : 13-Aug-2012
        @Author : RSystems International Ltd
        @purpose: The below line has been Commented as now we will showing the Accounts menu through changes in plugin.xml.
        @Task   : RMAP-86
        */
        
        //menuBar.add(actionMenu);
        
        // RSI changes | END
        menuBar.add(adminMenu);
        menuBar.add(helpMenu);
        
        // Main
        mainMenu.add(new Separator());
//        mainMenu.add(exitAction);
        
        // Action
        actionMenu.add(openExternalDeckAction);
        
        // Help
//        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
    }
}
