File collectionFile = new File(basedir, "api/target/classpath-api-version.json");

assert collectionFile.isFile()
assert ! collectionFile.text.contains("\"item\" : [ ]")

new File(basedir, 'api/target/classpath-api-version.result').text == "0"
