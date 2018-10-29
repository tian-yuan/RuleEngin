#!/bin/sh
echo `date`
echo `pwd`
echo `ls`
echo $ENV
echo $DEV_META
echo $PRO_MET
echo `java -Denv=$ENV -Ddev_meta=$DEV_META -Dpro_meta=$PRO_META -jar /app.jar`
