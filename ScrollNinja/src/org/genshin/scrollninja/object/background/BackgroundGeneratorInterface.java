package org.genshin.scrollninja.object.background;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 背景オブジェクトを生成するインタフェース
 * @author kou
 * @since		1.0
 * @version		1.0
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=BackgroundGenerator.class, name="Default"),
//	@JsonSubTypes.Type(value=FileBackgroundGenerator.class, name="FromFile"),
	@JsonSubTypes.Type(value=DirectoryBackgroundGenerator.class, name="FromDirectory"),
})
public interface BackgroundGeneratorInterface
{
	/**
	 * 背景オブジェクトを生成する
	 * @param targetLayer		生成先レイヤー
	 */
	public void generate(BackgroundLayer targetLayer);
	
	/**
	 * 世界の倍率を適用する。
	 * @param worldScale		世界の倍率
	 */
	public void setWorldScale(float worldScale);
}
