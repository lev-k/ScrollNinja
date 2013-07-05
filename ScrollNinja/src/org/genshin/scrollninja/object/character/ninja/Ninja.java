package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.character.ninja.controller.DefaultNinjaController;
import org.genshin.scrollninja.object.gui.Cursor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 忍者
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Ninja extends AbstractNinja
{
	public Ninja(World world, Vector2 position, Cursor cursor, boolean isNight)
	{
		super(world, position, isNight);
		
		//---- コントローラを設定する。
		setController(new DefaultNinjaController(this, cursor));
	}
}
