File collectionFile = new File(basedir, "target/basic-version.json");

assert collectionFile.isFile()
assert ! collectionFile.text.contains("\"item\" : [ ]")

new File(basedir, 'target/basic-version.result').text == "0"
