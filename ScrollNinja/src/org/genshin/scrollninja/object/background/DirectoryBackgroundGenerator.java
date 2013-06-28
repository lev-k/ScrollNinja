package org.genshin.scrollninja.object.background;

import org.genshin.scrollninja.render.sprite.SpriteDef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;


/**
 * ディレクトリから背景を生成する
 * @author kou
 * @since		1.0
 * @version		1.0
 */
public class DirectoryBackgroundGenerator extends AbstractBackgroundGenerator
{
	@Override
	public void generate(BackgroundLayer targetLayer)
	{
		final BackgroundGenerator generator = new BackgroundGenerator();
		generator.spriteDef = new SpriteDef();
		generator.position = new Vector2();
		generator.spriteDef.size = new Vector2(scale, scale);
		generator.spriteDef.position = new Vector2(scale * 0.5f, scale * 0.5f);
		
		final FileHandle dir = Gdx.files.local(directoryPath);
		for(FileHandle file : dir.list(".png"))
		{
			//---- オフセット値を算出する
			final String[] position = file.nameWithoutExtension().split("_");
			final float center = 500.0f;
			generator.position.x = (Float.parseFloat(position[1]) - center) * scale;
			generator.position.y = (center - 1.0f - Float.parseFloat(position[0])) * scale;
			
			//---- 背景オブジェクトを生成する
			generator.spriteDef.textureFilePath = file.path();
			generator.generate(targetLayer);
		}
	}
	
	@Override
	public void setWorldScale(float worldScale)
	{
		super.setWorldScale(worldScale);
		
		scale *= worldScale;
	}
	
	
	/** 背景画像を格納しているディレクトリのパス */
	public String directoryPath;
	
	/** 背景画像1枚の大きさ */
	public float scale;
}
