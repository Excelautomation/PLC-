# PLC++
Compiler til sproget PLC++, som er et imperativt sprog til Omron PLC'er.

[Se projektet på Github](https://github.com/sahb1239/PLC-)

## Kompiler PLC++
For at compilere skal SableCC 3.7 hentes og udpakkes i projektmappen. 
- [SableCC kan hentes her](http://sablecc.org/wiki/DownloadPage)

Efter SableCC er hentet og udpakket køres scriptet GenerateParser.bat (På Windows) eller GenerateParser.sh (På Unix baserede OS).
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

Kompiler et program
Opret nyt projekt i Cx Programmer
Vælg *Projektnavn -> PLC navn -> Programs -> ProgramNavn -> Symbols* i projektoversigten
Åben filen SymbolList.txt og kopir alt indhold vha. ctrl + c og ind i Cx Programmer vha. ctrl + v. En dialogboks ved navn Pastle Symbol skulle komme frem - tryk OK til denne.
Vælg nu *Projektnavn -> PLC navn -> Programs -> ProgramNavn -> Section navn (som standard Section1)*
Vælg menuen View -> Mnemonics
Kopir indholdet af filen InstructionList.txt ind i Cx Programmer vha. ctrl + c og ctrl + v.

Kør derefter programmet vha. Cx Programmer.


