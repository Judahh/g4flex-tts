package util;

public class Number {
	private String zero="zero";
	private String one="um";
	private String two="dois";
	private String three="três";
	private String four="quatro";
	private String five="cinco";
	private String six="seis";
	private String seven="sete";
	private String eight="oito";
	private String nine="nove";
	private String ten="dez";
	private String eleven="onze";
	private String twelve="doze";
	private String thirteen="treze";
	private String fourteen="catorze";
	private String fifteen="quinze";
	private String sixteen="dezesseis";
	private String seventeen="dezessete";
	private String eighteen="dezoito";
	private String nineteen="dezenove";
	private String twenty="vinte";
	private String thirty="trinta";
	private String forty="quarenta";
	private String fifty="cinquenta";
	private String sixty="sessenta";
	private String seventy="setenta";
	private String eighty="oitenta";
	private String ninety="noventa";
	private String oneHundred="cem";
	private String oneHundreds="cento";
	private String twoHundred="duzentos";
	private String threeHundred="trezentos";
	private String fourHundred="quatrocentos";
	private String fiveHundred="quinhetos";
	private String sixHundred="seicentos";
	private String sevenHundred="setecentos";
	private String eightHundred="oitocentos";
	private String nineHundred="novecentos";
	private String thousand="mil";
	private String million="milhão";
	private String millions="milhões";
	private String billion="bilhão";
	private String billions="bilhões";
//	private String trillion="trilhão";
//	private String quadrillion="quadrilhão";
//	private String quintillion="quintilhão";
	
	
	private String stringNumber;
	private int number;
	
	public Number(int number) {
		this.number=number;
		this.stringNumber=getStringNumber();
	}
	
	public Number(String number) {
		this.number=Integer.parseInt(number);
		this.stringNumber=getStringNumber();
	}
	
	public int getNumber() {
		return number;
	}
	
	private String getStringNumber() {
		String stringNumber="";
		int number=this.number;
		int unit;
//		int hundred;
		int thousand;
		int million;
		int billion;
//		int trillion;
//		int quadrillion;
//		int quintillion;
		
		billion=(int)number/1000000000;
		number=number%1000000000;
		
		million=(int)number/1000000;
		number=number%1000000;
		
		thousand=(int)number/1000;
		number=number%1000;
		
		unit=(int)number;
		
		boolean nullNumber=true;
		
		if(billion>0){
			nullNumber=false;
			stringNumber+=" "+getHundred(billion, nullNumber);
			if(billion==1){
				stringNumber+=" "+this.billion;
			}else{
				stringNumber+=" "+this.billions;
			}
//			if(million>0||thousand>0||unit>0){
//				stringNumber+=",";
//			}
		}
		
		if(million>0){
			nullNumber=false;
			stringNumber+=" "+getHundred(million, nullNumber);
			if(million==1){
				stringNumber+=" "+this.million;
			}else{
				stringNumber+=" "+this.millions;
			}
//			if(thousand>0||unit>0){
//				stringNumber+=",";
//			}
		}
		
		if(thousand>0){
			nullNumber=false;
			if(thousand>1){
				stringNumber+=" "+getHundred(thousand, nullNumber);
			}
			stringNumber+=" "+this.thousand;
//			if(unit>0){
//				stringNumber+=",";
//			}
		}
		
		if(unit>0){
			nullNumber=false;
		}
		
		stringNumber+=" "+getHundred(unit, nullNumber);
		
		return stringNumber;
	}
	
	private String getSingleHundred(int number){
		switch(number){
			case 2:
				return this.twoHundred;
			case 3:
				return this.threeHundred;
			case 4:
				return this.fourHundred;
			case 5:
				return this.fiveHundred;
			case 6:
				return this.sixHundred;
			case 7:
				return this.sevenHundred;
			case 8:
				return this.eightHundred;
			case 9:
				return this.nineHundred;
			default:
				return this.oneHundreds;	
		}
	}
	
	private String getHundred(int number,boolean nullNumber){
		String stringHundred="";
		int unit;
		int ten;
		int hundred;
		
		hundred=(int)number/100;
		number=number%100;
		
		unit=(int)number;
		
		if(hundred>0){
			if(hundred==1){
				if(unit==0){
					stringHundred+=this.oneHundred;
				}else{
					stringHundred+=getSingleHundred(hundred)+" e "+getTen(unit, nullNumber);
				}
			}else{
				if(unit==0){
					stringHundred+=getSingleHundred(hundred);
				}else{
					stringHundred+=getSingleHundred(hundred)+" e "+getTen(unit, nullNumber);
				}
			}
		}else{
			stringHundred+=getTen(unit, nullNumber);
		}		
		return stringHundred;
	}
	
	private String getSingleTen(int ten,int unit){
		if(ten==1){
			switch(unit){
				case 1:
					return this.eleven;
				case 2:
					return this.twelve;
				case 3:
					return this.thirteen;
				case 4:
					return this.fourteen;
				case 5:
					return this.fifteen;
				case 6:
					return this.sixteen;
				case 7:
					return this.seventeen;
				case 8:
					return this.eighteen;
				case 9:
					return this.nineteen;	
				default:
					return this.ten;
			}
		}else{
			switch(ten){
				case 2:
					return this.twenty;
				case 3:
					return this.thirty;
				case 4:
					return this.forty;
				case 5:
					return this.fifty;
				case 6:
					return this.sixty;
				case 7:
					return this.seventy;
				case 8:
					return this.eighty;
				case 9:
					return this.ninety;	
			}
		}
		return this.ten;
		
	}
	
	private String getTen(int number,boolean nullNumber){
		String stringTen="";
		int unit;
		int ten;
		
		ten=(int)number/10;
		number=number%10;
		
		unit=(int)number;
		
		if(ten>0){
			if(ten==1){
				stringTen+=getSingleTen(ten, unit);
			}else{
				if(unit==0){
					stringTen+=getSingleTen(ten, unit);
				}else{
					stringTen+=getSingleTen(ten, unit)+" e "+getUnit(unit, nullNumber);
				}
			}
		}else{
			stringTen+=getUnit(unit, nullNumber);
		}	
		
		return stringTen;
	}
	
	private String getUnit(int number, boolean nullNumber){
		System.out.println("Number:"+number+",bool:"+nullNumber);
		switch (number) {
			case 1 :
				return this.one;
			case 2 :
				return this.two;
			case 3 :
				return this.three;
			case 4 :
				return this.four;
			case 5 :
				return this.five;
			case 6 :
				return this.six;
			case 7 :
				return this.seven;
			case 8 :
				return this.eight;
			case 9 :
				return this.nine;

			default :
				if(nullNumber){
					return this.zero;
				}
				return "";
		}
	}
	
	@Override
	public String toString() {
		return this.stringNumber;
	}
	
	public String StringNumber() {
		return this.stringNumber;
	}
}
