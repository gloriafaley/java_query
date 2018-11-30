/*
  Here, for every format specifier, the 1st argument will be used, hence 1$. Here if we skip the argument_index 
  for 2nd and 3rd format specifier, it tries to find 3 arguments, but we need to use the same argument for
  all 3 format specifiers.

  So, it’s ok if we don’t specify argument _index for the first one, but we need to specify it for the other two.

  The flag here is made up of two characters. Where the first character is always a ‘t’ or ‘T’. The second character 
  depends on what part of Calendar is to be displayed.
*/
public void whenFormatSpecifierForCalendar_thenGotExpected() {
    Calendar c = new GregorianCalendar(2017, 11, 10);
    String s = String.format(
      "The date is: %tm %1$te,%1$tY", c);
 
    assertEquals("The date is: 12 10,2017", s);
}


/*
  Used for the basic types which represent Unicode characters: char, Character, byte, Byte, short, and Short. 
  This conversion can also be used for the types int and Integer when the Character.isValidCodePoint(int) returns true for them.
*/
public void givenString_whenCharConversion_thenConvertedString() {
    String s = String.format("The correct answer is %c", 'a');
    assertEquals("The correct answer is a", s);
 
    s = String.format("The correct answer is %c", null);
    assertEquals("The correct answer is null", s);
 
    s = String.format("The correct answer is %C", 'b');
    assertEquals("The correct answer is B", s);
 
    s = String.format("The valid unicode character: %c", 0x0400);
    assertTrue(Character.isValidCodePoint(0x0400));
    assertEquals("The valid unicode character: Ѐ", s);
}

/*
  These are used for Java integral types: byte, Byte, short, Short, int and Integer, long, Long, and BigInteger. 
  There are three conversions in this category:

  ‘d’ – for decimal number
  ‘o’ – for octal number
  ‘X’ or ‘x’ – for hexadecimal number
*/
public void whenNumericIntegralConversion_thenConvertedString() {
    String s = String.format("The number 25 in decimal = %d", 25);
    assertEquals("The number 25 in decimal = 25", s);
 
    s = String.format("The number 25 in octal = %o", 25);
    assertEquals("The number 25 in octal = 31", s);
 
    s = String.format("The number 25 in hexadecimal = %x", 25);
    assertEquals("The number 25 in hexadecimal = 19", s);
}

/*
  Used for Java floating-point types: float, Float, double, Double, and BigDecimal

  ‘e’ or ‘E’ – formatted as a decimal number in computerized scientific notation
  ‘f’ – formatted as a decimal number
  ‘g’ or ‘G’ – based on the precision value after rounding, this conversion formats into computerized
  scientific notation or decimal format
*/
public void whenNumericFloatingConversion_thenConvertedString() {
    String s = String.format(
      "The computerized scientific format of 10000.00 "
      + "= %e", 10000.00);
  
    assertEquals(
      "The computerized scientific format of 10000.00 = 1.000000e+04", s);
     
    String s2 = String.format("The decimal format of 10.019 = %f", 10.019);
    assertEquals("The decimal format of 10.019 = 10.019000", s2);
}

public void whenCreateFormatter_thenFormatterWithAppendable() {
    StringBuilder sb = new StringBuilder();
    Formatter formatter = new Formatter(sb);
    formatter.format("I am writting to a %s Instance.", sb.getClass());
     
    assertEquals(
      "I am writting to a class java.lang.StringBuilder Instance.", 
      sb.toString());
}

/*
  To get the left-justified column, you need a percentage symbol, a minus symbol, the number of characters, and then the letter "s" (lowercase). So ''%-15s'' means fifteen characters left-justified.

  To get a right-justified column the same sequence of characters are used, except for the minus sign.

  To get a newline %n is used. Note that the characters are surrounded with double quotes.
*/
public void whenJustifyString() {
  String heading2 = "Exam_Grade";
  String heading2 = "Exam_Grade";
  System.out.printf( "%-15s %15s %n", heading1, heading2);
