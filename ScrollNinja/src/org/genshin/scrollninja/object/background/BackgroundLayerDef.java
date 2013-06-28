package org.genshin.scrollninja.object.background;


/**
 * 背景レイヤーの初期化用定義クラス
 * @author	kou
 * @since		1.0
 * @version		1.0
 */
public class BackgroundLayerDef
{
	/** レイヤーの名前 */
	public String name;
	
	/** 背景レイヤーの倍率 */
	public float scale = 1.0f;
	
	/** 背景オブジェクト生成装置の配列 */
	public BackgroundGeneratorInterface[] backgrounds;
}
