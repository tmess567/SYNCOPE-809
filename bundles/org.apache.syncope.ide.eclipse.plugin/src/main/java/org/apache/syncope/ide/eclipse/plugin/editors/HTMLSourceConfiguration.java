package org.apache.syncope.ide.eclipse.plugin.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class HTMLSourceConfiguration extends SourceViewerConfiguration{
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer damageRepairer = new DefaultDamagerRepairer(getTagScanner());
		reconciler.setDamager(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		
		return reconciler;
	}

	private RuleBasedScanner getTagScanner() {
		RuleBasedScanner scanner = new RuleBasedScanner();
		
		Color TAG_COLOR = new Color(Display.getCurrent(), 191, 47, 117);
		Color COMMENT_COLOR = new Color(Display.getCurrent(), 110, 112, 101);
		Color DOCTYPE_COLOR = new Color(Display.getCurrent(), 148, 163, 10);
		Color STRING_COLOR = new Color(Display.getCurrent(), 224, 213, 107);
		
		IToken tagToken = new Token(new TextAttribute(TAG_COLOR));
		IToken commentToken = new Token(new TextAttribute(COMMENT_COLOR));
		IToken doctypeToken = new Token(new TextAttribute(DOCTYPE_COLOR));
		IToken stringToken = new Token(new TextAttribute(STRING_COLOR));
		
		IRule[] rules = new IRule[6];
		rules[0] = new SingleLineRule("<!DOCTYPE", ">", doctypeToken);
		rules[1] = new SingleLineRule("\"", "\"", stringToken, '\\'); 
        rules[2] = new SingleLineRule("'", "'", stringToken, '\\');
		rules[3] = new SingleLineRule("<!--", "-->", commentToken);
		rules[4] = new PatternRule("<", ">", tagToken, ' ', true, true);
		rules[5] = new SingleLineRule("</", ">", tagToken);
		
		scanner.setRules(rules);
		scanner.setDefaultReturnToken(new Token(
			new TextAttribute(new Color(Display.getCurrent(), 0, 0, 0))
		));
		
		return scanner;
	}
}
