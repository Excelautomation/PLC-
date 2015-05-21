# PLC++
Compiler til sproget PLC++, som er et imperativt sprog til Omron PLC'er.

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

Opret nyt projekt i Cx Programmer

Vælg *Projektnavn -> PLC navn -> Programs -> ProgramNavn -> Symbols* i projektoversigten

Åben filen SymbolList.txt og kopir alt indhold vha. ctrl + c og ind i Cx Programmer vha. ctrl + v. En dialogboks ved navn Pastle Symbol skulle komme frem - tryk OK til denne.

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
Vælg run configuration under IntelliJ til Run Tests og kør derefter programmet

100+ tests skulle dermed blive kørt.