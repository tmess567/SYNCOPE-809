package org.apache.syncope.ide.eclipse.plugin.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class XSLSourceConfiguration extends SourceViewerConfiguration{
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler presentationReconciler = new PresentationReconciler();
		DefaultDamagerRepairer damageRepairer = new DefaultDamagerRepairer(getTagScanner());
		presentationReconciler.setDamager(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		presentationReconciler.setRepairer(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		return presentationReconciler;
	}

	private RuleBasedScanner getTagScanner() {
		RuleBasedScanner scanner = new RuleBasedScanner();
		
		Color TAG_COLOR = new Color(Display.getCurrent(), 200, 0, 0);
		Color COMMENT_COLOR = new Color(Display.getCurrent(), 200, 0, 0);
		
		IToken tagToken = new Token(new TextAttribute(TAG_COLOR));
		IToken commentToken = new Token(new TextAttribute(COMMENT_COLOR));
		IRule[] rules = new IRule[2];
		rules[0] = new SingleLineRule("<tushar", "tushar>", tagToken);
		rules[1] = new EndOfLineRule("//", commentToken);
		scanner.setRules(rules);
		scanner.setDefaultReturnToken(new Token(
			new TextAttribute(new Color(Display.getCurrent(), 0, 0, 0))
		));
		
		return scanner;
	}
	
}
