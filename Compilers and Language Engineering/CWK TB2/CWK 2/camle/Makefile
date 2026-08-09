all:
	./antlr3 Lex.g
	./antlr3 Syn.g
	javac TokenConv.java
	java TokenConv Syn.tokens Irt.java 
	./antlr3 *.java
	./antlr3 camle testsk.w
	./assmule testsk.ass
	./assmule test4.ass

clean:
	rm -f *.class
	rm -f *.txt
	rm -f *.tokens
	rm -f *.ass
	rm -f Syn.java
	rm -f Lex.java
