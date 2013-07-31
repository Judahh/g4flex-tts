package view;

import util.Phrase;
import util.Number;

public class Console {
	public static void main(String[] args) {
		// System.out.println("sfdasdf");
		// System.out.println("ÁÀASÀPkJPIHJpihàiHIDJÇadjapsdÇÇÁsopççÕADWOEFIWOçÇáéíóúÁÉÍÓÚàèìòùÀÈÌÒÙãẽĩõũÃẼĨÕŨâêîôûÂÊÎÔÛ".toLowerCase());
		Phrase phrase = new Phrase("Geneflides",// "Sim ontem. Não são. Vru.Vrum. Vrão. Vrões. Ermitão. Aldeão. Aldeões. Em um local muito muito distante existia um Gigante, então descobrimos que era um macaco com telescopio no museu do Paraguai, Alexandre"
									// + "E aí Geneflides gostou do TTS?" +
				"brões guões guams brãos guais guasa brãosa gura guõesa guaisa brõespa brães brãespaguaspa brãospa gurpa guõespa guaispa brõespa brãespaguaspa brãospa gurpa guõespa guaispa brõespa brãespaguaspa brãospa gurpa guõespa guaispa brõespa brãespaguaspa brãospa gurpa guõespa guaispa brõespa brãespaguaspa brãospa gurpa guõespa guaispa brõespa brãespaguaspa brãospa gurpa guõespa guaispa brõespa brãespaguaspa brãospa gurpa guõespa guaispa brõespa brãespa"
 //				"Judah, avisa o Felipe que estou com umas 300 idéias pra fazermos a G4Flex bombar mais ainda. "+
				
//				"Manda bala ai no TTS com o Felipe, pois já temos várias aplicações internas pra realizar. "+

//				" felipe"
				// " eddie"//juntar os D's e outros repetidos que não
				// tenho som duplo como rr e ss
				// "Rua Floriano Peixoto, Fortaleza - Ceará, Brasil "+
				// "Rua João Cordeiro, Fortaleza - Ceará, Brasil"
				, 100);
		// Esta demorando porque ele esta falando toda vida logo depois de
		// contruir silaba a silaba... Deve ser melhor contruir todas as silabas
		// e depois falar!
		// phrase.sayPhrase();//por algum motivo isso nao funfa deve ser pq deve
		// cortar ainda mais o audio
		// System.out.println(new Number("1234155125"));
	}
}
