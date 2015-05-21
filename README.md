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
