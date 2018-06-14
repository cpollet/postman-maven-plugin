File collectionFile = new File(basedir, "api/target/classpath-provided-api-version.json");

assert collectionFile.isFile()

new File(basedir, 'api/target/classpath-provided-api-version.result').text == "0"
