File collectionFile = new File(basedir, "target/environments-version.json");

assert collectionFile.isFile()
assert ! collectionFile.text.contains("\"item\" : [ ]")

new File(basedir, 'target/environments-version.result').text == "0"
