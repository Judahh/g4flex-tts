package util.audio;

import java.io.*;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.*;

public class Sound extends Thread {

	private AudioInputStream audioInputStream;
	private AudioFormat audioFormat;
	private byte[] samples;
	private Semaphore semaphore;
	private InputStream inputStream;
	private String path;
	// private Position curPosition;

	enum Position {
		LEFT, RIGHT, NORMAL
	};

	public AudioFormat getAudioFormat() {
		return audioFormat;
	}

	public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}

	public Sound(AudioInputStream audioInputStream) {
		this.semaphore = new Semaphore(0);
		// curPosition = Position.NORMAL;
		this.audioInputStream = audioInputStream;
		this.start();
	}

	public Sound(String path) {
		this.path=path;
		this.semaphore = new Semaphore(0);
		// curPosition = Position.NORMAL;
		try {
			this.audioInputStream = AudioSystem.getAudioInputStream(new File(
					path));
			this.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
			try {
				System.err.println("Não existe audio para:" + path);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		this.audioFormat = audioInputStream.getFormat();
//		System.out.println("Entrou:"+this.audioFormat);
		this.samples = getThisSamples(audioInputStream);
		this.semaphore.release();
	}

	public byte[] getSamples() {
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.semaphore.release();
		return samples;
	}

	private byte[] getThisSamples(AudioInputStream stream) {
		int length = (int) (stream.getFrameLength() * audioFormat
				.getFrameSize());
//		System.out.println(length);
		byte[] samples = new byte[length];
		DataInputStream dataInputStream = new DataInputStream(stream);
		try {
			// dataInputStream.read(samples);
			dataInputStream.readFully(samples);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return samples;
	}

	public byte[] getSamples(AudioInputStream stream) {
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length = (int) (stream.getFrameLength() * audioFormat
				.getFrameSize());
		byte[] samples = new byte[length];
		DataInputStream dataInputStream = new DataInputStream(stream);
		try {
			dataInputStream.readFully(samples);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.semaphore.release();
		return samples;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	// Funfa, mas tem pico
	public void play(InputStream inputStream) {
		this.inputStream = inputStream;
		try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 100 ms buffer for real time change to the sound stream
		int bufferSize = audioFormat.getFrameSize()
				* Math.round(audioFormat.getSampleRate() / 10);
		byte[] buffer = new byte[bufferSize];
		SourceDataLine sourceDataLine;
		try {
			DataLine.Info dataLineInfo = new DataLine.Info(
					SourceDataLine.class, audioFormat);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat, bufferSize);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		}
		sourceDataLine.start();
		try {
			int numBytesRead = 0;
			while (numBytesRead != -1) {
				numBytesRead = inputStream.read(buffer, 0, buffer.length);
				if (numBytesRead != -1)
					sourceDataLine.write(buffer, 0, numBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		sourceDataLine.drain();
		sourceDataLine.close();
		this.semaphore.release();
	}

	// Não funfa
	// public void play() {
	// try {
	// final DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,
	// audioInputStream.getFormat(),
	// ((int) audioInputStream.getFrameLength() * audioFormat
	// .getFrameSize()));
	//
	// // Carrega o som para o dispositivo
	// final Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
	// clip.addLineListener(new LineListener() {
	//
	// // Evento do LineListener
	// public void update(final LineEvent e) {
	// if (e.getType() == LineEvent.Type.STOP) {
	// e.getLine().close();
	// }
	// }
	// });
	// clip.open(audioInputStream);
	// } catch (final Exception e) {
	// e.printStackTrace();
	// }
	// }

	// Não funfa
	// public void play() {
	// AudioFormat format = audioInputStream.getFormat();
	// SourceDataLine auline = null;
	// DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	//
	// try {
	// auline = (SourceDataLine) AudioSystem.getLine(info);
	// auline.open(format);
	// } catch (LineUnavailableException e) {
	// e.printStackTrace();
	// return;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return;
	// }
	//
	// if (auline.isControlSupported(FloatControl.Type.PAN)) {
	// FloatControl pan = (FloatControl) auline
	// .getControl(FloatControl.Type.PAN);
	// if (curPosition == Position.RIGHT)
	// pan.setValue(1.0f);
	// else if (curPosition == Position.LEFT)
	// pan.setValue(-1.0f);
	// }
	//
	// auline.start();
	// int nBytesRead = 0;
	// byte[] abData = new byte[524288];// 128Kb
	//
	// try {
	// while (nBytesRead != -1) {
	// nBytesRead = audioInputStream.read(abData, 0, abData.length);
	// if (nBytesRead >= 0)
	// auline.write(abData, 0, nBytesRead);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// return;
	// } finally {
	// auline.drain();
	// auline.close();
	// }
	//
	// }
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}