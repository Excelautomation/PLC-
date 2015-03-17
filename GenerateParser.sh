#!/bin/bash
rm -r PLC++/src/dk/aau/sw402F15/parser/
java -jar sablecc-3.7/lib/sablecc.jar -d PLC++/src/ PLC++/Grammar
