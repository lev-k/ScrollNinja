package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.physics.box2d.Shape;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 衝突判定の形状生成まっしーん
 * @author kou
 * @since		1.0
 * @version		1.0
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=CircleShapeGenerator.class, name="Circle"),
	@JsonSubTypes.Type(value=RectangleShapeGenerator.class, name="Rectangle"),
	@JsonSubTypes.Type(value=EdgeShapeGenerator.class, name="Edge"),
})
public interface ShapeGeneratorInterface
{
	/**
	 * 衝突判定の形状を生成する
	 * @return	衝突判定の形状
	 */
	public abstract Shape generate();
	
	/**
	 * 拡縮率を設定する
	 * @param scale		拡縮率
	 */
	public abstract void setScale(float scale);
}
