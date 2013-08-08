package util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioInputStream;

import util.audio.Sound;
import util.audio.WaveAppender;

public class Word {
	private String narrator;
	private String word;
	private ArrayList<Syllable> syllable; // Lista de sílabas
	private static final char[] vowels = new char[]{'a', 'e', 'i', 'o', 'u',
			'y', 'á', 'é', 'í', 'ó', 'ú', 'ý', 'à', 'è', 'ì', 'ò', 'ù', 'y',// não
																			// tem
																			// `y
			'ã', 'ẽ', 'ĩ', 'õ', 'ũ', 'y',// não tem ~y
			'â', 'ê', 'î', 'ô', 'û', 'y'};// não tem ^y

	public Word() {
		this.narrator = "Felipe";
		syllable = new ArrayList<>();
	}
	
	public Word(String narrator) {
		this.narrator = narrator;
		syllable = new ArrayList<>();
	}

	// Construtor que recebe a palavra e cria a lista de sílabas
	public Word(String narrator,String word) {
		this.narrator = narrator;
		syllable = new ArrayList<>();
		splitWord(word);
	}

	public void sayWord(Sound sound) {
		InputStream inputStream = new ByteArrayInputStream(sound.getSamples());
		sound.play(inputStream);
	}

	public void sayWord() {
		for (int index = 0; index < syllable.size(); index++) {
			syllable.get(index).saySyllable();
		}
	}

	public Sound getSound() {
		WaveAppender waveAppender = new WaveAppender();
		String[] path = new String[this.syllable.size()];
		for (int index = 0; index < this.syllable.size(); index++) {
			path[index] = this.syllable.get(index).getSound().getPath();
		}
		AudioInputStream audioInputStreamTemp = waveAppender.append(path,
				"audioFiles\\"+this.narrator+"\\Word\\" + word + ".wav");
		Sound sound = new Sound("audioFiles\\"+this.narrator+"\\Word\\" + word + ".wav");
		return sound;
	}

	private void addSyllable(String newSyllable) {
		if (syllable.size() > 0) {
			Syllable oldLastSyllable = syllable.get(syllable.size() - 1);
			Syllable lastSyllable = new Syllable(this.narrator,oldLastSyllable, newSyllable);
			this.syllable.add(lastSyllable);
			oldLastSyllable.setNext(lastSyllable);
		} else {
			Syllable lastSyllable = new Syllable(this.narrator,newSyllable);
			this.syllable.add(lastSyllable);
		}
	}

	private void addSyllable(char newSyllable) {
		if (syllable.size() > 0) {
			Syllable oldLastSyllable = syllable.get(syllable.size() - 1);
			Syllable lastSyllable = new Syllable(this.narrator,oldLastSyllable, newSyllable);
			this.syllable.add(lastSyllable);
			oldLastSyllable.setNext(lastSyllable);
		} else {
			Syllable lastSyllable = new Syllable(this.narrator,newSyllable);
			this.syllable.add(lastSyllable);
		}
	}

	public void splitWord(String word) {
		System.out.println();
		this.word = word;
		for (int index = 0; index < word.length(); index++) {// supondo que se
																// pode ter ate
																// 5 letras por
																// silaba
			if (index < word.length() - 4) {// tamanho maximo=5, como Vrões,
											// nhões, quões
				index = check5Syllables(index);
			} else if (index < word.length() - 3) {// tamanho maximo=4 quão
				index = check4Syllables(index);
			} else if (index < word.length() - 2) {// tamanho maximo=3
				index = check3Syllables(index);
			} else if (index < word.length() - 1) {// tamanho maximo=2
				index = check2Syllables(index);
			} else if (index < word.length()) {// tamanho maximo=1
				index = check1Syllable(index);
			}
		}
		for (int index = 0; index < this.syllable.size(); index++) {
			if (index + 1 < this.syllable.size()) {
				this.syllable.get(index).setNext(this.syllable.get(index + 1));
			}
			if (index == this.syllable.size() - 1) {
				this.syllable.get(index).setEnd(true);
			}
		}
		if (this.syllable.size() == 1) {
			this.syllable.get(0).setMono(true);
		}
		for (int index = 0; index < this.syllable.size(); index++) {
			this.syllable.get(index).start();
		}
//		sayWord(getSound());
		// sayWord();
	}

