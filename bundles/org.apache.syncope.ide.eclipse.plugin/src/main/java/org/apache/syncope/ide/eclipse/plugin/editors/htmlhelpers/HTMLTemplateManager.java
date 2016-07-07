package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import java.io.IOException;

import org.apache.syncope.ide.eclipse.plugin.Activator;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;

public class HTMLTemplateManager {

	private static final String CUSTOM_TEMPLATES_KEY
		= Activator.PLUGIN_ID + ".customtemplates";

	private static HTMLTemplateManager instance;
	private TemplateStore fStore;
	private ContributionContextTypeRegistry fRegistry;

	private HTMLTemplateManager(){
	}

	public static HTMLTemplateManager getInstance(){
		if(instance==null){
			instance = new HTMLTemplateManager();
		}
		return instance;
	}

	public TemplateStore getTemplateStore(){
		if (fStore == null){
			fStore = new ContributionTemplateStore(getContextTypeRegistry(),
					Activator.getDefault().getPreferenceStore(), CUSTOM_TEMPLATES_KEY);
			try {
				fStore.load();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return fStore;
	}

	public ContextTypeRegistry getContextTypeRegistry(){
		if (fRegistry == null){
			fRegistry = new ContributionContextTypeRegistry();
			fRegistry.addContextType(HTMLContextType.CONTEXT_TYPE);
		}
		return fRegistry;
	}

}
