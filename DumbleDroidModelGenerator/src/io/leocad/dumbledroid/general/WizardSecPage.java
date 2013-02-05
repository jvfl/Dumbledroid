package io.leocad.dumbledroid.general;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WizardSecPage extends WizardPage{
	
	private Text urlField;
	private Composite container;
	private WizardData data;

	public WizardSecPage(WizardData data) {
		super("DumbleDroid Model Generator");
		setTitle("DumbleDroid Model Generator");
		setDescription("Settings Page");
		this.data = data;
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		container = new Composite(parent, SWT.NULL);
		RowLayout layout = new RowLayout();
		container.setLayout(layout);

		Label label1 = new Label(container, SWT.NULL);
		label1.setText("Digite a url:");

		urlField = new Text(container, SWT.BORDER | SWT.SINGLE);
		urlField.setText("");
		urlField.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!urlField.getText().isEmpty()) {
					setPageComplete(true);

				}
			}

		});
		
		RowData gd = new RowData();
		urlField.setLayoutData(gd);
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

}