	private int check5Syllables(int index) {
		if (is5Syllables(index)) {
			this.addSyllable("" + word.charAt(index) + word.charAt(index + 1)
					+ word.charAt(index + 2) + word.charAt(index + 3)
					+ word.charAt(index + 4));
			return index + 4;
		}

		// caso nao seja checar se eh uma de 4
		return check4Syllables(index);
	}

	private boolean is5Syllables(int index) {// 1 de 3 ou 4 silabas onde as
												// ultimas tres letas sao: õ,e,s
		if(index+6>this.word.length()){
			return (is4Syllables(index) && (isE(this.word.charAt(index+3)) || isO(this.word.charAt(index+3)) || isI(this.word.charAt(index+3)) || hasMOrN(index+3)) &&  hasMOrNFriendRule(index+4))
					|| ((hasCLNorSHVowelRule(index) || hasLOrRFriendLOrRVowelRule(index)) &&  hasAorOES(index+2))
					|| (hasGUAEOrORule(index) && (this.word.charAt(index+3) == 'i') && hasMOrNFriendRule(index+4))
					|| (hasGUAOrOEOrOSRule(index));// tem pa-ra-guais
		}else{
			if(!isVowel(this.word.charAt(index+5))){
				return (is4Syllables(index) && (isE(this.word.charAt(index+3)) || isO(this.word.charAt(index+3)) || isI(this.word.charAt(index+3)) || hasMOrN(index+3)) &&  hasMOrNFriendRule(index+4))
						|| ((hasCLNorSHVowelRule(index) || hasLOrRFriendLOrRVowelRule(index)) &&  hasAorOES(index+2))
						|| (hasGUAEOrORule(index) && (this.word.charAt(index+3) == 'i') && hasMOrNFriendRule(index+4))
						|| (hasGUAOrOEOrOSRule(index));// tem pa-ra-guais
			}
		}
		return false;
		
	}

	private int check4Syllables(int index) {
		if (is4Syllables(index)) {
			this.addSyllable("" + word.charAt(index) + word.charAt(index + 1)
					+ word.charAt(index + 2) + word.charAt(index + 3));
			return index + 3;
		}

		// caso nao seja checar se eh uma de 3
		return check3Syllables(index);
	}

	private boolean is4Syllables(int index) {// (g/q + u + a/e(acentuada ou nao)
												// + i/o/u(acentuada ou nao)) ou
												// (consoante + õ/ã + e + s) ou
												// (1 de 3 silabas que a quarta
												// letra seja um m ou n) ou
												// (b/c/d/f/g/k/p/s/t/v + r/l +
												// ã + o)
		if (index < word.length() - 4) {
			return ((!isVowel(this.word.charAt(index)) && is3SyllablesVowelUnknownConsonant(index+1))
					|| (is3SyllablesUnknownUnknownVowel(index) && isEndConsonant(index + 3) && !isVowel(word.charAt(index + 4))) 
					|| (hasLOrRFriendLOrRVowelRule(index) && hasAO(index + 2)));
		}
		return ((!isVowel(this.word.charAt(index)) && is3SyllablesVowelUnknownConsonant(index+1))
				|| (is3SyllablesUnknownUnknownVowel(index) && isEndConsonant(index + 3)) 
				|| (hasLOrRFriendLOrRVowelRule(index) && hasAO(index + 2)));
	}

	private int check3Syllables(int index) {
		if (is3Syllables(index)) {
			this.addSyllable("" + word.charAt(index) + word.charAt(index + 1)
					+ word.charAt(index + 2));
			return index + 2;
		}

		// caso nao seja checar se eh uma de 2
		return check2Syllables(index);
	}

