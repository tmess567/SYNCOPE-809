package org.apache.syncope.ide.eclipse.plugin.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.syncope.ide.eclipse.plugin.views.TreeObject;

class TreeParent extends TreeObject {
	private List<TreeObject> children;

	public TreeParent(String name) {
		super(name);
		children = new ArrayList<TreeObject>();
	}

	public void addChild(TreeObject child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChild(TreeObject child) {
		children.remove(child);
		child.setParent(null);
	}

	public TreeObject[] getChildren() {
		return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}
}