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
os.system('java -Dfile.encoding=UTF-8 -classpath "C:\\Program Files\\Java\\jdk1.8.0_25\\jre\\lib\\;.\\;javax.jms.jar;javax.annotation.jar;javax.persistence.jar;javax.servlet.jar;javax.servlet.jsp.jar;javax.transaction.jar;javax.resource.jar;javax.ejb.jar;javax.servlet.jsp.jstl.jar;jsoup-1.10.1.jar;boilerpipe-1.2.2.jar;nekohtml-1.9.20.jar;xercesImpl-2.11.0.jar;xml-apis-1.4.01.jar" ' + search_engine + ' ' + query)
print('test')