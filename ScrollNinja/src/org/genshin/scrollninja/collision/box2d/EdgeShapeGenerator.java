package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.math.Vector2;
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
		shape.set(offset.x, offset.y, offset.x + direction.x, offset.y + direction.y);
		return shape;
	}
	
	@Override
	public void setScale(float scale)
	{
		direction.mul(scale);
		offset.mul(scale);
	}


	/** 衝突判定の向き */
	public Vector2 direction;
	
	/** 衝突判定の中心座標のオフセット値 */
	public Vector2 offset;
}
