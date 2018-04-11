
/*
 * Program Name: Simple Lexical Analyzer
 * Course Title: Compiler Design Laboratory
 * Course id: CSE 4101
 * Author Name: Zakaria Ahammed
 * Student ID: 130224
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {
	// Declaring operator
	public static String[] operator = { "+", "-", "*", "/", "%", "=", ">", "<", "|", "&", "!", "++", "--", "<<", ">>",
			"==", "<=", ">=", "><", "!=", };
	public static String[] operatorTitle = { "Additon", "Subtraction", "Multiplication", "Division", "Modulas",
			"Assign", "GT", "LT", "Bitwise Or", "Bitwise And", "Not", "Increment", "Decrement", "LeftShift",
			"Rightshift", "Equal", "LTE", "GTE", "LG", "NE" };
	// Declaring symbol
	public static String[] symbol = { "(", ")", "{", "}", ";", "," };
	public static String[] symbolTitle = { "Opening braces", "closing braces", "Left carly braces",
			"Right carly braces", "Semicolon", "Comma" };
	// Declaring keywords
	public static String[] keywords = { "if", "for", "while", "int", "float", "do", "return", "double" };

	// crate object for symbol and table
	public static SymbolTable[] symboltable = new SymbolTable[50];
	public static TokenTable[] tokenTable = new TokenTable[100];
	// counter for symbol table
	public static int cst = 0;
	// counter for token table
	public static int ctt = 0;
	// tracking the previous key word
	public static String prekey = "";

	// public static String line;
	public static int f = 0;

	public static void main(String[] args) throws IOException, StringIndexOutOfBoundsException {
		String sElement = "";
		String tempString = "";

		/*
		 * Here i take input from file and take each character in the line.
		 * Finally check them either it keyword,symbol, operator or identifier
		 */
		BufferedReader br = new BufferedReader(new FileReader("inputC.txt"));
		String line;
		// //read file until EOF
		while ((line = br.readLine()) != null) {

			// now check each character in line either it keyword,symbol,
			// operator or identifier
			// System.out.println(line.charAt((line.length()-2)));
			for (int i = 0; i < line.length(); i++) {

				// Skipping comment
				if ((line.charAt(i) == '/' && line.charAt(i + 1) == '/')
						|| (line.charAt(i) == '/' && line.charAt(i + 1) == '*')) {
					if ((line.charAt(i) == '/' && line.charAt(i + 1) == '*')) {
						f = 1;
					}
					break;
				}

				char c = line.charAt(i);
				// check is current character is a whitespace? then previous one
				// must me something
				if (Character.isWhitespace(c)) {
					// check previous string element
					checkContent(sElement);
					sElement = "";

				}
				// check is current character is a symbol? then previous one
				// must me something
				else if (check_token(symbol, Character.toString(c))) {
					checkContent(sElement);// check previous string element
					// insert this new symbol in token table
					tokenTable[ctt] = new TokenTable(Character.toString(c), "Special Symbol",
							symbolTitle[Arrays.asList(symbol).indexOf(Character.toString(c))]);
					ctt++;
					sElement = "";
				}
				// check is current character is a operator? then previous one
				// must me something
				else if (check_token(operator, Character.toString(c))) {
					checkContent(sElement);// check previous string element
					sElement = "";
					// check is current operator is a doubled symbol operator ?
					if (check_token(operator, Character.toString(line.charAt(i + 1)))) {
						// append previous and current operator and check is it
						// still a operator?
						tempString = tempString + c + line.charAt(i + 1);

						if (check_token(operator, tempString)) {
							// is this two are still a operator in together then
							// insert this new operator in token table
							tokenTable[ctt] = new TokenTable(tempString, "Operator",
									operatorTitle[Arrays.asList(operator).indexOf(tempString)]);
							ctt++;
						} else {
							// is this two are not still a operator in together
							// then insert this new operator in token table as
							// invalid operator
							tokenTable[ctt] = new TokenTable(tempString, "Operator", "Invalid Operator");
							ctt++;
						}
						tempString = "";// clear variable
						i++;// point to the next character
					}
					// if not a doubled symbol operator then insert this new
					// operator in token table
					else {

						// tempString = tempString + c;
						tokenTable[ctt] = new TokenTable(Character.toString(c), "Operator",
								operatorTitle[Arrays.asList(operator).indexOf(Character.toString(c))]);
						ctt++;
						// tempString = "";
					}
				}
				// if whitespace,symbol and operator check is failed then append
				// this character with the previous one
				else {
					sElement = sElement + c;
				}
			}
			// checkContent(sElement);
			// sElement = "";
		}
		br.close();

		System.out.println("=========*******========== Lexem Analyzer ==========*******===============");
		// print Token Table
		printTokenTable(tokenTable);
		// print Symbol Table
		printSymbolTable(symboltable);

	}

	// KEYWORD AND IDENTIFIER CHECK
	public static void checkContent(String searchString) {

		if (searchString != "") {
			// is searchString is a keyword then insert it in the token table
			if (Arrays.asList(keywords).contains(searchString)) {

				tokenTable[ctt] = new TokenTable(searchString, "keyword", "\tnull");
				ctt++;
				prekey = searchString; // use for which identifier use this
										// keyword
			}
			// if its not a keyword search for constant or identifier
			else {
				char ch = searchString.charAt(0); // taking the first character
				int flag = 0; // initializing a flag
				int flag1 = 0; // initializing second flag

				// if the first char is a digit then there is scope for being a
				// constant
				if (Character.isDigit(ch) || ch == '.') {
					// check the next char is valid for being a constant?
					for (int i = 1; i < searchString.length(); i++) {
						ch = searchString.charAt(i);
						if (!(Character.isDigit(ch) || ch == '.')) {
							flag = 1;
							// that means this is a invalid constant
							break;
						}
						// check is it contain more than one . ?
						if (ch == '.') {
							flag1++;
						}

					}
					// if searchString doesn't contain any character without
					// digit or . and number of . is not morethan 1
					// then this is a valid constant
					// now insert it in the token table
					if (flag == 0 && flag1 <= 1) {
						tokenTable[ctt] = new TokenTable(searchString, "Number", "\tConstant");
						ctt++;
					}
					// otherwise this is a invalid one
					else {
						tokenTable[ctt] = new TokenTable(searchString, "Invalid", "\tnull");
						ctt++;
					}

				}

				// if the first char is a alphabet or '_' then there is scope
				// for being a identifier
				else if (Character.isAlphabetic(ch) || ch == '_') {
					flag = 0; // reset flag
					// check the next char's is valid for being a identifier?
					for (int i = 1; i < searchString.length(); i++) {
						ch = searchString.charAt(i);

						// if any next char's is not from alphabet or digit or
						// '_'
						// then this one is not a valid identifier
						if (!(Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_')) {
							flag = 1;
						}
					}
					// if a valid identifier
					if (flag == 0) {
						// then check is it already inserted in the symbol
						// table?
						// if yes- then skip
						// if no- then insert this identifier in the symbol
						// table
						if (!checkExistence(searchString)) {
							symboltable[cst] = new SymbolTable(searchString, "id", prekey, cst);
							cst++;
						}
						// also insert to the token table as a valid identifier
						tokenTable[ctt] = new TokenTable(searchString, "id", "\tpointer to symbol table entry");
						ctt++;
					}

					else {
						tokenTable[ctt] = new TokenTable(searchString, "Invalid", "\tnull");
						ctt++;
					}
				}
				// if this is not a valid identifier then insert it in the token
				// table as invalid content
				else {
					tokenTable[ctt] = new TokenTable(searchString, "Invalid", "\tnull");
					ctt++;
				}
			}
		}

	}

	// This is a boolean method which take a string as a parameter
	// and return true if this string is already exists in the symbol table and
	// vice versa
	public static boolean checkExistence(String s) {
		boolean check = false;
		for (int i = 0; i < cst; i++) {
			if (symboltable[i].Symbol.equals(s)) {
				check = true;
				break;
			}
		}
		return check;
	}

	// token checking method
	// This is a boolean method which take a string array and string as a
	// parameter
	// and return true if this string is exists in the string array and vice
	// versa
	public static boolean check_token(String[] str, String token) {
		boolean result = false;
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(token)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static void printSymbolTable(SymbolTable[] st) {
		System.out.println("\n==========================Symbol Table==============================\n");
		System.out.println("----------------------------------------------------------------------");
		System.out.println("Symbol\t\tToken\tData Type\tPointer to symbol table entry");
		System.out.println("----------------------------------------------------------------------");
		for (int i = 0; i < cst; i++) {
			System.out.println(st[i].Symbol + "\t\t" + st[i].Token + "\t" + st[i].DataType + "\t\t"
					+ st[i].PointerToSymbolTableEntry);
		}
		System.out.println("-----------------------------------------------------------------------");
	}

	public static void printTokenTable(TokenTable[] tt) {
		System.out.println("\n============================Token Table==============================\n");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Lexeme\t\tToken\t\t\t\tAttribute");
		System.out.println("-------------------------------------------------------------------------");
		for (int i = 0; i < ctt; i++) {
			System.out.println(tt[i].lexeme + "\t\t" + tt[i].Token + "\t\t" + tt[i].Attribute);
		}
		System.out.println("-------------------------------------------------------------------------");
	}

}
