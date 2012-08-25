SAMPLES=$(wildcard samples/*.sample)
SAMPLE_OUTPUTS=$(SAMPLES:.sample=.output)

language: compiler.py langParser.py langLexer.py
samples: language $(SAMPLE_OUTPUTS)

compiler.py: compiler.g lang.tokens
	java -jar ~/Downloads/antlr-3.1.3.jar $<

%Parser.py %Lexer.py %.tokens: %.g
	java -jar ~/Downloads/antlr-3.1.3.jar $<

%Parser.py: %Lexer.py

%.output: %.sample langParser.py langLexer.py
	python langParser.py --rule program $< > $@ 2>&1 || true

clean:
	$(RM) samples/*.output
	$(RM) compiler.py compiler.tokens
	$(RM) langParser.py langLexer.py lang.tokens
	find . -name '*.pyc' -delete

.PHONY: language
.PHONY: samples
.PHONY: clean
