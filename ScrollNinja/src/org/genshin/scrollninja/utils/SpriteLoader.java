package org.genshin.scrollninja.utils;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.ScrollNinja;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * スプライトのパラメータを読み込み、生成する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class SpriteLoader
{
	/**
	 * スプライトのパラメータをXMLから読み込むコンストラクタ
	 * @param xmlElement	XMLエレメントオブジェクト
	 */
	public SpriteLoader(Element xmlElement)
	{
		assert xmlElement != null;
		
		xmlElement		= xmlElement.getChildByName("Sprite");
		textureLoader	= new TextureLoader(xmlElement.getChildByName("Texture"));
		width			= xmlElement.getFloat("Width", 0.0f);
		height			= xmlElement.getFloat("Height", 0.0f);
		originX			= xmlElement.getFloat("OriginX", 0.0f);
		originY			= xmlElement.getFloat("OriginY", 0.0f);
	}
	
	/**
	 * パラメータを元にスプライトを生成する。
	 * @return		スプライト
	 */
	public Sprite create()
	{
		Sprite sprite = new Sprite(textureLoader.create());

		float w = width == 0.0f ? sprite.getWidth() : width;
		float h = height == 0.0f ? sprite.getHeight() : height;
		
		sprite.setSize(w, h);
		sprite.setOrigin(sprite.getWidth()*0.5f+originX, sprite.getHeight()*0.5f+originY);
		sprite.setScale(ScrollNinja.scale);
		
		return sprite;
	}
	
	/** テクスチャローダ */
	private final TextureLoader textureLoader;
	
	/** スプライトの横幅 */
	private final float width;
	
	/** スプライトの縦幅 */
	private final float height;

	/** スプライトの中央となるX座標（0.0の時、ど真ん中） */
	private final float originX;

	/** スプライトの中央となるY座標（0.0の時、ど真ん中） */
	private final float originY;
	
	
	
	/**
	 * テクスチャのパラメータを読み込み、生成する。
	 */
	private class TextureLoader
	{
		/**
		 * テクスチャのパラメータをXMLから読み込むコンストラクタ
		 * @param xmlElement	XMLエレメントオブジェクト
		 */
		private TextureLoader(Element xmlElement)
		{
			assert xmlElement != null;
			
			filePath	= GlobalParam.INSTANCE.TEXTURE_DIRECTORY_PATH + xmlElement.get("FilePath");
			x			= xmlElement.getInt("X", 0);
			y			= xmlElement.getInt("Y", 0);
			width		= xmlElement.getInt("Width", 0);
			height		= xmlElement.getInt("Height", 0);
			
			assert filePath != null;
		}
		
		private TextureRegion create()
		{
			Texture texture = TextureFactory.getInstance().get(filePath);

			final int w = width == 0 ? texture.getWidth() : width; 
			final int h = height == 0 ? texture.getHeight() : height; 
			
			TextureRegion textureRegion = new TextureRegion(texture, x, y, w, h);
			return textureRegion;
		}

		/** テクスチャのファイルパス */
		private final String filePath;

		/** UVマップのX座標（ピクセル） */
		private final int x;
		
		/** UVマップのY座標（ピクセル） */
		private final int y;
		
		/** UVマップの横幅（ピクセル） */
		private final int width;
		
		/** UVマップの縦幅（ピクセル） */
		private final int height;
	}
}
