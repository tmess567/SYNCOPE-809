package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import java.util.ArrayList;
import java.util.List;

public class TagInfo {

	private String tagName;
	private boolean hasBody;
	private boolean emptyTag;
	private String description;
	private List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
	private List<String> children = new ArrayList<String>();
	
	public static final int NONE  = 0;
	public static final int EVENT = 1;
	public static final int FORM  = 2;
	
	public TagInfo(String tagName,boolean hasBody){
		this(tagName,hasBody,false);
	}
	
	public TagInfo(String tagName,boolean hasBody,boolean emptyTag){
		this.tagName = tagName;
		this.hasBody = hasBody;
		this.emptyTag = emptyTag;
	}
	
	public String getTagName(){
		return this.tagName;
	}
	
	public boolean hasBody(){
		return this.hasBody;
	}
	
	public boolean isEmptyTag(){
		return this.emptyTag;
	}
	
	public void addAttributeInfo(AttributeInfo attribute){
		int i = 0;
		for(;i<attributes.size();i++){
			AttributeInfo info = attributes.get(i);
			if(info.getAttributeName().compareTo(attribute.getAttributeName()) > 0){
				break;
			}
		}
		this.attributes.add(i,attribute);
	}
	
	public AttributeInfo[] getAttributeInfo(){
		return this.attributes.toArray(new AttributeInfo[this.attributes.size()]);
	}
	
	public AttributeInfo[] getRequiredAttributeInfo(){
		ArrayList<AttributeInfo> list = new ArrayList<AttributeInfo>();
		for(int i=0;i<attributes.size();i++){
			AttributeInfo info = (AttributeInfo)attributes.get(i);
			if(info.isRequired()){
				list.add(info);
			}
		}
		return list.toArray(new AttributeInfo[list.size()]);
	}
	
	public AttributeInfo getAttributeInfo(String name){
		for(int i=0;i<attributes.size();i++){
			AttributeInfo info = attributes.get(i);
			if(info.getAttributeName().equals(name)){
				return info;
			}
		}
		return null;
	}
	
	public void addChildTagName(String name){
		children.add(name);
	}
	
	public String[] getChildTagNames(){
		return children.toArray(new String[children.size()]);
	}
	
	@Override public boolean equals(Object obj){
		if(obj instanceof TagInfo){
			TagInfo tagInfo = (TagInfo)obj;
			if(tagInfo.getTagName().equals(getTagName())){
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDisplayString(){
	    return getTagName();
	}
	
}
