@echo off  

 start c:\progra~1\Intern~1\iexplore.exe http://10.202.86.116:8080/job/alps-server/build?token=service

choice /t 4 /d y /n

taskkill /f /t /im iexplore.exe

exit