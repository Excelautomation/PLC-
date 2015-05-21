# PLC++
Compiler til sproget PLC++, som er et imperativt sprog til Omron PLC'er.

## Kompiler PLC++
For at compilere skal SableCC 3.7 hentes og udpakkes i projektmappen. Herefter køres scriptet GenerateParser.bat (På Windows) eller GenerateParser.sh (På Unix baserede OS).
Herefter åbnes projektet i en IDE.

Bemærk: Java skal være registreret i path

## Argumenter
- [filsti] kompilerer den valgte fil
- --std inkluderer standard library (bemærk code generation har ikke færdigimplementeret struct, hvilket standard libary gør brug af)
- --ast pretty printer det abstrakte syntaks træ
- --verbose skriver debug meddelser ud omkring kompileringsprocessen
- --file kompilerer den fil i argumentet lige efter kommandoen (bemærk kommandoen --file behøves ikke at blive kaldt, da det er muligt at skrive filstien direkte)