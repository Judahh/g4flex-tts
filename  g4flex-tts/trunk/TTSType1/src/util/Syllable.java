package util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import util.audio.Sound;

public class Syllable {
	private String narrator;
	private Syllable next;
	private Syllable previous;
	private String syllable;
	private String speakableSyllable;
	private Sound sound;
	private boolean end;
	private boolean mono;

	public enum SpeakableType {// ordem básica:´,^,~,X
		DEFAULT, TYPE1, TYPE2, TYPE3, TYPE4
	}

	public Syllable(String narrator, Syllable previous, String syllable) {
		this.previous = previous;
		this.next = null;
		this.syllable = syllable;
		this.end = false;
		this.mono = false;
		this.narrator = narrator;
		this.speakableSyllable = this.syllable;
	}

	public Syllable(String narrator, Syllable previous, char syllable) {
		this.previous = previous;
		this.next = null;
		this.syllable = "" + syllable;
		this.end = false;
		this.mono = false;
		this.narrator = narrator;
		this.speakableSyllable = this.syllable;
	}

	public Syllable(String narrator, String syllable) {
		this.previous = null;
		this.next = null;
		this.syllable = syllable;
		this.end = false;
		this.mono = false;
		this.narrator = narrator;
		this.speakableSyllable = this.syllable;
	}

	public Syllable(String narrator, char syllable) {
		this.previous = null;
		this.next = null;
		this.syllable = "" + syllable;
		this.end = false;
		this.mono = false;
		this.narrator = narrator;
		this.speakableSyllable = this.syllable;
	}

	public void start() {
		makeSpeakableSyllable();
		saySpeakableSyllable();
	}

	private void saySpeakableSyllable() {
		System.out.print(this.speakableSyllable + "-");
		String tempBefore="";
		String tempAfter="";
		if(this.previous!=null){
			tempBefore="!";
		}
		if(this.next!=null){
			tempAfter="!";
		}
		this.sound = new Sound("audioFiles\\" + this.narrator + "\\Syllable\\"
				+ tempBefore + this.speakableSyllable + tempAfter + ".wav");
		// saySyllable();
	}

	public boolean isMono() {
		return mono;
	}

