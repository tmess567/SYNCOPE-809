package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

public class AssistInfo {
	
	private String displayString;
	private String replaceString;
	private Image image;
	private String additionalInfo;
	
	public AssistInfo(String displayString){
		this.displayString = displayString;
		this.replaceString = displayString;
	}

	public AssistInfo(String displayString,Image image){
		this.displayString = displayString;
		this.replaceString = displayString;
		this.image = image;
	}
	
	public AssistInfo(String replaceString,String displayString){
		this.displayString = displayString;
		this.replaceString = replaceString;
	}
	
	public AssistInfo(String replaceString,String displayString,Image image){
		this.displayString = displayString;
		this.replaceString = replaceString;
		this.image = image;
	}
	
	public AssistInfo(String replaceString,String displayString,Image image, String additionalInfo){
		this.displayString = displayString;
		this.replaceString = replaceString;
		this.image = image;
		this.additionalInfo = additionalInfo;
	}
	
	public String getDisplayString() {
		return displayString;
	}
	
	public String getReplaceString() {
		return replaceString;
	}
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	public Image getImage(){
		return this.image;
	}
	
	public ICompletionProposal toCompletionProposal(int offset, String matchString, Image defaultImage){
		return new CompletionProposal(
				getReplaceString(),
				offset - matchString.length(), matchString.length(),
				getReplaceString().length(),
				getImage()==null ? defaultImage : getImage(),
				getDisplayString(), null, getAdditionalInfo());
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof AssistInfo){
			AssistInfo info = (AssistInfo) obj;
			if(compareString(info.getReplaceString(), getReplaceString()) && 
					compareString(info.getDisplayString(), getDisplayString()) &&
					compareString(info.getAdditionalInfo(), getAdditionalInfo())){
				return true;
			}
		}
		return false;
	}
	
	public static boolean compareString(String value1, String value2){
		if(value1 == null && value2 == null){
			return true;
		}
		if(value1 != null && value1.equals(value2)){
			return true;
		}
		return false;
	}
	
}
