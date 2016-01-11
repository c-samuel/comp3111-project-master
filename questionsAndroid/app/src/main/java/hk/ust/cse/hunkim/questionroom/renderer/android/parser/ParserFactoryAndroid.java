package hk.ust.cse.hunkim.questionroom.renderer.android.parser;

import hk.ust.cse.hunkim.questionroom.renderer.share.platform.parser.Parser;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.parser.ParserFactory;

public class ParserFactoryAndroid extends ParserFactory {

	@Override
	public Parser createParser() {
		return new ParserA();
	}

}
