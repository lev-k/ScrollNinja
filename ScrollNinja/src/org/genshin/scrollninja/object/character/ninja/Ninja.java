package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.object.character.ninja.controller.DefaultNinjaController;
import org.genshin.scrollninja.object.gui.Cursor;
import org.genshin.scrollninja.render.RenderObject;

import com.badlogic.gdx.graphics.Color;
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
	public Ninja(World world, Vector2 position, Cursor cursor)
	{
		super(world, position);
		
		//---- コントローラを設定する。
		setController(new DefaultNinjaController(this, cursor));
		
		//---- night mode
		for(RenderObject ro : getRenderObjects())
		{
			ro.setColor(Color.BLACK);
		}
	}
}
