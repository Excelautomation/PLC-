# PLC++
Compiler til sproget PLC++, som er et imperativt sprog til Omron PLC'er.

Følgende readme fil er lavet i Githubs markdown format.

[Se projektet på Github](https://github.com/sahb1239/PLC-)

## Compiler PLC++
For at compilere skal SableCC 3.7 hentes og udpakkes i projektmappen. 
- [SableCC kan hentes her](http://sablecc.org/wiki/DownloadPage)

Efter SableCC er hentet og udpakket køres scriptet GenerateParser.bat (På Windows) eller GenerateParser.sh (På Unix baserede öperativsystemer).
- **Bemærk: Java skal være registreret i path**

Herefter åbnes projektet og kompileres i et IDE såsom IntelliJ Community Edition.

## Argumenter
- [filsti] kompilerer den valgte fil
- --std inkluderer standard library (bemærk code generation har ikke færdigimplementeret struct, hvilket standard libary gør brug af)
- --ast pretty printer det abstrakte syntaks træ
- --verbose skriver debug meddelser ud omkring kompileringsprocessen
- --file kompilerer den fil i argumentet lige efter kommandoen (bemærk kommandoen --file behøves ikke at blive kaldt, da det er muligt at skrive filstien direkte)

## Eksekvering af PLC++ programmer
**Bemærk: For at eksekvere PLC++ programmer kræver det at man har installeret programmet Cx Programmer.**

For at eksekvere et PLC++ kræver det nogle manuelle steps da det ikke var muligt at generere projektfiler direkte.

Compiler et program

Opret nyt projekt i Cx Programmer: Vælg CP1H, tryk på settings, vælg "X" under CPU Type.

Vælg *Projektnavn -> PLC navn -> Programs -> ProgramNavn -> Symbols* i projektoversigten

Åben filen SymbolList.txt og kopier alt indhold vha. ctrl + c og ind i Cx Programmer vha. ctrl + v. En dialogboks ved navn Pastle Symbol skulle komme frem - tryk OK til denne.

Vælg nu *Projektnavn -> PLC navn -> Programs -> ProgramNavn -> Section navn (som standard Section1)*

Vælg menuen View -> Mnemonics

Kopir indholdet af filen InstructionList.txt ind i Cx Programmer vha. ctrl + c og ctrl + v.


Kør derefter programmet vha. Cx Programmer.

## Importering i IntelliJ
Udfør først handlingerne beskrevet under overskriften **Compiler PLC++**

Åben IntelliJ

Vælg menuen File -> Open...

Find mappen med projektet og vælg mappen PLC++ og tryk OK

Vælg menuen Run -> Edit configurations...

Tryk på + i venstre hjørne

Vælg Application

Skriv dk.aau.sw402F15.Main i Main class

Skriv kommando argumenter i program arguments

Tryk ok og compiler nu PLC++

## Kørsel af tests
Projektet benytter JUnit til unit tests disse køres således:

Vælg run configuration Run Tests i IntelliJ og kør derefter programmet

Der er 100+ unit tests.

## Struktuerering af projektet
Compileren er struktureret således at hver del af compileren ligger i hver sin mappe - alt kode ligger i mappen src. Alt grammatik ligger i filen Grammar.

### src
Compiler indeholder funktionaliteten til at kalde alle andre dele af programmet.

Rækkefølgen på mapperne nedenunder er rækkefølgen de forskellige dele bliver eksekveret i compileren.
- **Parser**
	- Indeholder alt det autogenererede kode fra SableCC. SableCC generer her både en Lexer, Parser samt træ gennemløbsklasserne.
- **Rewriter**
	- Indeholder funktionalitet der omskriver træet for at generalisere de elementer der kan generaliseres. Eksempel kan compound assignments omskrive til normale assignments og dermed slippes for meget dubleret kode efterfølgende
- **Preprocessor**
	- Registrerer alle funktioner og structs og ligger dem ind i symboltabellen for at understøtte dynamisk scope (benytter klasser fra **SymbolProcessor**, som indlæser funktioner, deklarationer (kun globale deklarationer) og structs)
- **FunctionChecker**
	- Tjekker om kildekoden indeholder en init og run metode og disse har ingen parametre og har ingen returtype
- **ScopeChecker**
	- Håndterer scopecheck. ScopeChecker indlæser også deklarationer i funktioner og benytter en klasse fra **SymbolProcessor** som **Preprocessor** også bruger. Deklarationer i funktioner har statisk scope og skal dermed være deklareret før de bliver brugt.
- **TypeChecker**
	- Håndterer typecheck
- **CodeGenerator**
	- Indeholder alt code generation
	
Derudover indeholder projektet også
- **Symboltable**
	- Indeholder symboltabellen
- **Exception**
	- Indeholder alle compiler exceptions (alle exceptions nedarver CompilerException der ligger i den mappe)
- **Tests**
	- Indeholder alle unit tests

## Eksempler
I mappen Examples ligger der desuden 3 eksempler som kan kompileres vha. PLC++ compileren.

- **fixedFee.ppp**
	- Indeholder et program der tager en pris og ligger et gebyr ovenpå. Under kørslen af funktionen til beregningen af prisen tændes en lampe på port Q#100.00.
- **simpleSensor.ppp**
	- Simpelt program der tænder for port Q#100.0 hvis input port I#0.0 er aktiv.
- **square.ppp**
	- Ganger det samme tal med sig selv i en funktion
	
## Projektet Port
Mappen Port indeholder en simpel compiler der tager port delen fra PLC++. Gramatikken er defineret til SableCC (Dog uden AST).

Projektet indeholder en scanner lavet i hånden. Dele af lexeren (scanneren) er inkluderet i rapporten.