	public void setMono(boolean mono) {
		this.mono = mono;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public boolean isEnd() {
		return end;
	}

	public Syllable getPrevious() {
		return this.previous;
	}

	public void setPrevious(Syllable previous) {
		this.previous = previous;
	}

	public Syllable getNext() {// TODO: Tem que ter cuidado ao usar isso!!!!
		return next;
	}

	public void setNext(Syllable next) {
		this.next = next;
	}

	public void saySyllable() {
		InputStream inputStream = new ByteArrayInputStream(sound.getSamples());
		this.sound.play(inputStream);
	}

	private boolean isNotPronouncedVowel(char a) {
		char[] vowels = new char[]{'a', 'e', 'i', 'o', 'u'};
		for (int i = 0; i < vowels.length; i++) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private void addMOrNRuleForAorO(int index) {
		String temp = this.speakableSyllable;
		if (this.speakableSyllable.charAt(index) == 'a'
				|| this.speakableSyllable.charAt(index) == 'o') {
			this.speakableSyllable = temp.substring(0, index);
			this.speakableSyllable += getSpeakableVowel(temp.charAt(index),
					SpeakableType.TYPE2);
			if(temp.length()>index + 2){
				this.speakableSyllable += temp.substring(index + 2, temp.length());
			}
		}
	}

	private void addMOrNRule(int index) {
		String temp = this.speakableSyllable;
		addMOrNRuleForAorO(index);
		if (this.speakableSyllable.charAt(index) == 'e') {
			this.speakableSyllable = temp.substring(0, index);
			this.speakableSyllable += "em";
			this.speakableSyllable += temp.substring(index + 1, temp.length());
		}
		if (this.speakableSyllable.charAt(index) == 'i'
				|| this.speakableSyllable.charAt(index) == 'y') {
			this.speakableSyllable = temp.substring(0, index);
			this.speakableSyllable += "im";
			this.speakableSyllable += temp.substring(index + 1, temp.length());
		}
		if (this.speakableSyllable.charAt(index) == 'u') {
			this.speakableSyllable = temp.substring(0, index);
			this.speakableSyllable += "um";
			this.speakableSyllable += temp.substring(index + 1, temp.length());
		}
	}

	private void setMOrNRule() {
		for (int index = 0; index < this.speakableSyllable.length(); index++) {
			if (isNotPronouncedVowel(this.speakableSyllable.charAt(index))) {
				if (index + 1 < this.speakableSyllable.length()) {
					if (this.speakableSyllable.charAt(index + 1) == 'm'
							|| this.speakableSyllable.charAt(index + 1) == 'n') {
						addMOrNRuleForAorO(index);
					}
				} else {
					if (this.next != null) {
						if (this.next.getSyllable().charAt(0) == 'm'
								|| this.next.getSyllable().charAt(0) == 'n') {
							addMOrNRule(index);
						}
					}
				}
			}
		}

	}

	private char getSpeakableVowel(char vowel) {
		switch (vowel) {
			case 'a' :

				if (!this.end || this.mono) {
					return getSpeakableA(SpeakableType.DEFAULT);
				} else {
					return getSpeakableA(SpeakableType.TYPE1);
				}

			case 'e' :

				if (!this.end) {
					return getSpeakableE(SpeakableType.DEFAULT);
				} else {
					return getSpeakableE(SpeakableType.TYPE2);
				}

			case 'o' :

				if (!this.end) {
					return getSpeakableO(SpeakableType.DEFAULT);
				} else {
					return getSpeakableO(SpeakableType.TYPE3);
				}
		}
		return vowel;
	}

	private char getSpeakableVowel(char vowel, SpeakableType type) {
		switch (vowel) {
			case 'a' :
				return getSpeakableA(type);

			case 'e' :

				return getSpeakableE(type);

			case 'o' :

				return getSpeakableO(type);
		}
		return vowel;
	}

	private char getSpeakableA(SpeakableType type) {
		switch (type) {
			case DEFAULT :

				return 'á';

			case TYPE1 :

				return 'â';

			case TYPE2 :

				return 'ã';

			default :

				return 'á';
		}
	}

	private char getSpeakableE(SpeakableType type) {
		switch (type) {
			case DEFAULT :

				return 'ê';

			case TYPE1 :

				return 'é';

			case TYPE2 :

				return 'i';

			default :

				return 'ê';
		}
	}

	private char getSpeakableO(SpeakableType type) {
		switch (type) {
			case DEFAULT :

				return 'ô';

			case TYPE1 :

				return 'ó';

			case TYPE2 :

				return 'õ';

			case TYPE3 :

				return 'u';

			default :

				return 'ô';
		}
	}

	private boolean isE(char a) {
		char[] vowels = new char[]{'a', 'e', 'i', 'o', 'u', 'y', 'á', 'é', 'í',
				'ó', 'ú', 'ý', 'à', 'è', 'ì', 'ò', 'ù', 'y',// não tem `y
				'ã', 'ẽ', 'ĩ', 'õ', 'ũ', 'y',// não tem ~y
				'â', 'ê', 'î', 'ô', 'û', 'y'};// não tem ^y

		for (int i = 1; i < vowels.length; i = i + 6) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private boolean isVowel(char a) {// TODO:
		char[] vowels = new char[]{'a', 'e', 'i', 'o', 'u', 'y', 'á', 'é', 'í',
				'ó', 'ú', 'ý', 'à', 'è', 'ì', 'ò', 'ù', 'y',// não tem `y
				'ã', 'ẽ', 'ĩ', 'õ', 'ũ', 'y',// não tem ~y
				'â', 'ê', 'î', 'ô', 'û', 'y'};// não tem ^y
		for (int i = 0; i < vowels.length; i++) {
			if (a == vowels[i]) {
				return true;
			}
		}

		return false;
	}

	private void addBetweenVowelsRule(int index) {// TODO:
		String temp = this.speakableSyllable;
		this.speakableSyllable = temp.substring(0, index);
		this.speakableSyllable += getSpeakableS(SpeakableType.TYPE1);
		this.speakableSyllable += temp.substring(index + 1, temp.length());
	}

	private void addNotBetweenVowelsRule(int index) {// TODO:
		String temp = this.speakableSyllable;
		this.speakableSyllable = temp.substring(0, index);
		this.speakableSyllable += getSpeakableS(SpeakableType.DEFAULT);
		this.speakableSyllable += temp.substring(index + 1, temp.length());
	}

	private void setBetweenVowelsRule() {// TODO:
		for (int index = 0; index < this.speakableSyllable.length(); index++) {
			if (this.speakableSyllable.charAt(index) == 's') {
				if (index == speakableSyllable.length() - 1) {
					return;
				}
				if (index > 0) {
					if (isVowel(this.speakableSyllable.charAt(index - 1))) {
						if (index + 1 < this.speakableSyllable.length()) {
							if (isVowel(this.speakableSyllable
									.charAt(index + 1))) {
								addBetweenVowelsRule(index);
							}
						} else if (this.next != null) {
							if (isVowel(this.next.getSyllable().charAt(0))) {
								addBetweenVowelsRule(index);
							}
						}
					}
				} else if (this.previous != null) {
					if (isVowel(this.previous.getSyllable().charAt(
							this.previous.getSyllable().length() - 1))) {
						if (index + 1 < this.speakableSyllable.length()) {
							if (isVowel(this.speakableSyllable
									.charAt(index + 1))) {
								addBetweenVowelsRule(index);
							}
						} else if (this.next != null) {
							if (isVowel(this.next.getSyllable().charAt(0))) {
								addBetweenVowelsRule(index);
							}
						} else {
							addNotBetweenVowelsRule(index);
						}
					} else {
						addNotBetweenVowelsRule(index);
					}
				} else {
					addNotBetweenVowelsRule(index);
				}
			}
		}
	}

	private void addExRule(int index) {// TODO:
		String temp = this.speakableSyllable;
		this.speakableSyllable = temp.substring(0, index);
		this.speakableSyllable += getSpeakableX(SpeakableType.TYPE2);
		this.speakableSyllable += temp.substring(index + 1, temp.length());
	}

	private void addNotExRule(int index) {// TODO:
		String temp = this.speakableSyllable;
		this.speakableSyllable = temp.substring(0, index);
		this.speakableSyllable += getSpeakableX(SpeakableType.DEFAULT);
		this.speakableSyllable += temp.substring(index + 1, temp.length());
	}

	private void setExRule() {// TODO:
		for (int index = 0; index < this.speakableSyllable.length(); index++) {
			if (this.speakableSyllable.charAt(index) == 'x') {
				if (index > 0) {
					if (isE(this.speakableSyllable.charAt(index - 1))) {
						if (index + 1 < this.speakableSyllable.length()) {
							if (isVowel(this.speakableSyllable
									.charAt(index + 1))) {
								addExRule(index);
							}
						} else if (this.next != null) {
							if (isVowel(this.next.getSyllable().charAt(0))) {
								addExRule(index);
							}
						}
					}
				} else if (this.previous != null) {
					if (isE(this.previous.getSyllable().charAt(
							this.previous.getSyllable().length() - 1))) {
						if (index + 1 < this.speakableSyllable.length()) {
							if (isVowel(this.speakableSyllable
									.charAt(index + 1))) {
								addExRule(index);
							}
						} else if (this.next != null) {
							if (isVowel(this.next.getSyllable().charAt(0))) {
								addExRule(index);
							}
						} else {
							addNotExRule(index);
						}
					} else {
						addNotExRule(index);
					}
				} else {
					addNotExRule(index);
				}
			}
		}
	}

	private String getSpeakableConsonant(char consonant, SpeakableType type) {
		switch (consonant) {
			case 's' :
				return getSpeakableS(type) + "";

			case 'x' :
				return getSpeakableX(type);
		}
		return consonant + "";
	}

	private char getSpeakableS(SpeakableType type) {
		switch (type) {
			case DEFAULT :

				return 'ç';

			case TYPE1 :

				return 'z';

			default :

				return 'ç';
		}
	}

	private String getSpeakableX(SpeakableType type) {
		switch (type) {
			case DEFAULT :

				return "ch";

			case TYPE1 :

				return "ç";

			case TYPE2 :

				return "z";

			case TYPE3 :

				return "ks";

			case TYPE4 :

				return null;

			default :

				return "ch";
		}
	}

	public String makeSpeakableSyllable() {
		speakableSyllable = speakableSyllable.replaceAll("or", "ôr");//TODO: Ver se sempre or=ôr
		speakableSyllable = speakableSyllable.replaceAll("ar", "ár");//TODO: Ver se sempre ar=ár
		speakableSyllable = speakableSyllable.replaceAll("er", "êr");//TODO: Ver se sempre er=êr
		setMOrNRule();

		speakableSyllable = speakableSyllable.replaceAll("w", "v");
		speakableSyllable = speakableSyllable.replaceAll("y", "i");

		speakableSyllable = speakableSyllable.replaceAll("kê", "quê");
		speakableSyllable = speakableSyllable.replaceAll("ké", "qué");
		speakableSyllable = speakableSyllable.replaceAll("ke", "qué");
		speakableSyllable = speakableSyllable.replaceAll("ki", "qui");
		speakableSyllable = speakableSyllable.replaceAll("k", "c");

		speakableSyllable = speakableSyllable.replaceAll("sh", "ch");
		
		speakableSyllable = speakableSyllable.replaceAll("je", "ge");
		speakableSyllable = speakableSyllable.replaceAll("ji", "gi");
		
		speakableSyllable = speakableSyllable.replaceAll("ah", "á");
		speakableSyllable = speakableSyllable.replaceAll("eh", "é");
		speakableSyllable = speakableSyllable.replaceAll("î", "i");
		speakableSyllable = speakableSyllable.replaceAll("í", "i");
		speakableSyllable = speakableSyllable.replaceAll("ih", "i");
		speakableSyllable = speakableSyllable.replaceAll("oh", "ó");
		speakableSyllable = speakableSyllable.replaceAll("û", "ú");
		speakableSyllable = speakableSyllable.replaceAll("ú", "ú");
		speakableSyllable = speakableSyllable.replaceAll("uh", "ú");
		
		speakableSyllable = speakableSyllable.replaceAll("h", "");
		
		speakableSyllable = speakableSyllable.replaceAll("cê", "çê");
		speakableSyllable = speakableSyllable.replaceAll("cé", "çé");
		speakableSyllable = speakableSyllable.replaceAll("ce", "çe");
		speakableSyllable = speakableSyllable.replaceAll("ci", "çi");

		speakableSyllable = speakableSyllable.replaceAll("a", ""
				+ getSpeakableVowel('a'));

		speakableSyllable = speakableSyllable.replaceAll("e", ""
				+ getSpeakableVowel('e'));

		speakableSyllable = speakableSyllable.replaceAll("o", ""
				+ getSpeakableVowel('o'));

		setBetweenVowelsRule();
		setExRule();
		setRRule();

		return speakableSyllable;
	}

	private void setRRule() {//som de r normal: r, som de rrrr: R
		if(this.previous!=null){
			if(this.previous.getSyllable().charAt(this.previous.getSyllable().length()-1)!='r'){
				if(this.getSpeakableSyllable().charAt(0)=='r'){
					String temp=this.speakableSyllable;
					this.speakableSyllable="";
					this.speakableSyllable+='R';
					if(temp.length()>1){
						this.speakableSyllable+=temp.substring(1);
					}
				}
			}
		}
	}

	public Sound getSound() {
		return sound;
	}

	public String getSpeakableSyllable() {
		return speakableSyllable;
	}

	public String getSyllable() {
		return syllable;
	}

	@Override
	public String toString() {
		return syllable;
	}

	public String getNarrator() {
		return narrator;
	}

	public void setNarrator(String narrator) {
		this.narrator = narrator;
	}
}
