package crawlers;

import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;

public class Translate {

	private static HashMap<Character, Character> alphaMap = new HashMap<Character, Character>() {
		{
			put('a', 'а');
			put('b', 'б');
			put('c', 'ц');
			put('d', 'д');
			put('e', 'е');
			put('f', 'ф');
			put('g', 'г');
			put('h', 'х');
			put('i', 'и');
			put('j', 'ј');
			put('k', 'к');
			put('l', 'л');
			put('m', 'м');
			put('n', 'н');
			put('o', 'о');
			put('p', 'п');
			put('q', 'љ');
			put('r', 'р');
			put('s', 'с');
			put('t', 'т');
			put('u', 'у');
			put('v', 'в');
			put('w', 'њ');
			put('x', 'џ');
			put('y', 'ѕ');
			put('z', 'з');
			put('[', 'ш');
			put('\\', 'ѓ');
			put('^', 'ч');
			put(']', 'ќ');
			put('@', 'ж');
			put(' ', ' ');
			put('.', '.');
			put('-', '-');
			
		}
	};

	public static String translateWord(String word) {
		char[] alphaArray = word.toCharArray();
		char[] resultArray = new char[alphaArray.length];
		char alpha;
		for (int i = 0; i < alphaArray.length; i++) {
				alpha = Character.toLowerCase(alphaArray[i]);
				resultArray[i] = alphaMap.get(alpha);
		}
		
		String resultWord  = WordUtils.capitalize(String.copyValueOf(resultArray));
		
		return resultWord;
	}
}