	private boolean is3Syllables(int index) {// (consoante + ã + o) ou (n/l + h
												// + vogal) ou
												// (b/c/d/f/g/k/p/s/t/v + r/l +
												// vogal) ou (consoante + vogal
												// + r/s/m/n/l) ou (g/q + u +
												// a/e(acentuada ou nao)) ou
												// (vogal + consoante + vogal)
												// ou (õ + e + s (como
												// a-vi-ões))
		// TODO: Ver consoante + vogal + vogal (como te-les-co-(pio) , (cai)-xa
		// , (lei)-te , te-(sou)-ro , (me-i)-a , (sa-í)-da , (vo-o) ,
		// (co-o)r-de-nar , (sa-a)-ra , (ru-í)-do e (sa-i)-da), existem casos
		// que separa e outros que nao
		// estava pensando em colocar pra juntar sempre, e na hora de gravar eu
		// imagino que a "separacao" viria inconcientemente, mas se jah
		// separassemos aqui seria melhor...

		return (is3SyllablesVowelUnknownConsonant(index)
		|| is3SyllablesUnknownUnknownVowel(index)
		|| is3SyllablesRest(index));
	}
	
	private boolean is3SyllablesVowelUnknownConsonant(int index) {
		return ((hasMOrNplusFriendRule(index))//TODO: checar
		|| (hasAorOES(index)));
	}
	
	private boolean is3SyllablesUnknownUnknownVowel(int index) {
		return ((hasConsonantAORule(index)) || (hasCLNorSHVowelRule(index))
				|| (hasLOrRFriendLOrRVowelRule(index))
				|| (hasGUAEIOrORule(index))
		//      ||(hasConsonantVowelVowelSubrule(index)) //possivel bug
		);
	}
	
	private boolean is3SyllablesRest(int index) {
		return (hasConsonantVowelEndConsonantRule(index));
	}

	private int check2Syllables(int index) {
		if (is2Syllables(index)) {
			this.addSyllable("" + word.charAt(index) + word.charAt(index + 1));
			return index + 1;
		}

		// caso nao seja checar se eh uma de 1
		return check1Syllable(index);
	}

	private boolean is2Syllables(int index) {// (consoante + vogal) ou (vogal +
												// consoante + consoante) ou
												// (vogal + m/s) ou (ã + o)
		if (index < word.length() - 2) {
			return (((!isVowel(word.charAt(index))) && isVowel(word
					.charAt(index + 1)))
					|| (isVowel(word.charAt(index))
							&& isEndConsonant(index + 1) && !isVowel(word
								.charAt(index + 2))) || hasAO(index));
		} else if (index < word.length() - 1) {
			return ((!isVowel(word.charAt(index))) && isVowel(word.charAt(index + 1))
					|| (isVowel(word.charAt(index)) && (isEndConsonant(index + 1))) 
					|| hasAO(index));
		}
		return false;
	}

	private int check1Syllable(int index) {// sempre eh
		this.addSyllable(word.charAt(index));
		return index;
	}
	
	// Verifica se é ch,lh,nh...etc + uma vogal
	private boolean hasCLNorSHVowelRule(int index) {// tem também gh e kh de
													// Ghandi e Genghis Khan
		return ((this.word.charAt(index) == 'c'
				|| this.word.charAt(index) == 'ç'
				|| this.word.charAt(index) == 'g'
				|| this.word.charAt(index) == 'l'
				|| this.word.charAt(index) == 'n'
				|| this.word.charAt(index) == 's' || this.word.charAt(index) == 'k')
				&& (this.word.charAt(index + 1) == 'h') && isVowel(this.word
					.charAt(index + 2)));
	}

	// Verifica se a silaba é b,c,g,k,etc + r ou l + alguma vogal
	private boolean hasLOrRFriendLOrRVowelRule(int index) {
		return (isLOrRFriend(index)
				&& (this.word.charAt(index + 1) == 'r' || this.word
						.charAt(index + 1) == 'l') && isVowel(this.word
					.charAt(index + 2)));
	}

