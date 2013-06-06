package org.genshin.scrollninja.collision.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=FixtureGenerator.class, name="Default"),
	@JsonSubTypes.Type(value=BodyEditorFixtureGenerator.class, name="BodyEditor"),
})
public abstract class AbstractFixtureGenerator
{
	/**
	 * Fixtureを生成する
	 * @param owner		Fixtureの生成先Body
	 * @return			生成したFixture
	 */
	public Fixture generate(Body owner)
	{
		//---- Filterを生成する
		if(filter != null)
		{
			filter.generate(fixtureDef.filter);
			filter = null;
		}
		
		//---- まだFixtureはできていない
		return null;
	}
	
	/**
	 * 拡縮率を設定する
	 * @param scale		拡縮率
	 */
	public abstract void setScale(float scale);

	
	/** Fixtureの名前 */
	public String name;
	
	/** Fixtureの定義 */
	public FixtureDef fixtureDef;
	
	/** FixtureのFilter生成装置 */
	public FilterGenerator filter;
}
