class_dir = '../se/out/production/se'

import os
import json
import sys

os.chdir(class_dir)

print('Content-type: application/json; charset=utf-8\n')

listpath = './keywords/'
output = json.dumps(os.listdir(listpath)).encode('utf8')

sys.stdout.buffer.write(output)