	// Olhar o método hasLOrRFriendLOrRVowelrule
	private boolean isLOrRFriend(int index) {
		return ((this.word.charAt(index) == 'b')
				|| (this.word.charAt(index) == 'c')
				|| (this.word.charAt(index) == 'd')
				|| (this.word.charAt(index) == 'f')
				|| (this.word.charAt(index) == 'g')
				|| (this.word.charAt(index) == 'k')
				|| (this.word.charAt(index) == 'p')
				|| (this.word.charAt(index) == 's')
				|| (this.word.charAt(index) == 't') || (this.word.charAt(index) == 'v'));
	}

	// Verifica se a primeira letra é consoante, se a segunda é vogal e se a
	// terceira
	private boolean hasConsonantVowelEndConsonantRule(int index) {// ver
																	// problema
																	// do sh!!!
		if (index < word.length() - 3) {
			return (  !isVowel(this.word.charAt(index))
					&& isVowel(this.word.charAt(index + 1))
					&& isEndConsonant(index + 2) 
					&& (!isVowel(this.word.charAt(index + 3)) && ( ((this.word.charAt(index + 2) == 's' || this.word.charAt(index + 2) != 'n' || this.word.charAt(index + 2) != 'l') && this.word.charAt(index + 3) != 'h') 
					|| (this.word.charAt(index + 2) != 's' && this.word.charAt(index + 2) != 'n' && this.word.charAt(index + 2) != 'l'))));
		}
		return ((!isVowel(this.word.charAt(index)))
				&& isVowel(this.word.charAt(index + 1)) && isEndConsonant(index + 2));
	}

	// Verifica se o caracter
	private boolean isEndConsonant(int index) {
		return ((this.word.charAt(index) == 'l')
				|| (this.word.charAt(index) == 'm')
				|| (this.word.charAt(index) == 'n')
				|| (this.word.charAt(index) == 'r')
				|| (this.word.charAt(index) == 'h')// de Judah por exemplo
				|| (this.word.charAt(index) == 's')
				|| (this.word.charAt(index) == 'x') || (this.word.charAt(index) == 'z'));
	}

	// Verifica se não é vogal e se o proximo caracter é uma consoante com ã e o
	private boolean hasConsonantAORule(int index) {// (consoante + ã + o)
		return ((!isVowel(this.word.charAt(index))) && hasAO(index + 1));
	}

	// faz o trabalho para o metodo hasConsonantAORule
	private boolean hasAO(int index) {
		return ((this.word.charAt(index) == 'ã') && 
				((this.word.charAt(index + 1) == 'o')||(this.word.charAt(index + 1) == 'e')));
	}

	private boolean hasConsonantAorOESRule(int index) {
		return ((!isVowel(this.word.charAt(index))) && hasAorOES(index + 1));
	}

	// Verifica se é ães ou ões
	private boolean hasAorOES(int index) {
		return ((this.word.charAt(index) == 'ã') && (this.word.charAt(index + 1) == 'o') && hasMOrNFriendRule(index+2))
				||((this.word.charAt(index) == 'ã' || this.word.charAt(index) == 'õ') && (this.word.charAt(index + 1) == 'e') && hasMOrNFriendRule(index+2));
	}

	// Verifica se a primeira letra é "g", se a seg
	private boolean hasGUAEIOrORule(int index) {
		return ((this.word.charAt(index) == 'g' || this.word.charAt(index) == 'q')
				&& (this.word.charAt(index + 1) == 'u') && (isA(this.word
				.charAt(index + 2))
				|| isE(this.word.charAt(index + 2))
				|| isI(this.word.charAt(index + 2)) || isO(this.word
					.charAt(index + 2))));
	}
	
