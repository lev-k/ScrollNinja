package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Fixture生成してやんよ
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class FixtureGenerator extends AbstractFixtureGenerator
{
	@Override
	public Fixture generate(Body owner)
	{
		super.generate(owner);
		
		//---- 形状を生成する
		if(shape != null)
		{
			fixtureDef.shape = shape.generate();
			shape = null;
		}
		
		//---- Fixtureを生成する
		return owner.createFixture(fixtureDef);
	}
	
	
	@Override
	public void setScale(float scale)
	{
		shape.setScale(scale);
	}


	/** Fixtureの形状生成装置 */
	public ShapeGeneratorInterface shape;
}
