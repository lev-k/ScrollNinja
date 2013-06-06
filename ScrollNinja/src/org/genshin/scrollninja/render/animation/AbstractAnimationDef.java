package org.genshin.scrollninja.render.animation;

import org.genshin.engine.utils.Point;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * アニメーション定義の基本クラス
 * @author kou
 * @since		1.0
 * @version		1.0
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=TextureAnimationDef.class, name="Texture"),
	@JsonSubTypes.Type(value=UVScrollAnimationDef.class, name="UVScroll"),
})
public abstract class AbstractAnimationDef
{
	/**
	 * アニメーションつくーる
	 * @return		作ったアニメーション
	 */
	abstract AnimationInterface generate();
	
	/** アニメーションの名前 */
	public String name;
	
	/** テクスチャのパス */
	public String textureFilePath;
	
	/** UVマップの大きさ */
	public Point uvSize;
	
	/** ループフラグ */
	public boolean looping;
}
