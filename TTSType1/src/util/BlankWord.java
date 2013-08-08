package util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;

import util.audio.Sound;
import util.audio.WaveAppender;

public class BlankWord extends Word{
	
	private long time;
	private Sound sound;
	
	public BlankWord(long time) {
		super();
		this.time=time;
		makeTime();
	}
	
	private void makeTime(){
		long time=this.time;
		int unit;
		int ten;
		int hundred;
		int thousand;
		int tenThousand;
		int hundredThousand;
		
		hundredThousand=(int)time/100000;
		time=time%100000;
		
		tenThousand=(int)time/10000;
		time=time%10000;
		
		thousand=(int)time/1000;
		time=time%1000;
		
		hundred=(int)time/100;
		time=time%100;
		
		ten=(int)time/10;
		time=time%10;
		
		unit=(int)time;
		
		WaveAppender waveAppender = new WaveAppender();
		String[] path = new String[unit+ten+hundred+thousand+tenThousand+hundredThousand];
		for (int index = 0; index < unit; index++) {
			path[index] = "audioFiles\\null\\1.wav";
		}
		for (int index = unit; index < unit+ten; index++) {
			path[index] = "audioFiles\\null\\10.wav";
		}
		for (int index = unit+ten; index < unit+ten+hundred; index++) {
			path[index] = "audioFiles\\null\\100.wav";
		}
		for (int index = unit+ten+hundred; index < unit+ten+hundred+thousand; index++) {
			path[index] = "audioFiles\\null\\1000.wav";
		}
		for (int index = unit+ten+hundred+thousand; index < unit+ten+hundred+thousand+tenThousand; index++) {
			path[index] = "audioFiles\\null\\10000.wav";
		}
		for (int index = unit+ten+hundred+thousand+tenThousand; index < unit+ten+hundred+thousand+tenThousand+hundredThousand; index++) {
			path[index] = "audioFiles\\null\\100000.wav";
		}
		AudioInputStream audioInputStreamTemp = waveAppender.append(path,"audioFiles\\null\\null.wav");
		this.sound = new Sound("audioFiles\\null\\null.wav");
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
	
	public long getTime() {
		return time;
	}
}
