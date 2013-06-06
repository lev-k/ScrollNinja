package org.genshin.scrollninja.collision.box2d;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * BodyEditorの出力データからFixtureを生成しますよ
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class BodyEditorFixtureGenerator extends AbstractFixtureGenerator
{
	@Override
	public Fixture generate(Body owner)
	{
		super.generate(owner);
		
		Vector2.tmp3.set(owner.getPosition());
		owner.setTransform(Vector2.tmp3.x - 20.48f, Vector2.tmp3.y, 0.0f);
		
		//---- BodyEditorの出力データからFixtureを生成する
		final BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(filePath));
		lastImagePath = loader.getImagePath(name);
		loader.attachFixture(owner, name, fixtureDef, scale);

		owner.setTransform(Vector2.tmp3.x, Vector2.tmp3.y, 0.0f);
		
		//---- Fixtureは複数作られる可能性があるため、nullを返すことにする
		return null;
	}
	
	
	@Override
	public void setScale(float scale)
	{
		this.scale *= scale;
	}


	/** BodyEditorの出力データのファイルパス */
	public String filePath;
	
	/** Fixtureの大きさ */
	public float scale = 1.0f;
	
	/** 最後に生成したデータに使われていた画像のパス */
	public String lastImagePath;
}
