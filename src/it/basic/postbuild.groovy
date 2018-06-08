// import java.util.regex.Pattern

File collectionFile = new File(basedir, "target/simple-it-version.json");

assert collectionFile.isFile()

new File(basedir, 'target/simple-it-version.result').text == "0"
