package io.leocad.dumbledroid.general;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;

public class ModelGeneratorWizard extends Wizard implements IWorkbenchWizard {

	protected WizardFirstPage first;
	protected WizardSecPage second;
	protected WizardData data;

	public ModelGeneratorWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		data = new WizardData();
		this.first = new WizardFirstPage(data);
		addPage(this.first);
	}

	@Override
	public boolean performFinish() {
		
		if(data.jsonObject != null){
			
		}
		
		return false;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}

}
