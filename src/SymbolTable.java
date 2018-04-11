
public class SymbolTable {
	
	String Symbol;
	String Token;
	String DataType;
	int PointerToSymbolTableEntry;
	public SymbolTable()
	{
		
	}
	public SymbolTable(String symbol, String token, String dataType,
			int pointerToSymbolTableEntry) {
		super();
		Symbol = symbol;
		Token = token;
		DataType = dataType;
		PointerToSymbolTableEntry = pointerToSymbolTableEntry;
	}

	
	
}