	// Verifica se a primeira letra é "g", se a seg
	private boolean hasGUAEOrORule(int index) {
		return ((this.word.charAt(index) == 'g' || this.word.charAt(index) == 'q')
				&& (this.word.charAt(index + 1) == 'u') && (isA(this.word
				.charAt(index + 2))
				|| isE(this.word.charAt(index + 2))
				|| isO(this.word.charAt(index + 2))));
	}
	
	private boolean hasGUAOrOEOrOSRule(int index) {
		return ((this.word.charAt(index) == 'g' || this.word.charAt(index) == 'q')
				&& (this.word.charAt(index + 1) == 'u') 
				&& ( hasAorOES(index+2) )
				);
	}

	private boolean hasWithIOrOOrU(int index, boolean withIOU) {
		if (withIOU) {
			return (isI(this.word.charAt(index))
					|| isO(this.word.charAt(index)) || isU(this.word
						.charAt(index)));
		}
		return true;
	}

	// Não é mais usado... acabou sendo substituido por isEndConsonant()
	// private boolean hasMOrNRule(int index){
	// if(index<word.length()-2){
	// return (hasMOrN(index) && !isVowel(this.word.charAt(index+1)));
	// }
	// return (hasMOrN(index));
	// }

	private boolean hasMOrNplusFriendRule(int index) {//TODO: checar
		if(this.word.length()>index+2){
			if(this.word.length()>index+3){
				if(!isVowel(this.word.charAt(index+3))){
					return(isVowel(this.word.charAt(index)) && hasMOrN(index+1) && hasMOrNFriendRule(index+2));
				}
			}else{
				return(isVowel(this.word.charAt(index)) && hasMOrN(index+1) && hasMOrNFriendRule(index+2));
			}
		}
		return false;
	}
	
	private boolean hasMOrNFriendRule(int index) {//TODO: checar
		//RSLXZ
		return ((this.word.charAt(index) == 'r')
				|| (this.word.charAt(index) == 's')
				|| (this.word.charAt(index) == 'l')
				|| (this.word.charAt(index) == 'x')
				|| (this.word.charAt(index) == 'z'));
	}
	
	private boolean hasMOrN(int index) {
		return (this.word.charAt(index) == 'm' || this.word.charAt(index) == 'n');
	}

	private boolean hasVowelConsonantVowelRule(int index) {
		return (isVowel(this.word.charAt(index))
				&& !isVowel(this.word.charAt(index + 1)) && isVowel(this.word
					.charAt(index + 2)));
	}

	private boolean hasConsonantVowelVowelSubrule(int index) {
		if (index < word.length() - 4) {
			return (!isVowel(this.word.charAt(index))
					&& isVowel(this.word.charAt(index + 1))
					&& isVowel(this.word.charAt(index + 2)) && !isEndConsonant(this.word
						.charAt(index + 3)));
		}
		return (!isVowel(this.word.charAt(index))
				&& isVowel(this.word.charAt(index + 1)) && isVowel(this.word
					.charAt(index + 2)));
	}

	// Verifica se o caracter é uma vogal armazenada no vetor "Vowels"
	private boolean isVowel(char a) {
		for (int i = 0; i < vowels.length; i++) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private boolean isA(char a) {
		for (int i = 0; i < vowels.length; i = i + 6) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private boolean isE(char a) {
		for (int i = 1; i < vowels.length; i = i + 6) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private boolean isI(char a) {
		for (int i = 2; i < vowels.length; i = i + 6) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private boolean isO(char a) {
		for (int i = 3; i < vowels.length; i = i + 6) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private boolean isU(char a) {
		for (int i = 4; i < vowels.length; i = i + 6) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	// Verifica se o caracter é igual a s
	private boolean isS(char a) {
		return (a == 's');
	}

	public String getWord() {
		return word;
	}

	public ArrayList<Syllable> getSyllable() {
		return syllable;
	}
	
	public String getNarrator() {
		return narrator;
	}
	
	public void setNarrator(String narrator) {
		this.narrator = narrator;
	}
}
