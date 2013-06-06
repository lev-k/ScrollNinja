package org.genshin.scrollninja.object.background;

import org.genshin.scrollninja.render.animation.AnimationSetDef;
import org.genshin.scrollninja.render.sprite.SpriteDef;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * 背景オブジェクトの初期化用定義
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class BackgroundDef
{
	/** スプライトの初期化用定義 */
	public 	SpriteDef spriteDef;
	
	/** アニメーションセットの初期化用定義 */
	public AnimationSetDef animationSetDef;
	
	/** 再生するアニメーションの名前 */
	public String animationName;
	
	/** 色 */
	public Color color;
}
