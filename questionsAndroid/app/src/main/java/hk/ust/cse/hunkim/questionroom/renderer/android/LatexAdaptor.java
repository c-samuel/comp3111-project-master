package hk.ust.cse.hunkim.questionroom.renderer.android;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import hk.ust.cse.hunkim.questionroom.renderer.android.graphics.ColorA;
import hk.ust.cse.hunkim.questionroom.renderer.android.graphics.Graphics2DA;
import hk.ust.cse.hunkim.questionroom.renderer.share.LaTeXAtom;
import hk.ust.cse.hunkim.questionroom.renderer.share.TeXConstants;
import hk.ust.cse.hunkim.questionroom.renderer.share.TeXFormula;
import hk.ust.cse.hunkim.questionroom.renderer.share.TeXIcon;
import hk.ust.cse.hunkim.questionroom.renderer.share.exception.ParseException;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.FactoryProvider;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Color;
import hk.ust.cse.hunkim.questionroom.renderer.share.platform.graphics.Insets;


/**
 * Created by CHENZiqi on 22/11/15.
 */
public class LatexAdaptor {
    public LatexAdaptor() {
        initFactoryProvider();
    }

    private void initFactoryProvider() {
        if (FactoryProvider.INSTANCE == null) {
            //FactoryProvider.INSTANCE = new FactoryProviderAndroid(getContext().getAssets());
        }
    }
}
