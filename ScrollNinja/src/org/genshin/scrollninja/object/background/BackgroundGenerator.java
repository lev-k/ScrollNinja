package org.genshin.scrollninja.object.background;

import org.genshin.scrollninja.render.animation.AnimationSetDef;
import org.genshin.scrollninja.render.sprite.SpriteDef;

/**
 * 背景オブジェクトを生成する
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class BackgroundGenerator extends AbstractBackgroundGenerator
{
	@Override
	public void generate(BackgroundLayer targetLayer)
	{
		//---- 背景定義を設定する。
		BackgroundDef def = new BackgroundDef();
		def.spriteDef = spriteDef;
		def.animationSetDef = animationSetDef;
		def.animationName = animationName;
		def.position = position;
		def.color = color;
		
		//---- 背景を生成する。
		targetLayer.createBackground(def);
	}
	
	@Override
	public void setWorldScale(float worldScale)
	{
		super.setWorldScale(worldScale);

		if(spriteDef.position != null)	spriteDef.position.mul(worldScale);
		if(spriteDef.size != null)		spriteDef.size.mul(worldScale);
		if(spriteDef.origin != null)	spriteDef.origin.mul(worldScale);
	}


	/** スプライトの初期化用定義 */
	public 	SpriteDef spriteDef;
	
	/** アニメーションセットの初期化用定義 */
	public AnimationSetDef animationSetDef;
	
	/** 再生するアニメーションの名前 */
	public String animationName;
}
