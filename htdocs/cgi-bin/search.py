class_dir = '../se/out/production/se'
search_engine = 'se.SearchServer'

# enable cgi traceback manager
import cgitb
cgitb.enable()

# get form data
import cgi
form = cgi.FieldStorage()
if 'query' in form:
    query = form['query'].value
else:
    query = ''

# change working dir
import os
os.chdir(class_dir)

# invoke java search engine
os.system('java -Dfile.encoding=UTF-8 -classpath ".\\;..\\..\\..\\lib\\jsoup-1.10.1.jar;..\\..\\..\\lib\\boilerpipe-1.2.2.jar;..\\..\\..\\lib\\nekohtml-1.9.20.jar;..\\..\\..\\lib\\xercesImpl-2.11.0.jar;..\\..\\..\\lib\\xml-apis-1.4.01.jar" ' + search_engine + ' ' + query)
print('test')