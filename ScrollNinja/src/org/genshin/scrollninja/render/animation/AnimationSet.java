package org.genshin.scrollninja.render.animation;

import java.util.HashMap;
import java.util.Map;

/**
 * 複数のアニメーションを管理するクラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class AnimationSet
{
	/**
	 * コンストラクタ
	 * @param def アニメーションセットの初期化用定義
	 */
	public AnimationSet(AnimationSetDef def)
	{
		for(AbstractAnimationDef animationDef : def.animations)
		{
			if(animationDef.name == null || animationDef.name.isEmpty())
				animationDef.name = "Animation" + animations.size();
			if(animationDef.uvSize == null)
				animationDef.uvSize = def.uvSize;
			animations.put(animationDef.name, animationDef.generate());
		}
	}
	
	/**
	 * アニメーションを取得する。
	 * @param animationName		取得するアニメーションの名前
	 * @return		指定した名前のアニメーションオブジェクト
	 */
	public AnimationInterface getAnimation(String animationName)
	{
		return animations.get(animationName);
	}
	
	
	/** アニメーションのマップ */
	private final Map<String, AnimationInterface> animations = new HashMap<String, AnimationInterface>();
}
