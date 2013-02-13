package io.leocad.dumbledroid.general;

import java.io.IOException;
import java.util.ArrayList;

import io.leocad.dumbledroid.generator.ClassGenerator;
import io.leocad.dumbledroid.generator.JSONParser;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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
	private Label buttonType;
	private Button jsonButton;
	private Button xmlButton;
	private Label typeArray;
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
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 1;
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
		
		buttonType = new Label(container, SWT.NULL);
		buttonType.setText("The file in this url is a:");

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
		
		typeArray = new Label(container, SWT.NULL);
		typeArray.setText("Some internal arrays were found. Their classes should be named: ");
		typeArray.setVisible(false);
		
		final Table table = new Table(container, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		final TableColumn originalName = new TableColumn(table, SWT.NONE);
		final TableColumn finalName = new TableColumn(table, SWT.NONE);
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		final int EDITABLECOLUMN = 1;
		
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				Control oldEditor = editor.getEditor();
				if (oldEditor != null) oldEditor.dispose();
		
				TableItem item = (TableItem)e.item;
				if (item == null) return;
		
				Text newEditor = new Text(table, SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));
				newEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						Text text = (Text)editor.getEditor();
						editor.getItem().setText(EDITABLECOLUMN, text.getText());
					}
				});
				newEditor.selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		});
		table.setVisible(false);
		
		
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
					table.setVisible(true);
					
					if(data.jsonObject != null){
						JSONParser.preParseJSONObject(data.jsonObject);
					}else if(data.jsonArray != null){
						JSONParser.preParseJSONArray(data.jsonArray);
					}
					
					ArrayList<String> arrayNames = JSONParser.arrayNames;
					ArrayList<String> originalNames = JSONParser.originalNames;
					
					
					for (int i = 0; i < arrayNames.size() ; i++) {
						TableItem item = new TableItem(table, SWT.NONE);
						String name = originalNames.get(i);
						String suggestedName = arrayNames.get(i);
						item.setText(new String[] {name, suggestedName});
					}
					originalName.pack();
					finalName.pack();
					
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
					
					
					ArrayList<String> finalNames = new ArrayList<String>();
					TableItem[] items = table.getItems();
					for(int i = 0; i < items.length ; i++){
						finalNames.add(items[i].getText(1));
					}
					
					JSONParser.arrayNames = finalNames;
					
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
		
		GridData fieldData = new GridData();
		fieldData.minimumWidth = 500;
		fieldData.grabExcessHorizontalSpace = true;
		fieldData.horizontalSpan = 2;
		urlField.setLayoutData(fieldData);
		pathField.setLayoutData(fieldData);
		className.setLayoutData(fieldData);
		buttonType.setLayoutData(fieldData);
		
		GridData tableData = new GridData();
		tableData.minimumWidth = 500;
		tableData.horizontalSpan = 2;
		table.setLayoutData(tableData);
				
		GridData radioData = new GridData();
		xmlButton.setLayoutData(radioData);
		jsonButton.setLayoutData(radioData);
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}
}
