# Compilador BNF

 Especicação
---
O objetivo deste programa é ser um compilador básico para traduzir uma gramática livre
de contexto (GLC), representada pela Forma Normal de Bakus (BNF-Backus Normal Form)
para uma representação de classicação de gramáticas GLC.

Este programa foi desenvolvido em duas etapas:

**1. Front-end**

	• Análise Léxica: Analisador léxico para GLC representada por um BNF.
	• Análise Sintática e Semântica: Analisador sintático para uma GLC representada por um BNF.
	  Conjunto First e Conjunto Folow.
	  	  
	  
	  
**2. Back-end**

	• Classicação da gramática: Análise sintática para a gramática gerada.
	  Geração de uma tabela para cada uma dos analisadores sintáticos (LR(0), LR(1), sLR(1), e LALR(1)) 
	  onde se descreve se a gramática pertence ou não ao analisador sintático utilizado.	  
	• Reconhecimento de uma cadeia: Se a gramatica pertencer a um ou mais analisador,
      	  a partir da tabela sintática. 
	  
	  
# Backus-Naur Form
	Define uma notação textual compacta para as produções de uma gramática livre de contexto Notação
	BNF básica:
	
	::=      produção (->)
	<...>    símbolo não-terminal
	"..."    símbolo terminal
    ...|...  alternativa
    [... ]   opcional
	
		
# Exemplo

< S >::="a"< S >
< Q >::="a"< Q > |< Q > "b" |""




