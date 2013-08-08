package util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import util.audio.Sound;

public class DBWord extends Word {
	private Sound sound;
	
	public DBWord(String narrator,String word) {
		super(narrator);
		this.sound = new Sound("audioFiles\\"+narrator+"\\Word\\"+word+".wav");
	}
	
	@Override
	public void sayWord(Sound sound) {
		InputStream inputStream = new ByteArrayInputStream(sound.getSamples());
		sound.play(inputStream);
	}
	
	@Override
	public Sound getSound() {
		return this.sound;
	}
}
