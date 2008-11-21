#! /bin/sh

echo "### Generating changes.txt from completed issues"
./change-lister.py --quiet --name "FlowPaint" --project flowpaint --output changes.txt --release `cat VERSION` --temp temporary-issue-list.csv --prefix Release-

rm temporary-issue-list.csv


