package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * 線分の衝突判定オブジェクトを生成する
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class EdgeShapeGenerator implements ShapeGeneratorInterface
{
	@Override
	public Shape generate()
	{
		final EdgeShape shape = new EdgeShape();
		shape.set(offset, 0.0f, offset + length, 0.0f);
		return shape;
	}
	
	@Override
	public void setScale(float scale)
	{
		length *= scale;
		offset *= length;
	}


	/** 衝突判定の長さ */
	public float length;
	
	/** 衝突判定の中心座標のオフセット値 */
	public float offset;
}
