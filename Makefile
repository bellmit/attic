SAMPLES=$(wildcard samples/*.sample)
SAMPLE_OUTPUTS=$(SAMPLES:.sample=.output)

samples: $(SAMPLE_OUTPUTS)

%Parser.py %Lexer.py %.tokens: %.g
	java -jar ~/Downloads/antlr-3.1.3.jar $<

%Parser.py: %Lexer.py

%.output: %.sample langParser.py langLexer.py
	python langParser.py --rule program $< > $@ 2>&1 || true

clean:
	$(RM) samples/*.output
	$(RM) langParser.py langLexer.py lang.tokens
	find . -name '*.pyc' -delete

.PHONY: samples
.PHONY: clean
