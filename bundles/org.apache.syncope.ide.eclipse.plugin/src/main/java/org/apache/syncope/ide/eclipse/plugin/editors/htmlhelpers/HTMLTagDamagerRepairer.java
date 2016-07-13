package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

public class HTMLTagDamagerRepairer extends DefaultDamagerRepairer {

	public HTMLTagDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
	}

	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged) {
		if (!documentPartitioningChanged) {
			String source = fDocument.get();
			int start = source.substring(0, e.getOffset()).lastIndexOf('<');
			if(start == -1){
				start = 0;
			}
			int end = source.indexOf('>', e.getOffset());
			int nextEnd = source.indexOf('>', end + 1);
			if(nextEnd >= 0 && nextEnd > end){
				end = nextEnd;
			}
			int end2 = e.getOffset() + (e.getText() == null ? e.getLength() : e.getText().length());
			if(end == -1){
				end = source.length();
			} else if(end2 > end){
				end = end2;
			} else {
				end++;
			}

			return new Region(start, end - start);
		}
		return partition;
	}

}