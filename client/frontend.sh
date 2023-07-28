#!/bin/sh
npm run build
rm -r ../src/main/resources/app/
cp -r dist ../src/main/resources/app/