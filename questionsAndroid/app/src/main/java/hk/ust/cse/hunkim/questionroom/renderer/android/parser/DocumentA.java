package hk.ust.cse.hunkim.questionroom.renderer.android.parser;

import hk.ust.cse.hunkim.questionroom.renderer.share.platform.parser.Document;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.parser.Element;

public class DocumentA implements Document {
	
	private org.w3c.dom.Document impl;
	
	public DocumentA(org.w3c.dom.Document impl) {
		this.impl = impl;
	}

	public Element getDocumentElement() {
		return new ElementA(impl.getDocumentElement());
	}

}
