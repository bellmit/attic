SAMPLES=$(wildcard samples/*.sample)
SAMPLES_PARSER=$(SAMPLES:.sample=.parser)
SAMPLES_COMPILER=$(SAMPLES:.sample=.compiler)

language: compiler.py langParser.py langLexer.py
samples: language $(SAMPLES_PARSER) $(SAMPLES_COMPILER)

compiler.py: compiler.g lang.tokens
	java -jar ~/Downloads/antlr-3.1.3.jar $<

%Parser.py %Lexer.py %.tokens: %.g
	java -jar ~/Downloads/antlr-3.1.3.jar $<

%Parser.py: %Lexer.py

%.parser: %.sample langParser.py langLexer.py
	python langParser.py --rule program $< > $@ 2>&1 || true

%.compiler: %.sample compiler.py langLexer.py langParser.py
	python compiler.py \
		--lexer langLexer \
		--parser langParser \
		--parser-rule program \
		--rule program \
		$< > $@ 2>&1 || true

clean:
	$(RM) samples/*.parser samples/*.compiler
	$(RM) compiler.py compiler.tokens
	$(RM) langParser.py langLexer.py lang.tokens
	find . -name '*.pyc' -delete

.PHONY: language
.PHONY: samples
.PHONY: clean
