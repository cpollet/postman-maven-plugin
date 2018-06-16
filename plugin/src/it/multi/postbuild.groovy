File collectionFile = new File(basedir, "target/multi-version.json");

assert collectionFile.isFile()
assert ! collectionFile.text.contains("\"item\" : [ ]")

new File(basedir, 'target/multi-version.result').text == "0"
