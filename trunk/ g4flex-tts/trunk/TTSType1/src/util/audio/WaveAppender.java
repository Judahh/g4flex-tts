package util.audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;



public class WaveAppender {
	private AudioInputStream waveFile1;
	private AudioInputStream waveFile2;

	public WaveAppender() {
	}

	public WaveAppender(String waveFile1, String waveFile2) {
		try {
			this.waveFile1 = AudioSystem
					.getAudioInputStream(new File(waveFile1));
			this.waveFile2 = AudioSystem
					.getAudioInputStream(new File(waveFile1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WaveAppender(AudioInputStream waveFile1, AudioInputStream waveFile2) {
		this.waveFile1 = waveFile1;
		this.waveFile2 = waveFile2;
	}

	public AudioInputStream append() {
		List<AudioInputStream> audioInputStreamList = new ArrayList<>();
		try {
			audioInputStreamList.add(waveFile1);
			audioInputStreamList.add(waveFile2);
			AudioInputStream audioInputStream = new SequenceAudioInputStream(this.waveFile1.getFormat(),audioInputStreamList);
			this.waveFile1.close();
			this.waveFile2.close();
			for (int index = 0; index < audioInputStreamList.size(); index++) {
				audioInputStreamList.get(index).close();
			}
			audioInputStream.close();
			return audioInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public AudioInputStream append(String waveFile1, String waveFile2) {
		try {
			this.waveFile1 = AudioSystem
					.getAudioInputStream(new File(waveFile1));
			this.waveFile2 = AudioSystem
					.getAudioInputStream(new File(waveFile1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			List<AudioInputStream> audioInputStreamList = new ArrayList<>();
			audioInputStreamList.add(this.waveFile1);
			audioInputStreamList.add(this.waveFile2);
			AudioInputStream audioInputStream = new SequenceAudioInputStream(this.waveFile1.getFormat(),audioInputStreamList);
			this.waveFile1.close();
			this.waveFile2.close();
			for (int index = 0; index < audioInputStreamList.size(); index++) {
				audioInputStreamList.get(index).close();
			}
			audioInputStream.close();
			return audioInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AudioInputStream append(List<AudioInputStream>  audioInputStreamList) {
		try {
			AudioInputStream audioInputStream = new SequenceAudioInputStream(audioInputStreamList.get(0).getFormat(),audioInputStreamList);
			for (int index = 0; index < audioInputStreamList.size(); index++) {
				audioInputStreamList.get(index).close();
			}
			audioInputStream.close();
			return audioInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AudioInputStream append(String[] waveFilePath,String path) {
		List<AudioInputStream> audioInputStreamList = new ArrayList<>();
		try {
			for (int index = 0; index < waveFilePath.length; index++) {
				audioInputStreamList.add(AudioSystem.getAudioInputStream(new File(waveFilePath[index])));
			}
			AudioFormat audioFormat = AudioSystem.getAudioInputStream(new File(waveFilePath[0])).getFormat();
			AudioInputStream audioInputStream = new SequenceAudioInputStream(audioFormat,audioInputStreamList);
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,new File(path));
			for (int index = 0; index < audioInputStreamList.size(); index++) {
				audioInputStreamList.get(index).close();
			}
			audioInputStream.close();
			return audioInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AudioInputStream append(AudioInputStream[] waveFile) {
		List<AudioInputStream> audioInputStreamList = new ArrayList<>();
		
		for (int index = 0; index < waveFile.length; index++) {
			audioInputStreamList.add(waveFile[index]);
		}
		
//		long length=0;
//		for (int index = 0; index < waveFile.length; index++) {
//			audioInputStreamList.add(waveFile[index]);
//			length+=waveFile[index].getFrameLength();
//		}
		
		try {
//			AudioInputStream audioInputStream = new AudioInputStream(new SequenceAudioInputStream(waveFile[0].getFormat(),audioInputStreamList),waveFile[0].getFormat(), length);
			
			AudioInputStream audioInputStream = new SequenceAudioInputStream(waveFile[1].getFormat(),audioInputStreamList);
			for (int index = 0; index < waveFile.length; index++) {
				waveFile[index].close();
			}
			for (int index = 0; index < audioInputStreamList.size(); index++) {
				audioInputStreamList.get(index).close();
			}
			audioInputStream.close();
			return audioInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AudioInputStream append(AudioInputStream waveFile1,
			AudioInputStream waveFile2) {
		this.waveFile1 = waveFile1;
		this.waveFile2 = waveFile2;
		List<AudioInputStream> audioInputStreamList = new ArrayList<>();
		try {
			audioInputStreamList.add(waveFile1);
			audioInputStreamList.add(waveFile2);
			AudioInputStream audioInputStream = new SequenceAudioInputStream(this.waveFile1.getFormat(),audioInputStreamList);
			this.waveFile1.close();
			this.waveFile2.close();
			for (int index = 0; index < audioInputStreamList.size(); index++) {
				audioInputStreamList.get(index).close();
			}
			audioInputStream.close();
			return audioInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void save(AudioInputStream audioInputStream,String path){
		try{
		AudioSystem.write(audioInputStream, 
                AudioFileFormat.Type.WAVE, 
                new File(path));
		audioInputStream.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
}
