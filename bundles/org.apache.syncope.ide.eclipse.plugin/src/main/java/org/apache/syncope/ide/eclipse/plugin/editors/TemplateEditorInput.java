package org.apache.syncope.ide.eclipse.plugin.editors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class TemplateEditorInput implements IStorageEditorInput {

	private final String inputString;
	private final String title;
	private final String tooltip;
	
	private String[] inputStringList;
	private String[] titleList;
	private String[] tooltipList;
	
	public String[] getInputStringList() {
		return inputStringList;
	}

	public String[] getTitleList() {
		return titleList;
	}

	public String[] getTooltipList() {
		return tooltipList;
	}

	public String getInputString() {
		return inputString;
	}
	
	public TemplateEditorInput(String inputString, String title, String tooltip) {
		this.inputString = inputString;
		this.title = title;
		this.tooltip = tooltip;
	}
	
	public TemplateEditorInput(String[] inputStringList, String[] titleList, String[] tooltipList){
		this.inputString = null;
		this.title = null;
		this.tooltip = null;
		
		this.inputStringList = inputStringList;
		this.titleList = titleList;
		this.tooltipList = tooltipList;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getName() {
		return title;
	}

	public String getToolTipText() {
		return tooltip;
	}

	public IStorage getStorage() throws CoreException {
		return new IStorage() {
			public InputStream getContents() throws CoreException {
				try {
					return new ByteArrayInputStream(inputString.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}

			public IPath getFullPath() {
				return null;
			}

			public String getName() {
				return TemplateEditorInput.this.getName();
			}

			public boolean isReadOnly() {
				return false;
			}

			public Object getAdapter(Class adapter) {
				return null;
			}
		};
	}
}