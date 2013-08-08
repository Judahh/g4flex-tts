package util.audio;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class SequenceAudioInputStream extends AudioInputStream {
	private static final boolean DEBUG = true;

	private List<AudioInputStream> m_audioInputStreamList;
	private int m_nCurrentStream;

	public SequenceAudioInputStream(AudioFormat audioFormat,
			Collection<AudioInputStream> audioInputStreams) {
		super(new ByteArrayInputStream(new byte[0]), audioFormat,
				AudioSystem.NOT_SPECIFIED);
		m_audioInputStreamList = new ArrayList<>(audioInputStreams);
		m_nCurrentStream = 0;
	}

	// TODO: remove
	private boolean addAudioInputStream(AudioInputStream audioStream) {
		if (DEBUG) {
			out("SequenceAudioInputStream.addAudioInputStream(): called.");
		}
		// Contract.check(audioStream != null);
		if (!getFormat().matches(audioStream.getFormat())) {
			if (DEBUG) {
				out("SequenceAudioInputStream.addAudioInputStream(): audio formats do not match, trying to convert.");
			}
			AudioInputStream asold = audioStream;
			audioStream = AudioSystem.getAudioInputStream(getFormat(), asold);
			if (audioStream == null) {
				out("###  SequenceAudioInputStream.addAudioInputStream(): could not convert.");
				return false;
			}
			if (DEBUG) {
				out(" converted");
			}
		}
		// Contract.check(audioStream != null);
		synchronized (m_audioInputStreamList) {
			m_audioInputStreamList.add(audioStream);
			m_audioInputStreamList.notifyAll();
		}
		if (DEBUG) {
			out("SequenceAudioInputStream.addAudioInputStream(): enqueued "
					+ audioStream);
		}
		return true;
	}

	private AudioInputStream getCurrentStream() {
		return (AudioInputStream) m_audioInputStreamList.get(m_nCurrentStream);
	}

	private boolean advanceStream() {
		m_nCurrentStream++;
		boolean bAnotherStreamAvailable = (m_nCurrentStream < m_audioInputStreamList
				.size());
		return bAnotherStreamAvailable;
	}

	public long getFrameLength() {
		long lLengthInFrames = 0;
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = (AudioInputStream) streamIterator.next();
			long lLength = stream.getFrameLength();
			if (lLength == AudioSystem.NOT_SPECIFIED) {
				return AudioSystem.NOT_SPECIFIED;
			} else {
				lLengthInFrames += lLength;
			}
		}
		return lLengthInFrames;
	}

	public int read() throws IOException {
		AudioInputStream stream = getCurrentStream();
		int nByte = stream.read();
		if (nByte == -1) {
			/*
			 * The end of the current stream has been signaled. We try to
			 * advance to the next stream.
			 */
			boolean bAnotherStreamAvailable = advanceStream();
			if (bAnotherStreamAvailable) {
				/*
				 * There is another stream. We recurse into this method to read
				 * from it.
				 */
				return read();
			} else {
				/*
				 * No more data. We signal EOF.
				 */
				return -1;
			}
		} else {
			/*
			 * The most common case: We return the byte.
			 */
			return nByte;
		}
	}

	public int read(byte[] abData, int nOffset, int nLength) throws IOException {
		AudioInputStream stream = getCurrentStream();
		int nBytesRead = stream.read(abData, nOffset, nLength);
		if (nBytesRead == -1) {
			/*
			 * The end of the current stream has been signaled. We try to
			 * advance to the next stream.
			 */
			boolean bAnotherStreamAvailable = advanceStream();
			if (bAnotherStreamAvailable) {
				/*
				 * There is another stream. We recurse into this method to read
				 * from it.
				 */
				return read(abData, nOffset, nLength);
			} else {
				/*
				 * No more data. We signal EOF.
				 */
				return -1;
			}
		} else {
			/*
			 * The most common case: We return the length.
			 */
			return nBytesRead;
		}
	}

	public long skip(long lLength) throws IOException {
		throw new IOException(
				"skip() is not implemented in class SequenceInputStream. Mail if you need this feature.");
	}

	public int available() throws IOException {
		return getCurrentStream().available();
	}

	public void close() throws IOException {
		// TODO: should we close all streams in the list?
	}

	public void mark(int nReadLimit) {
		throw new RuntimeException(
				"mark() is not implemented in class SequenceInputStream. Mail if you need this feature.");
	}

	public void reset() throws IOException {
		throw new IOException(
				"reset() is not implemented in class SequenceInputStream. Mail if you need this feature.");
	}

	public boolean markSupported() {
		return false;
	}

	private static void out(String strMessage) {
		System.out.println(strMessage);
	}
}

/*** SequenceAudioInputStream.java ***/
