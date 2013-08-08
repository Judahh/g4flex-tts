package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;

import util.audio.Sound;
import util.audio.WaveAppender;

import util.Number;

public class Phrase {
	private String narrator;
	private String phrase;
	private ArrayList<Word> word; // Lista de palavras
	private String pattern = "[^\\p{Alpha}\\p{Digit}çÇáéíóúÁÉÍÓÚàèìòùÀÈÌÒÙãẽĩõũÃẼĨÕŨâêîôûÂÊÎÔÛ]";// tudo
	// menos
	// Alfabeto
	// português
	// do
	// Brasil
	// +
	// dígitos
//	private String pattern = "[^\\p{Alpha}\\p{Digit}çÇáéíóúÁÉÍÓÚàèìòùÀÈÌÒÙãẽĩõũÃẼĨÕŨâêîôûÂÊÎÔÛ\\?]";// tudo
																									// menos
																									// Alfabeto
																									// português
																									// do
																									// Brasil
																									// +
																									// dígitos
	private long time;

	public Phrase() {
		word = new ArrayList<>();
	}
	
	// Construtor que recebe frase e o tempo
		public Phrase(String phrase, long time) {
			this.word = new ArrayList<>();
			this.time = time;
			this.narrator="Felipe";
			splitPhrase(this.narrator,phrase, this.time);
		}

		// Construtor que recebe frase e uso o tempo padrão
		public Phrase(String phrase) {
			this.word = new ArrayList<>();
			this.time = 100;
			this.narrator="Felipe";
			splitPhrase(this.narrator,phrase, this.time);
		}
	
	// Construtor que recebe frase e o tempo
	public Phrase(String narrator,String phrase, long time) {
		this.word = new ArrayList<>();
		this.time = time;
		this.narrator=narrator;
		splitPhrase(this.narrator,phrase, this.time);
	}

	// Construtor que recebe frase e uso o tempo padrão
	public Phrase(String narrator,String phrase) {
		this.word = new ArrayList<>();
		this.time = 250;
		this.narrator=narrator;
		splitPhrase(this.narrator,phrase, this.time);
	}

	private String checkWord(String word){
		for (int index = 0; index < word.length(); index++) {
			String number="";
			int init=-1;
			boolean out=true;
			boolean in=true;
			while((word.charAt(index)>47) && (word.charAt(index)<58) && in){
				if(out){
					init=index;
					out=false;
				}
				number+=word.charAt(index);
				if(index<word.length()-1){
					index=index+1;
				}else{
					in=false;
				}
			}
			if(init>-1){
				String stringAux=word.substring(0, init);
				String numberAux=new Number(number).StringNumber().trim();
				stringAux+=numberAux;
				if(index<word.length()-1){
					stringAux+=word.substring(index, word.length());
				}
				word=stringAux;
				index=init+numberAux.length();
			}
		}
		return word;
	}
	
	public void sayPhrase(Sound sound) {
		InputStream inputStream = new ByteArrayInputStream(sound.getSamples());
		sound.play(inputStream);
	}
	
	private boolean existsWord(String word){
		File file = new File("audioFiles\\"+this.narrator+"\\Word\\"+word+".wav");  
        return file.exists();
	}

	public void addBlankTime(long time) {
		System.out.println("branco");
		try {
			word.add(new BlankWord(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Sound getSound() {
		WaveAppender waveAppender = new WaveAppender();
		String[] path = new String[this.word.size()];
		for (int index = 0; index < this.word.size(); index++) {
			path[index] = this.word.get(index).getSound().getPath();
		}
		AudioInputStream audioInputStreamTemp = waveAppender.append(path,
				"audioFiles\\"+this.narrator+"\\phrase.wav");
		Sound sound = new Sound("audioFiles\\"+this.narrator+"\\phrase.wav");
		return sound;
	}
	
	// Dividi a frase em várias palavras
	public void splitPhrase(String narrator,String phrase, long time) {
		this.phrase = phrase;
		Pattern splitter = Pattern.compile(pattern);// usa como corte tudo que
													// não pertence ao Alfabeto
													// português do Brasil +
													// dígitos
		String[] result = splitter.split(phrase.toLowerCase());// tudo minusculo
		
		

		for (String newWord : result) {
			System.out.println(newWord);
			if (!newWord.isEmpty()) {// se a palavra não for vazia entra no IF
				if(existsWord(newWord)){
					System.out.println("Old");
					word.add(new DBWord(narrator,newWord));
				}else{
					String[] checkedWord=checkWord(newWord).split(" ");
					for (String newCheckedWord : checkedWord) {
//						System.out.println("Checked:"+newCheckedWord);
						if(existsWord(newCheckedWord)){
							System.out.println("Old");
							word.add(new DBWord(narrator,newCheckedWord));
						}else{
							word.add(new Word(narrator,newCheckedWord)); // adiciona nova palavra na lista de palavras
						}
					}
				}
				addBlankTime(time);
			}else{
				addBlankTime(time);
			}
		}
		
//		for (int index=0;index<result.length;index++) {
//			System.out.println(result[index]);
//			if(index<result.length-1){
//				while(result[index+1].contains("?")){
//					if(result[index+1].charAt(0)=='?'){
//						result[index].concat("?");
//						result[index+1]=result[index+1].substring(1);
//					}
//				}
//			}
//			if (!result[index].isEmpty()) {// se a palavra não for vazia entra no IF
//				if(existsWord(result[index])){
//					System.out.println("Old");
//					word.add(new DBWord(narrator,result[index]));
//				}else{
//					String[] checkedWord=checkWord(result[index]).split(" ");
//					for (String newCheckedWord : checkedWord) {
////						System.out.println("Checked:"+newCheckedWord);
//						if(existsWord(newCheckedWord)){
//							System.out.println("Old");
//							word.add(new DBWord(narrator,newCheckedWord));
//						}else{
//							word.add(new Word(narrator,newCheckedWord)); // adiciona nova palavra na lista de palavras
//						}
//					}
//				}
//				addBlankTime(time);
//			}else{
//				addBlankTime(time);
//			}
//		}
		
		
		sayPhrase(getSound());
		//TODO: Nao esta deletando... tem que deletar!
//		for (int index = 0; index < this.word.size(); index++) {
////			System.out.println(this.word.get(index).getSound().getPath());
//			if(this.word.get(index).getSyllable().size()==0){
//				File file = new File(this.word.get(index).getSound().getPath());
//                while(!file.delete()){  
//                	System.gc();  
//                	System.out.println("Delete:"+this.word.get(index).getSound().getPath()+":"+file.delete());
//    				file.deleteOnExit();
//                    try {  
//                        Thread.sleep(10);  
//                    } catch (InterruptedException e) {  
//                        // TODO Auto-generated catch block  
//                        e.printStackTrace();  
//                    }  
//                } 
//			}
//		}
	}

	public void sayPhrase() {
		for (int index = 0; index < word.size(); index++) {
			word.get(index).sayWord();
			try {
				Thread.sleep(time);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getPhrase() {
		return phrase;
	}

	public ArrayList<Word> getWord() {
		return word;
	}
}
