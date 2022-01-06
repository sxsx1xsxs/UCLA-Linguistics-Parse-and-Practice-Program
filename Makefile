build:
	javac -classpath src src/program/Total.java -d bin/ -source 8 -target 8
	cd bin;\
	jar --create -e program/Total --file ../UCLAPPP.jar .
test:
	java -jar UCLAPPP.jar
