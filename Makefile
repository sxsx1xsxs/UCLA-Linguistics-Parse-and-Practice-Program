build:
	javac -classpath src src/program/Total.java -d bin/
	cd bin;\
	jar --create -e program/Total --file ../UCLAPPP.jar .
test:
	java -jar UCLAPPP.jar
