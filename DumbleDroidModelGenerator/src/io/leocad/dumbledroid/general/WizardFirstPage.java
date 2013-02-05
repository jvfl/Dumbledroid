package io.leocad.dumbledroid.general;

import java.io.IOException;

import io.leocad.dumbledroid.generator.ClassGenerator;
import io.leocad.dumbledroid.generator.JSONParser;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.json.JSONException;

public class WizardFirstPage extends WizardPage {
	
	private Text pathField;
	private Label typePath;
	private Text urlField;
	private Label typeUrl;
	private Text className;
	private Label typeClass;
	private Button jsonButton;
	private Button xmlButton;
	private Button submitButton;
	private Composite container;
	private WizardData data;

	public WizardFirstPage(WizardData data) {
		super("DumbleDroid Model Generator");
		setTitle("DumbleDroid Model Generator");
		setDescription("Settings Page");
		this.data = data;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		RowLayout layout = new RowLayout();
		container.setLayout(layout);
		String absolutePath = "";
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    if (window != null)
	    {
	        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
	        Object firstElement = selection.getFirstElement();
	        
	       if (firstElement instanceof IAdaptable) {
	        	
	        	IResource resource = (IResource)((IAdaptable)firstElement).getAdapter(IResource.class);
	            absolutePath = resource.getLocation().toPortableString();
	            
	            int lastSlash = absolutePath.lastIndexOf("/");
	            String lastWord = absolutePath.substring(lastSlash);
	            
	            if(lastWord.contains(".")){
	            	absolutePath = absolutePath.substring(0,lastSlash)+"/";
	            }else{
	            	absolutePath += "/";
	            }
	            
	        }
	    }
	    
	    typePath = new Label(container, SWT.NULL);
		typePath.setText("Type the path where the files should be saved:");

		pathField = new Text(container, SWT.BORDER | SWT.SINGLE);
		pathField.setText(absolutePath);

		typeUrl = new Label(container, SWT.NULL);
		typeUrl.setText("Type the url:");

		urlField = new Text(container, SWT.BORDER | SWT.SINGLE);
		urlField.setText("");

		jsonButton = new Button(container, SWT.RADIO);
		jsonButton.setText("JSON");
		jsonButton.setEnabled(false);
		
		xmlButton = new Button(container, SWT.RADIO);
		xmlButton.setText("XML");
		xmlButton.setEnabled(false);
		
		typeClass = new Label(container, SWT.NULL);
		typeClass.setText("Type the base class name:");
		typeClass.setVisible(false);
		
		className = new Text(container, SWT.BORDER | SWT.SINGLE);
		className.setText("");
		className.setVisible(false);
		
		submitButton = new Button(container, SWT.BUTTON1);
		submitButton.setText("Gerar");
		submitButton.setEnabled(false);
		
		urlField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!urlField.getText().isEmpty()) {
					data.url = urlField.getText();
					xmlButton.setEnabled(true);
					jsonButton.setEnabled(true);
				}
			}

		});
		
		
		jsonButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				try {
					data.getJson();
					
					className.setVisible(true);
					typeClass.setVisible(true);
					
					if(data.jsonObject != null){
						JSONParser.preParseJSONObject(data.jsonObject);
					}else if(data.jsonArray != null){
						JSONParser.preParseJSONArray(data.jsonArray);
					}
					
				} catch (JSONException e1) {
					// Bad json
					e1.printStackTrace();
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		className.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!className.getText().isEmpty()) {
					data.url = urlField.getText();
					setPageComplete(true);
					submitButton.setEnabled(true);
				}
			}

		});
		
		submitButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				try {
					
					ClassGenerator.filePath = pathField.getText();
					
					if(data.jsonObject != null){
						JSONParser.parseJSONObject( data.jsonObject, className.getText() , "testPackage");
					}else if(data.jsonArray != null){
						JSONParser.parseJSONArray( data.jsonArray, className.getText()+"s", className.getText(), "testPackage");
					}
					
				} catch (JSONException e1) {
					// Bad json
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		

		RowData gd = new RowData();
		urlField.setLayoutData(gd);
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}
}
