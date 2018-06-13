File collectionFile = new File(basedir, "target/multi-version.json");

assert collectionFile.isFile()

new File(basedir, 'target/multi-version.result').text == "0"
