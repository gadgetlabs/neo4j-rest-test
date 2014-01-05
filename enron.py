#!/usr/bin/env python
import sys
import os
import glob
import email

#cat enron.data | awk -F" " '{printf("%s %s %s\n",$1, $2, $3)}' | sort | uniq -c | sort -n

def main():
	for filename in glob.glob('/home/badger/Downloads/enron_mail_20110402/maildir/*/*/[0-9]*'):
		try:
			with open(filename, 'r') as fp:
				try:
					msg = email.message_from_string(fp.read())
					tos = [f.strip() for f in msg['to'].split(",")]
					froms = [f.strip() for f in msg['from'].split(",")]

					if len(froms) > 1:
						continue

					for to in tos:
						print "\"{0}\", \"{1}\", \"{2}\"".format(msg['date'], froms[0], to)				 

				except:
					continue			
		except:
			continue


if __name__ == '__main__':
	main()
