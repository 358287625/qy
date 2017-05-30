#!/bin/bash
echo " params 1 is  $1 "
echo " params 2 is  $2 "

/opt/libreoffice5.2/program/soffice --headless --invisible --convert-to pdf $1 --outdir $2

exit