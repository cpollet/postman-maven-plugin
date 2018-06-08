// import java.util.regex.Pattern

File collectionFile = new File(basedir, "target/basic-version.json");

assert collectionFile.isFile()

new File(basedir, 'target/basic-version.result').text == "0"
