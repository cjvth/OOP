javac -d build src/main/java/ru/nsu/cjvth/heapsort/Main.java src/main/java/ru/nsu/cjvth/heapsort/HeapSort.java
javadoc -d jd -sourcepath ./src/main/java/ ru.nsu.cjvth.heapsort
java -classpath build ru.nsu.cjvth.heapsort.Main