#!/usr/bin/env bash
echo $1 | sed 's/A/a/g; s/T/t/g; s/C/c/g; s/G/g/g; s/t/A/g; s/a/T/g; s/g/C/g; s/c/G/g;'

