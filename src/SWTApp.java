import com.jameslow.AirPlay;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import java.io.IOException;

public class SWTApp {

    private String title = "AirPlay - Application";
    private String menuFile = "Fichier";
    private String menuExit = "Quitter";
    private String selectFile = "Selectionner une photo";

    private Text serverText = null;

    private Shell shell;

    public SWTApp(Display display) {
        shell = new Shell(display);
        shell.setText(title);
        shell.setLayout(new FillLayout(SWT.VERTICAL));

        initMenu();
        initTextServer();
        initFileSelector();

        shell.setSize(250, 100);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private void initTextServer() {
        serverText = new Text(shell, SWT.BORDER);
        serverText.setText("freebox-player.local");
    }


    public void initMenu() {

        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&" + menuFile);

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);

        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setText("&" + menuExit);
        shell.setMenuBar(menuBar);

        exitItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }
        });

    }

    public void initFileSelector() {
        Button buttonSelectFile = new Button(shell, SWT.PUSH);
        buttonSelectFile.setText(selectFile);
        buttonSelectFile.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
                final String file = fileDialog.open();
                final String server = serverText.getText();
                Thread thread = new Thread() {
                    public void run() {
                        AirPlay airPlay = new AirPlay(server);
                        try {
                            airPlay.photo(file);
                            airPlay.stop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                thread.interrupt();
            }
        });
    }


    public static void main(String[] args) {
        Display display = new Display();
        new SWTApp(display);
        display.dispose();
    }

}
